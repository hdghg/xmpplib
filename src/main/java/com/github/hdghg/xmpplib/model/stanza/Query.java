package com.github.hdghg.xmpplib.model.stanza;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Query {

    @XmlElement(namespace = "jabber:client")
    private String username;
    @XmlElement(namespace = "jabber:client")
    private String password;
    @XmlElement(namespace = "jabber:client")
    private String digest;
    @XmlElement(namespace = "jabber:client")
    private String resource;

    public String getUsername() {
        return username;
    }

    public Query setUsername(String value) {
        this.username = value;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Query setPassword(String value) {
        this.password = value;
        return this;
    }

    public String getDigest() {
        return digest;
    }

    public Query setDigest(String value) {
        this.digest = value;
        return this;
    }

    public String getResource() {
        return resource;
    }

    public Query setResource(String value) {
        this.resource = value;
        return this;
    }

}
