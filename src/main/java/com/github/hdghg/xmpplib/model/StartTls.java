package com.github.hdghg.xmpplib.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "starttls", namespace = "urn:ietf:params:xml:ns:xmpp-tls")
@XmlAccessorType(XmlAccessType.FIELD)
public class StartTls {
    @XmlElement
    private Boolean required;

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }
}
