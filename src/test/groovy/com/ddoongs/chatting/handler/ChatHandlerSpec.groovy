package com.ddoongs.chatting.handler

import com.ddoongs.chatting.ChattingApplication
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
    private int port

    @Autowired
    private ObjectMapper objectMapper

    def "Direct Chat Basic Test"() {
        given:
        def url = "ws://localhost:${port}/ws/v1/chat"

        BlockingQueue<String> leftQueue = new ArrayBlockingQueue<>(1)
        def leftClient = new StandardWebSocketClient()
        def leftWebSocketSession = leftClient.execute(new TextWebSocketHandler() {
            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                leftQueue.put(message.payload)
            }
        }, url).get()

        BlockingQueue<String> rightQueue = new ArrayBlockingQueue<>(1)
        def rightClient = new StandardWebSocketClient()
        def rightWebSocketSession = rightClient.execute(new TextWebSocketHandler() {
            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                rightQueue.put(message.payload)
            }
        }, url).get()

        when:
        leftWebSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(new Chat("안녕하세요."))))
        rightWebSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(new Chat("Hello."))))

        then:
        rightQueue.poll(1, TimeUnit.SECONDS).contains("안녕하세요.")

        and:
        leftQueue.poll(1, TimeUnit.SECONDS).contains("Hello.")

        cleanup:
        leftWebSocketSession?.close()
        rightWebSocketSession?.close()
    }
}
