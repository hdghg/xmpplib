package com.github.hdghg.xmpplib.model;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "features")
@XmlAccessorType(XmlAccessType.FIELD)
public class Features {
    @XmlElementWrapper(name = "mechanisms", namespace = "urn:ietf:params:xml:ns:xmpp-sasl")
    @XmlElement(name = "mechanism", namespace = "urn:ietf:params:xml:ns:xmpp-sasl")
    private List<String> mechanisms;
    @XmlElement(name = "c", namespace = "http://jabber.org/protocol/caps")
    private Caps caps;
    @XmlElement(name = "starttls", namespace = "urn:ietf:params:xml:ns:xmpp-tls")
    private StartTls starttls;

    public Caps getCaps() {
        return caps;
    }

    public void setCaps(Caps caps) {
        this.caps = caps;
    }

    public StartTls getStarttls() {
        return starttls;
    }

    public void setStarttls(StartTls starttls) {
        this.starttls = starttls;
    }

    public List<String> getMechanisms() {
        return mechanisms;
    }

    public void setMechanisms(List<String> mechanisms) {
        this.mechanisms = mechanisms;
    }
}
