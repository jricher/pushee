CREATE TABLE feed (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  url            VARCHAR(2000),
  type           INT(3),
  publisher_id   BIGINT
);