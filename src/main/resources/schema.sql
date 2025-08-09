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

CREATE TABLE IF NOT EXISTS chat_user
(
    user_id                BIGINT AUTO_INCREMENT,
    username               VARCHAR(20)  NOT NULL,
    password               VARCHAR(255) NOT NULL,
    connection_invite_code VARCHAR(32)  NOT NULL,
    connection_count       INT          NOT NULL,
    created_at             TIMESTAMP    NOT NULL,
    updated_at             TIMESTAMP    NOT NULL,
    PRIMARY KEY (user_id),
    CONSTRAINT unique_username UNIQUE (username),
    CONSTRAINT unique_friend_invite_code UNIQUE (connection_invite_code)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS user_connection
(
    partner_a_user_id BIGINT      NOT NULL,
    partner_b_user_id BIGINT      NOT NULL,
    status            VARCHAR(32) NOT NULL,
    inviter_user_id   BIGINT      NOT NULL,
    created_at        TIMESTAMP   NOT NULL,
    updated_at        TIMESTAMP   NOT NULL,

    PRIMARY KEY (partner_a_user_id, partner_b_user_id),
    INDEX idx_friend_b_user_id (partner_b_user_id),
    INDEX idx_friend_a_user_id (partner_a_user_id),
    INDEX idx_friend_b_user_id_status (partner_b_user_id, status),
    INDEX idx_friend_a_user_id_status (partner_a_user_id, status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
