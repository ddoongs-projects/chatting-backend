package com.ddoongs.chatting.dto

import com.ddoongs.chatting.constants.MessageType
import com.ddoongs.chatting.constants.UserConnectionsStatus
import com.ddoongs.chatting.dto.websocket.inbound.*
import com.ddoongs.chatting.json.JsonUtils
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class RequestTypeMappingSpec extends Specification {

    JsonUtils jsonUtils = new JsonUtils(new ObjectMapper());

    def "DTO 형식의 JSON 문자열을 해당 타입의 DTO로 변환할 수 있다"() {
        given:
        String jsonBody = payload


        when:
        BaseRequest request = jsonUtils.fromJson(jsonBody, BaseRequest).get()


        then:
        request.getClass() == expectedClass
        validate(request)


        where:
        payload                                                                    | expectedClass               | validate
        '{"type": "FETCH_USER_INVITE_CODE_REQUEST"}'                               | FetchUserInviteCodeRequest  | { req -> { (req as FetchUserInviteCodeRequest).getType() == MessageType.FETCH_USER_INVITE_CODE_REQUEST } }
        '{"type": "FETCH_CONNECTIONS_REQUEST", "status": "ACCEPTED"}'              | FetchConnectionsRequest     | { req -> { (req as FetchConnectionsRequest).status == UserConnectionsStatus.ACCEPTED } }
        '{"type": "INVITE_REQUEST", "userInviteCode": "TestInviteCode123"}'        | InviteRequest               | { req -> { (req as InviteRequest).userInviteCode.code() == "TestInviteCode123" } }
        '{"type": "ACCEPT_INVITE_REQUEST", "username": "testuser"}'                | AcceptInviteRequest         | { req -> { (req as AcceptInviteRequest).getUsername() == "testuser" } }
        '{"type": "REJECT_INVITE_REQUEST", "username": "testuser"}'                | RejectInviteRequest         | { req -> { (req as RejectInviteRequest).getUsername() == "testuser" } }
        '{"type": "DISCONNECT_CONNECTION_REQUEST", "username": "testuser"}'        | DisconnectConnectionRequest | { req -> { (req as DisconnectConnectionRequest).getUsername() == "testuser" } }
        '{"type": "WRITE_CHAT", "username": "testuser", "content": "testmessage"}' | WriteChat                   | { req -> { (req as WriteChat).content == "testmessage" } }
        '{"type": "KEEP_ALIVE"}'                                                   | KeepAlive                   | { req -> { (req as KeepAlive).getType() == MessageType.KEEP_ALIVE } }
    }
}
