package com.github.hdghg.xmpplib.model.auth;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "failure", namespace = "urn:ietf:params:xml:ns:xmpp-sasl")
@XmlAccessorType(XmlAccessType.FIELD)
public class Failure {
    @XmlElement(name = "bad-nonce", namespace = "urn:ietf:params:xml:ns:xmpp-sasl")
    private String badNonce;
    @XmlElement(name = "not-authorized", namespace = "urn:ietf:params:xml:ns:xmpp-sasl")
    private String notAuthorized;

    public String getNotAuthorized() {
        return notAuthorized;
    }

    public Failure setNotAuthorized(String notAuthorized) {
        this.notAuthorized = notAuthorized;
        return this;
    }

    public String getBadNonce() {
        return badNonce;
    }

    public Failure setBadNonce(String badNonce) {
        this.badNonce = badNonce;
        return this;
    }
}
