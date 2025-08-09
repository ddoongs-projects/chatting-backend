package com.ddoongs.chatting.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {

  private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

  private final ObjectMapper objectMapper;

  public JsonUtils(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public <T> Optional<T> fromJson(String json, Class<T> clazz) {
    try {
      return Optional.of(objectMapper.readValue(json, clazz));
    } catch (IOException ex) {
      log.error("Failed JSON to Object: {}", ex.getMessage());
      return Optional.empty();
    }
  }

  public Optional<String> toJson(Object obj) {
    try {
      return Optional.of(objectMapper.writeValueAsString(obj));
    } catch (JsonProcessingException ex) {
      log.error("Filed Object to Json: {}", ex.getMessage());
      return Optional.empty();
    }
  }
}
