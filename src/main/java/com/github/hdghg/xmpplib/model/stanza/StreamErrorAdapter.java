package com.github.hdghg.xmpplib.model.stanza;


import org.w3c.dom.Element;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StreamErrorAdapter extends XmlAdapter<Element, StreamError> {
    @Override
    public StreamError unmarshal(Element v) throws Exception {
        StreamError result = new StreamError();
        String tagName = v.getTagName();
        result.setTagName(tagName);
        String nodeValue = v.getNodeValue();
        result.setTagValue(nodeValue);
        result.setType(StreamErrorType.UNKNOWN);
        for (StreamErrorType streamErrorType : StreamErrorType.values()) {
            if (streamErrorType.getValue().equals(tagName)) {
                result.setType(streamErrorType);
                break;
            }
        }
        return result;
    }

    @Override
    public Element marshal(StreamError v) throws Exception {
        return null;
    }
}
