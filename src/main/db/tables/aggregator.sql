CREATE TABLE aggregator (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  displayName    VARCHAR(2000),
  feed_id        BIGINT,
  subscriber_id  BIGINT
);