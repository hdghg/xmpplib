package com.github.hdghg.xmpplib.model.stanza;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "iq")
@XmlAccessorType(XmlAccessType.FIELD)
public class Iq extends Stanza {
    @XmlAttribute
    private String type;
    @XmlElement(namespace = "jabber:iq:auth")
    private Query query;
    @XmlElement
    private Error error;
    @XmlElement(namespace = "urn:ietf:params:xml:ns:xmpp-bind")
    private Bind bind;

    public Bind getBind() {
        return bind;
    }

    public Iq setBind(Bind bind) {
        this.bind = bind;
        return this;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getType() {
        return type;
    }

    public Iq setType(String type) {
        this.type = type;
        return this;
    }

    public String getId() {
        return super.getId();
    }

    public Iq setId(String id) {
        super.setId(id);
        return this;
    }

    public String getTo() {
        return super.getTo();
    }

    public Iq setTo(String to) {
        super.setTo(to);
        return this;
    }

    public Query getQuery() {
        return query;
    }

    public Iq setQuery(Query query) {
        this.query = query;
        return this;
    }
}
