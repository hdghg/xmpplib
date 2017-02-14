package com.github.hdghg.xmpplib.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "stream", namespace = "http://etherx.jabber.org/streams")
@XmlAccessorType(XmlAccessType.FIELD)
public class Stream {
    @XmlElement(name = "features", namespace = "http://etherx.jabber.org/streams")
    private Features features;
    @XmlAttribute
    private String id;
    @XmlAttribute
    private String from;
    @XmlAttribute
    private String version;
    @XmlAttribute(name = "lang", namespace = javax.xml.XMLConstants.XML_NS_URI)
    private String lang;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Features getFeatures() {
        return features;
    }

    public void setFeatures(Features features) {
        this.features = features;
    }
}
