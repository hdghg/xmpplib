package com.github.hdghg.xmpplib.model.auth;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "challenge", namespace = "urn:ietf:params:xml:ns:xmpp-sasl")
@XmlAccessorType(XmlAccessType.FIELD)
public class Challenge {
    @XmlValue
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
