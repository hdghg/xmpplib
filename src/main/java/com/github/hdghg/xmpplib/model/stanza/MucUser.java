package com.github.hdghg.xmpplib.model.stanza;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class MucUser {
    @XmlElement(name = "item", namespace = "http://jabber.org/protocol/muc#user")
    private Item item;

    public Item getItem() {
        return item;
    }

    public MucUser setItem(Item item) {
        this.item = item;
        return this;
    }
}
