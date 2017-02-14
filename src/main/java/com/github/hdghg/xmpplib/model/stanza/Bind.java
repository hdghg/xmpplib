package com.github.hdghg.xmpplib.model.stanza;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Bind {
    @XmlElement(namespace = "urn:ietf:params:xml:ns:xmpp-bind")
    private String jid;

    public String getJid() {
        return jid;
    }

    public Bind setJid(String jid) {
        this.jid = jid;
        return this;
    }
}
