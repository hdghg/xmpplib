package com.github.hdghg.xmpplib.model.stanza;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Error {
    @XmlAttribute
    private Long code;
    @XmlAttribute
    private String type;
    @XmlAnyElement()
    private StreamError streamError;
    @XmlElement(type=String.class, namespace = "urn:ietf:params:xml:ns:xmpp-stanzas", name = "text")
    private String text;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StreamError getStreamError() {
        return streamError;
    }

    public void setStreamError(StreamError streamError) {
        this.streamError = streamError;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return String.format("Error{code='%s', type='%s', streamError=%s, text='%s'}",
                code, type, streamError, text);
    }
}
