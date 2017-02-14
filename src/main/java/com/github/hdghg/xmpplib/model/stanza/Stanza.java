package com.github.hdghg.xmpplib.model.stanza;

import org.apache.commons.lang3.RandomUtils;

import javax.xml.bind.annotation.*;
import java.util.concurrent.atomic.AtomicLong;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Iq.class, Message.class, Presence.class})
public class Stanza {
    @XmlAttribute
    private String id;
    @XmlAttribute
    private String to;

    private static transient AtomicLong initial = new AtomicLong(RandomUtils.nextLong());

    public Stanza() {
    }

    public Stanza(String stanzaId) {
        this.id = (null == stanzaId ? String.valueOf(initial.incrementAndGet()) : stanzaId);
    }

    public String getId() {
        return id;
    }

    public Stanza setId(String id) {
        this.id = id;
        return this;
    }

    public String getTo() {
        return to;
    }

    public Stanza setTo(String to) {
        this.to = to;
        return this;
    }
}
