package com.github.hdghg.xmpplib.model.stanza;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Update {
    @XmlElement(namespace = "vcard-temp:x:update")
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public Update setPhoto(String photo) {
        this.photo = photo;
        return this;
    }
}
