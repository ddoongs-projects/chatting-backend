package com.ddoongs.chatting.integration

import com.ddoongs.chatting.ChattingApplication
import com.ddoongs.chatting.constants.UserConnectionsStatus
import com.ddoongs.chatting.dto.domain.UserId
import com.ddoongs.chatting.entity.UserConnectionId
import com.ddoongs.chatting.repository.UserConnectionRepository
import com.ddoongs.chatting.repository.UserRepository
import com.ddoongs.chatting.service.UserConnectionLimitService
import com.ddoongs.chatting.service.UserConnectionService
import com.ddoongs.chatting.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(classes = ChattingApplication.class)
class UserConnectionServiceSpec extends Specification {
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

    def cleanup() {
        (0..19).each {
            userService.getUserId("testUser${it}").ifPresent { userId ->
                userRepository.deleteById(userId.id())

                for (def status : UserConnectionsStatus.values()) {
                    userConnectionRepository.findConnectionsByPartnerAUserIdAndStatus(userId.id(), status).each {
                        userConnectionRepository.deleteById(new UserConnectionId(
                                Long.min(userId.id(), it.getUserId()),
                                Long.max(userId.id(), it.getUserId())
                        ))
                    }
                    userConnectionRepository.findConnectionsByPartnerBUserIdAndStatus(userId.id(), status).each {
                        userConnectionRepository.deleteById(new UserConnectionId(
                                Long.min(userId.id(), it.getUserId()),
                                Long.max(userId.id(), it.getUserId())
                        ))
                    }
                }
            }
        }
    }

    def "연결 요청 수락은 연결 제한 수를 넘을 수 없다"() {
        given:
        userConnectionLimitService.setLimitConnections(10)
        (0..19).each {
            userService.addUser("testUser${it}", "testPass${it}")
        }

        def userIdA = userService.getUserId("testUser0").get()
        def inviteCodeA = userService.getInviteCode(userIdA).get()

        (1..9).each {
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


    }


    def "연결 카운트는 연결 삭제 시 정확히 반영된다"() {
        given:
        (0..10).each {
            userService.addUser("testUser${it}", "testPass${it}")
        }

        def userIdA = userService.getUserId("testUser0").get()
        def inviteCodeA = userService.getInviteCode(userIdA).get()

        (1..10).each {
            userConnectionService.invite(userService.getUserId("testUser${it}").get(), inviteCodeA)
        }

        (1..5).each {
            userConnectionService.accept(userIdA, "testUser${it}")
        }

        def results = Collections.synchronizedList(new ArrayList<Boolean>())


        when:
        def threads = (1..10).collect { idx ->
            Thread.start {
                def userId = userService.getUserId("testUser${idx}")
                results << userConnectionService.disconnect(userId.get(), "testUser0").getFirst()
            }
        }
        threads*.join()


        then:
        results.count { it == true } == 5
        userService.getConnectionCount(userService.getUserId("testUser0").get()).get() == 0


    }

}
