CREATE TABLE post
(
    id                BIGINT AUTO_INCREMENT,
    content           VARCHAR(42) NOT NULL,
    type              VARCHAR(20) NOT NULL,
    author_id         BIGINT NOT NULL,
    original_post_id  BIGINT,
    created_at        TIMESTAMP NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_post_user
        FOREIGN KEY (author_id) REFERENCES user (id),

    CONSTRAINT fk_original_post
        FOREIGN KEY (original_post_id) REFERENCES post (id)
);
