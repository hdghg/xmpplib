package com.github.hdghg.xmpplib.model.stanza;

import javax.xml.bind.annotation.*;


@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class Message extends Stanza {
    @XmlAttribute
    private String from;
    @XmlAttribute
    private String type;
    @XmlElement
    private String subject;
    @XmlAttribute(name = "lang", namespace = javax.xml.XMLConstants.XML_NS_URI)
    private String lang;
    @XmlElement
    private String body;
    @XmlElement(namespace = "urn:xmpp:delay")
    private Delay delay;

    public Message() {
    }

    public Message(String stanzaId) {
        super(stanzaId);
    }

    public Message setTo(String to) {
        super.setTo(to);
        return this;
    }

    public String getTo() {
        return super.getTo();
    }

    public Message setId(String stanzaId) {
        super.setId(stanzaId);
        return this;
    }

    public String getId() {
        return super.getId();
    }

    public String getFrom() {
        return from;
    }

    public Message setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getType() {
        return type;
    }

    public Message setType(String type) {
        this.type = type;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Message setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getLang() {
        return lang;
    }

    public Message setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Message setBody(String body) {
        this.body = body;
        return this;
    }

    public Delay getDelay() {
        return delay;
    }

    public Message setDelay(Delay delay) {
        this.delay = delay;
        return this;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", type='" + type + '\'' +
                ", subject='" + subject + '\'' +
                ", lang='" + lang + '\'' +
                ", body='" + body + '\'' +
                ", delay=" + delay +
                '}';
    }
}
