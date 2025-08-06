package com.ddoongs.chatting.handler

import com.ddoongs.chatting.ChattingApplication
import com.ddoongs.chatting.dto.Chat
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.handler.TextWebSocketHandler
import spock.lang.Specification

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.TimeUnit

@SpringBootTest(
        classes = ChattingApplication,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class ChatHandlerSpec extends Specification {

    @LocalServerPort
    int port

    @Autowired
    ObjectMapper objectMapper

    def "Group Chat Basic Test"() {
        given:
        def url = "ws://localhost:${port}/ws/v1/chat"
        def (clientA, clientB, clientC) = [createClient(url), createClient(url), createClient(url)]

        when:
        clientA.session.sendMessage(new TextMessage(objectMapper.writeValueAsString(new Chat("clientA", "안녕하세요. A 입니다."))))
        clientB.session.sendMessage(new TextMessage(objectMapper.writeValueAsString(new Chat("clientB", "안녕하세요. B 입니다."))))
        clientC.session.sendMessage(new TextMessage(objectMapper.writeValueAsString(new Chat("clientC", "안녕하세요. C 입니다."))))

        then:
        def resultA = clientA.queue.poll(1, TimeUnit.SECONDS) + clientA.queue.poll(1, TimeUnit.SECONDS)
        def resultB = clientB.queue.poll(1, TimeUnit.SECONDS) + clientB.queue.poll(1, TimeUnit.SECONDS)
        def resultC = clientC.queue.poll(1, TimeUnit.SECONDS) + clientC.queue.poll(1, TimeUnit.SECONDS)

        resultA.contains("clientB") && resultA.contains("clientC")
        resultB.contains("clientC") && resultB.contains("clientA")
        resultC.contains("clientA") && resultC.contains("clientB")

        and:
        clientA.queue.isEmpty()
        clientB.queue.isEmpty()
        clientC.queue.isEmpty()

        cleanup:
        clientA.session?.close()
        clientB.session?.close()
        clientC.session?.close()
    }

    static def createClient(String url) {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1)
        def client = new StandardWebSocketClient()
        WebSocketSession webSocketSession = client.execute(new TextWebSocketHandler() {
            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                blockingQueue.put(message.payload)
            }
        }, url).get()

        [queue: blockingQueue, session: webSocketSession]
    }
}
