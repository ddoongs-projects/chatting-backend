package com.ddoongs.chatting.dto

import com.ddoongs.chatting.dto.websocket.inbound.BaseRequest
import com.ddoongs.chatting.dto.websocket.inbound.InviteRequest
import com.ddoongs.chatting.dto.websocket.inbound.KeepAliveRequest
import com.ddoongs.chatting.dto.websocket.inbound.WriteChatRequest
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
        payload                                                                    | expectedClass    | validate
        '{"type": "INVITE_REQUEST", "userInviteCode": "TestInviteCode123"}'        | InviteRequest    | { req -> { (req as InviteRequest).userInviteCode.code() == "TestInviteCode123" } }
        '{"type": "WRITE_CHAT", "username": "testuser", "content": "testmessage"}' | WriteChatRequest | { req -> { (req as WriteChatRequest).content == "testmessage" } }
        '{"type": "KEEP_ALIVE"}'                                                   | KeepAliveRequest | { req -> { (req as KeepAliveRequest).getType() == "KEEP_ALIVE" } }
    }
}
