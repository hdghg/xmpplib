package com.github.hdghg.xmpplib.model.auth;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "success", namespace = "urn:ietf:params:xml:ns:xmpp-sasl")
@XmlAccessorType(XmlAccessType.FIELD)
public class Success {
    @XmlValue
    private String value;
}
