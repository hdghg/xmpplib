package com.github.hdghg.xmpplib.api;

import com.github.hdghg.xmpplib.model.Proceed;
import com.github.hdghg.xmpplib.model.Stream;
import com.github.hdghg.xmpplib.model.auth.*;
import com.github.hdghg.xmpplib.model.stanza.Stanza;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class XmppUtils {
    public static byte[] openStream(String host) {
        String hello = String.format("<?xml version=\"1.0\"?>" +
                "<stream:stream xmlns:stream=\"http://etherx.jabber.org/streams\" version=\"1.0\" " +
                "xmlns=\"jabber:client\" to=\"%s\" xml:lang=\"en\" " +
                "xmlns:xml=\"http://www.w3.org/XML/1998/namespace\">", host);
        return hello.getBytes();
    }

    private static JAXBContext jaxbContext = null;
    private static Marshaller marshaller;
    private static Unmarshaller unmarshaller;

    private static JAXBContext getJaxbContext() throws JAXBException {
        if (null == jaxbContext) {
            jaxbContext = JAXBContext.newInstance(Stanza.class, Stream.class, Auth.class,
                    Challenge.class, Response.class, Success.class, Failure.class, Proceed.class);
        }
        return jaxbContext;
    }

    private static Marshaller getMarshaller() throws JAXBException {
        if (null == marshaller) {
            marshaller = getJaxbContext().createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        }
        return marshaller;
    }

    private static Unmarshaller getUnmarshaller() throws JAXBException {
        if (null == unmarshaller) {
            unmarshaller = getJaxbContext().createUnmarshaller();
        }
        return unmarshaller;
    }

    public synchronized static byte[] marshal(Object s) throws JAXBException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        getMarshaller().marshal(s, result);
        return result.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public synchronized static <T> T unmarshal(byte[] array, int offset, int length)
            throws JAXBException {
        ByteArrayInputStream bais = new ByteArrayInputStream(array, offset, length);
        return (T) getUnmarshaller().unmarshal(bais);
    }

    public synchronized static <T> T unmarshal(byte[] array) throws JAXBException {
        return unmarshal(array, 0, array.length);
    }

    public static String parseJid(String jidWithResource) {
        String[] split = jidWithResource.split("/", 2);
        return split[0];
    }

    public static String parseResource(String jidWithResource) {
        String[] split = jidWithResource.split("/", 2);
        return split[1];
    }
}
