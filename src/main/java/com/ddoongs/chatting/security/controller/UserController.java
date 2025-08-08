package com.ddoongs.chatting.security.controller;

import com.ddoongs.chatting.dto.restapi.UserRegisterRequest;
import com.ddoongs.chatting.service.ChatUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RestController
public class ChatUserController {


  private static final Logger log = LoggerFactory.getLogger(ChatUserController.class);
  private final ChatUserService chatUserService;

  public ChatUserController(ChatUserService chatUserService) {
    this.chatUserService = chatUserService;
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody UserRegisterRequest request) {
    try {
      chatUserService.addUser(request.username(), request.password());
      return ResponseEntity.ok("User registered.");
    } catch (Exception ex) {
      log.error("Add user failed. cause: {}", ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Register user failed.");
    }
  }

  @PostMapping("/unregister")
  public ResponseEntity<String> register(HttpServletRequest request) {
    try {
      chatUserService.removeUser();
      request.getSession().invalidate();
      return ResponseEntity.ok("User unregistered.");
    } catch (Exception ex) {
      log.error("Remove user failed. cause: {}", ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Unregister user failed.");
    }
  }

}
