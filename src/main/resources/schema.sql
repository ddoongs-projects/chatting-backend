CREATE TABLE IF NOT EXISTS chat
(
    chat_sequence BIGINT AUTO_INCREMENT,
    user_name     VARCHAR(20)   NOT NULL,
    content       VARCHAR(1000) NOT NULL,
    created_at    TIMESTAMP     NOT NULL,
    updated_at    TIMESTAMP     NOT NULL,
    PRIMARY KEY (chat_sequence)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;