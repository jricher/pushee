INSERT INTO FEED (id, url, type) VALUES (1, 'http://example.com/1', 1);
INSERT INTO FEED (id, url, type) VALUES (2, 'http://example.com/2', 0);

INSERT INTO SUBSCRIBER (id, postbackURL) VALUES (1, 'http://example.com/sub/1');
INSERT INTO SUBSCRIBER (id, postbackURL) VALUES (2, 'http://example.com/sub/2');

INSERT INTO SUBSCRIPTION (id, feed_id, subscriber_id, postbackURL) VALUES (1, 1, 1, 'http://example.com/sub/1');
INSERT INTO SUBSCRIPTION (id, feed_id, subscriber_id, postbackURL) VALUES (2, 1, 2, 'http://example.com/sub/2');

INSERT INTO PUBLISHER (id, callbackURL) VALUES (1, 'http://example.com/pub/1');
INSERT INTO PUBLISHER (id, callbackURL) VALUES (2, 'http://example.com/pub/2');

INSERT INTO CLIENTDETAILS (clientId, clientSecret, webServerRedirectUri, clientName, clientDescription, allowRefresh) VALUES ('1', 'asdf', 'http://example.com/client1', 'Client 1', 'The first client', 0);
INSERT INTO CLIENTDETAILS (clientId, clientSecret, webServerRedirectUri, clientName, clientDescription, allowRefresh) VALUES ('2', 'ghjkl', 'http://example.com/client2', 'Client 2', 'The second client', 0);

INSERT INTO ACCESSTOKEN (id, tokenType, refresh_token_id, client_id) VALUES ('1asdf', 'bearer', '1', '1');
INSERT INTO ACCESSTOKEN (id, tokenType, refresh_token_id, client_id) VALUES ('jklop2', 'bearer', '5', '2');

INSERT INTO REFRESHTOKEN (id, client_id) VALUES ('1', '1');
INSERT INTO REFRESHTOKEN (id, client_id) VALUES ('5', '2');

INSERT INTO AGGREGATOR (id, displayName, feed_id, subscriber_id) VALUES (1, 'Aggregator One', 1, 2);
INSERT INTO AGGREGATOR (id, displayName, feed_id, subscriber_id) VALUES (2, 'Aggregator Two', 2, 1);