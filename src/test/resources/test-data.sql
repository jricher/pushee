INSERT INTO FEED (id, url, type) VALUES (1, 'http://example.com/1', 1);
INSERT INTO FEED (id, url, type) VALUES (2, 'http://example.com/2', 0);

INSERT INTO SUBSCRIBER (id, postbackURL) VALUES (1, 'http://example.com/sub/1');
INSERT INTO SUBSCRIBER (id, postbackURL) VALUES (2, 'http://example.com/sub/2');

INSERT INTO SUBSCRIPTION (id, feed_id, subscriber_id, postbackURL) VALUES (1, 1, 1, 'http://example.com/sub/1');
INSERT INTO SUBSCRIPTION (id, feed_id, subscriber_id, postbackURL) VALUES (2, 1, 2, 'http://example.com/sub/2');

INSERT INTO PUBLISHER (id, callbackURL) VALUES (1, 'http://example.com/pub/1');
INSERT INTO PUBLISHER (id, callbackURL) VALUES (2, 'http://example.com/pub/2');