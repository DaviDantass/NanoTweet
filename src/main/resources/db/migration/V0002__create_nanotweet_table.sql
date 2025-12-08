CREATE TABLE nanotweet
(
    id                     BIGINT NOT NULL AUTO_INCREMENT,
    content                VARCHAR(42) NOT NULL,
    type                   ENUM('ORIGINAL', 'REPOST', 'QUOTE') NOT NULL,
    author_id              BIGINT NOT NULL,
    original_nanotweet_id  BIGINT,
    created_at             DATETIME NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_nanotweet_user
        FOREIGN KEY (author_id) REFERENCES `user` (id),

    CONSTRAINT fk_original_nanotweet
        FOREIGN KEY (original_nanotweet_id) REFERENCES nanotweet (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
