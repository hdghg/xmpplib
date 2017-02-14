package com.github.hdghg.xmpplib.model.stanza;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Item {
    @XmlAttribute(name = "role")
    private String role;
    @XmlAttribute(name = "affiliation")
    private String affiliation;
    @XmlAttribute(name = "jid")
    private String jid;

    public String getJid() {
        return jid;
    }

    public Item setJid(String jid) {
        this.jid = jid;
        return this;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public Item setAffiliation(String affiliation) {
        this.affiliation = affiliation;
        return this;
    }

    public String getRole() {
        return role;
    }

    public Item setRole(String role) {
        this.role = role;
        return this;
    }
}
