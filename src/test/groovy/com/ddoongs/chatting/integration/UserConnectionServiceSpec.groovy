package com.ddoongs.chatting.integration

import com.ddoongs.chatting.ChattingApplication
import com.ddoongs.chatting.dto.domain.UserId
import com.ddoongs.chatting.repository.UserConnectionRepository
import com.ddoongs.chatting.repository.UserRepository
import com.ddoongs.chatting.service.UserConnectionLimitService
import com.ddoongs.chatting.service.UserConnectionService
import com.ddoongs.chatting.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(classes = ChattingApplication.class)
public class UserConnectionServiceSpec extends Specification {
    @Autowired
    UserService userService;

    @Autowired
    UserConnectionService userConnectionService;

    @Autowired
    UserConnectionLimitService userConnectionLimitService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserConnectionRepository userConnectionRepository;

    def "연결 요청 수락은 연결 제한 수를 넘을 수 없다"() {
        given:
        userConnectionLimitService.setLimitConnections(10)
        (0..19).collect {
            userService.addUser("testUser${it}", "testPass${it}")
        }

        def userIdA = userService.getUserId("testUser0").get()
        def inviteCodeA = userService.getInviteCode(userIdA).get()

        (1..9).collect {
            userConnectionService.invite(userService.getUserId("testUser${it}").get(), inviteCodeA)
            userConnectionService.accept(userIdA, "testUser${it}")
        }

        def inviteCodes = (10..19).collect {
            userService.getInviteCode(userService.getUserId("testUser${it}").get()).get()
        }
        inviteCodes.each { userConnectionService.invite(userIdA, it) }

        def results = Collections.synchronizedList(new ArrayList<Optional<UserId>>())


        when:
        def threads = (10..19).collect { idx ->
            Thread.start {
                def userId = userService.getUserId("testUser${idx}")
                results << userConnectionService.accept(userId.get(), "testUser0").getFirst()
            }
        }
        threads*.join()


        then:
        results.count { it.isPresent() } == 1


        cleanup:
        (0..19).each {
            def userId = userService.getUserId("testUser${it}").get()
            userRepository.deleteById(userId.id())
        }
    }


}
