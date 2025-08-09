package com.ddoongs.chatting.dto.domain;

import com.ddoongs.chatting.constants.UserConnectionsStatus;

public record Connection(String username, UserConnectionsStatus status) {

}
