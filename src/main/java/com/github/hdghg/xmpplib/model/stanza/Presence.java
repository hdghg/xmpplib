package com.github.hdghg.xmpplib.model.stanza;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "presence")
@XmlAccessorType(XmlAccessType.FIELD)
public class Presence extends Stanza {
    @XmlAttribute(name = "lang", namespace = javax.xml.XMLConstants.XML_NS_URI)
    private String lang;
    @XmlAttribute
    private String from;
    @XmlElement
    private Long priority;
    @XmlElement(namespace = "http://jabber.org/protocol/muc", name = "x")
    private Muc muc;
    @XmlElement(namespace = "vcard-temp:x:update", name = "x")
    private Update update;
    @XmlElement(namespace = "http://jabber.org/protocol/muc#user", name = "x")
    private MucUser mucUser;
    @XmlAttribute
    private String type;
    @XmlElement
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getType() {
        return type;
    }

    public Presence setType(String type) {
        this.type = type;
        return this;
    }

    public Update getUpdate() {
        return update;
    }

    public Presence setUpdate(Update update) {
        this.update = update;
        return this;
    }

    public MucUser getMucUser() {
        return mucUser;
    }

    public Presence setMucUser(MucUser mucUser) {
        this.mucUser = mucUser;
        return this;
    }

    public Muc getMuc() {
        return muc;
    }

    public Presence setMuc(Muc muc) {
        this.muc = muc;
        return this;
    }

    public String getLang() {
        return lang;
    }

    public Presence setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public Presence setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return super.getTo();
    }

    public Presence setTo(String to) {
        super.setTo(to);
        return this;
    }

    public Long getPriority() {
        return priority;
    }

    public Presence setPriority(Long priority) {
        this.priority = priority;
        return this;
    }
}
