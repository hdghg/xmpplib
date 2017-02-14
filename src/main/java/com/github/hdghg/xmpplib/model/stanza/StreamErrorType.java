package com.github.hdghg.xmpplib.model.stanza;

public enum StreamErrorType {
    BAD_REQUEST("bad-request"),
    CONFLICT("conflict"),
    FEATURE_NOT_IMPLEMENTED("feature-not-implemented"),
    FORBIDDEN("forbidden"),
    GONE("gone"),
    INTERNAL_SERVER_ERROR("internal-server-error"),
    ITEM_NOT_FOUND("item-not-found"),
    JID_MALFORMED("jid-malformed"),
    NOT_ACCEPTABLE("not-acceptable"),
    NOT_ALLOWED("not-allowed"),
    NOT_AUTHORIZED("not-authorized"),
    PAYMENT_REQUIRED("payment-required"),
    POLICY_VIOLATION("policy-violation"),
    RECIPIENT_UNAVAILABLE("recipient-unavailable"),
    REDIRECT("redirect"),
    REGISTRATION_REQUEST("registration-required"),
    REMOTE_SERVER_NOT_FOUND("remote-server-not-found"),
    REMOTE_SERVER_TIMEOUT("remote-server-timeout"),
    RESOURCE_CONSTRAINT("resource-constraint"),
    SERVICE_UNAVAILABLE("service-unavailable"),
    SUBSCRIPTION_REQUIRED("subscription-required"),
    UNDEFINED_CONDITION("undefined-condition"),
    UNEXPECTED_RESULT("unexpected-request"),
    UNKNOWN("unknown");

    private final String value;

    StreamErrorType(String value) {

        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
