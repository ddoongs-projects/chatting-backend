package com.ddoongs.chatting.service

import com.ddoongs.chatting.constants.UserConnectionsStatus
import com.ddoongs.chatting.dto.domain.InviteCode
import com.ddoongs.chatting.dto.domain.User
import com.ddoongs.chatting.dto.domain.UserId
import com.ddoongs.chatting.dto.projection.UserConnectionStatusProjection
import com.ddoongs.chatting.repository.UserConnectionRepository
import org.springframework.data.util.Pair
import spock.lang.Specification

class UserConnectionServiceSpec extends Specification {

    UserConnectionService userConnectionService
    UserService userService = Stub()
    UserConnectionRepository userConnectionRepository = Stub()

    def setup() {
        userConnectionService = new UserConnectionService(userService, userConnectionRepository)
    }

    def "사용자 연결 신청에 대한 테스트"() {
        given:
        userService.getUser(inviteCodeOfTargetUser) >> Optional.of(new User(targetUserId, targetUsername))
        userService.getUsername(senderUserId) >> Optional.of(senderUsername)
        userConnectionRepository.findStatusByPartnerAUserIdAndPartnerBUserId(_ as Long, _ as Long) >> {
            Optional.of(Stub(UserConnectionStatusProjection) {
                getStatus() >> beforeConnectionStatus.name()
            })
        }


        when:
        def result = userConnectionService.invite(senderUserId, usedInviteCode)


        then:
        result == expectedResult


        where:
        scenario              | senderUserId  | senderUsername | targetUserId  | targetUsername | inviteCodeOfTargetUser      | usedInviteCode               | beforeConnectionStatus             | expectedResult
        'Valid invite code'   | new UserId(1) | 'userA'        | new UserId(2) | 'userB'        | new InviteCode('user2code') | new InviteCode('user2code')  | UserConnectionsStatus.NONE         | Pair.of(Optional.of(new UserId(2)), 'userA')
        'Already Connected'   | new UserId(1) | 'userA'        | new UserId(2) | 'userB'        | new InviteCode('user2code') | new InviteCode('user2code')  | UserConnectionsStatus.ACCEPTED     | Pair.of(Optional.of(new UserId(2)), "Already connected with " + targetUsername)
        'Already Invited'     | new UserId(1) | 'userA'        | new UserId(2) | 'userB'        | new InviteCode('user2code') | new InviteCode('user2code')  | UserConnectionsStatus.PENDING      | Pair.of(Optional.of(new UserId(2)), "Already invited to " + targetUsername)
        'Already Rejected'    | new UserId(1) | 'userA'        | new UserId(2) | 'userB'        | new InviteCode('user2code') | new InviteCode('user2code')  | UserConnectionsStatus.REJECTED     | Pair.of(Optional.of(new UserId(2)), "Already invited to " + targetUsername)
        'After Disconnected'  | new UserId(1) | 'userA'        | new UserId(2) | 'userB'        | new InviteCode('user2code') | new InviteCode('user2code')  | UserConnectionsStatus.DISCONNECTED | Pair.of(Optional.of(new UserId(2)), 'userA')
        'Invalid Invite Code' | new UserId(1) | 'userA'        | new UserId(2) | 'userB'        | new InviteCode('user2code') | new InviteCode('nobodycode') | UserConnectionsStatus.DISCONNECTED | Pair.of(Optional.empty(), "Invalid invite code")
        'Self Invite Code'    | new UserId(1) | 'userA'        | new UserId(1) | 'userA'        | new InviteCode('user1code') | new InviteCode('user1code')  | UserConnectionsStatus.DISCONNECTED | Pair.of(Optional.empty(), "Can't self invite")
    }
}
