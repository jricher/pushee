CREATE TABLE subscription (
  id            	BIGINT AUTO_INCREMENT,
  feed_id        	BIGINT,
  timeout		 	DATE,
  secret		 	VARCHAR(2000),
  subscriber_id	BIGINT,
  postbackURL    	VARCHAR(2000)
);