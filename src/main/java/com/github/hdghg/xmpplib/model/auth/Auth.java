package com.github.hdghg.xmpplib.model.auth;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "auth", namespace = "urn:ietf:params:xml:ns:xmpp-sasl")
@XmlAccessorType(XmlAccessType.FIELD)
public class Auth {
    @XmlAttribute
    private String mechanism;
    @XmlValue
    private String value;

    public String getMechanism() {
        return mechanism;
    }

    public Auth setMechanism(String mechanism) {
        this.mechanism = mechanism;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Auth setValue(String value) {
        this.value = value;
        return this;
    }
}
