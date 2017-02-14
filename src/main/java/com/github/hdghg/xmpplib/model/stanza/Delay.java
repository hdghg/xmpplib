package com.github.hdghg.xmpplib.model.stanza;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Delay {
    @XmlAttribute
    private String from;
    @XmlAttribute
    private String stamp;

    public String getFrom() {
        return from;
    }

    public Delay setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getStamp() {
        return stamp;
    }

    public Delay setStamp(String stamp) {
        this.stamp = stamp;
        return this;
    }
}
