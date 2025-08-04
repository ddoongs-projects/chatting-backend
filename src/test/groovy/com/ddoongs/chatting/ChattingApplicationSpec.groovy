package com.ddoongs.chatting

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ChattingApplicationSpec extends Specification {

    def "context loads"() {
        expect:
        true
    }

}
