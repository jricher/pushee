package org.mitre.pushee.hub.web;

public interface PuSHProtocolParameters {

	public static final String HUB_URL = "hub.url";
	public static final String HUB_SECRET = "hub.secret";
	public static final String HUB_CALLBACK = "hub.callback";
	public static final String HUB_VERIFY = "hub.verify";
	public static final String HUB_VERIFY_TOKEN = "hub.verify_token";
	public static final String HUB_LEASE_SECONDS = "hub.lease_seconds";
	public static final String HUB_CHALLENGE = "hub.challenge";
	public static final String HUB_TOPIC = "hub.topic";
	public static final String HUB_MODE = "hub.mode";
	public static final String HUB_MODE_UNSUBSCRIBE = HUB_MODE + "=unsubscribe";
	public static final String HUB_MODE_SUBSCRIBE = HUB_MODE + "=subscribe";
	public static final String HUB_MODE_PUBLISH = HUB_MODE + "=publish";

}