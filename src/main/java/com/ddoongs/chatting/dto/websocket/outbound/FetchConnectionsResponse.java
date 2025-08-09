package com.ddoongs.chatting.dto.websocket.outbound;

import com.ddoongs.chatting.constants.MessageType;
import com.ddoongs.chatting.dto.domain.Connection;
import java.util.List;

public class FetchConnectionsResponse extends BaseMessage {

  private final List<Connection> connections;

  public FetchConnectionsResponse(List<Connection> connections) {
    super(MessageType.FETCH_CONNECTIONS_RESPONSE);
    this.connections = connections;
  }

  public List<Connection> getConnections() {
    return connections;
  }
}
