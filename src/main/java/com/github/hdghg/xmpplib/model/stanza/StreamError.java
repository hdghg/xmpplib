package com.github.hdghg.xmpplib.model.stanza;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(StreamErrorAdapter.class)
public class StreamError {
    private StreamErrorType type;

    private String tagName;
    private String tagValue;

    public StreamErrorType getType() {
        return type;
    }

    public void setType(StreamErrorType type) {
        this.type = type;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type", type)
                .append("tagName", tagName)
                .append("tagValue", tagValue)
                .toString();
    }
}
