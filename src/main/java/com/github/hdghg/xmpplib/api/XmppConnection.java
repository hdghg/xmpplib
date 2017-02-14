package com.github.hdghg.xmpplib.api;

import com.github.hdghg.xmpplib.model.Features;
import com.github.hdghg.xmpplib.model.Proceed;
import com.github.hdghg.xmpplib.model.StartTls;
import com.github.hdghg.xmpplib.model.Stream;
import com.github.hdghg.xmpplib.model.auth.*;
import com.github.hdghg.xmpplib.model.stanza.*;
import org.apache.commons.codec.binary.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.net.ssl.*;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class XmppConnection {
    private static final Logger log = LoggerFactory.getLogger(XmppConnection.class);

    private String streamId;
    private String user;
    private String host;
    private SocketChannel openChannel;
    private String unparsed = "";
    private List<Stanza> unreported = new ArrayList<>();
    private boolean connected;
    private Features initialFeatures;
    private Features authorizedFeatures;


    public XmppConnection(String jid) throws IOException, JAXBException {
        String[] split = jid.split("@");
        this.user = split[0];
        this.host = split[1];
    }

    private Stream openStream() throws JAXBException, IOException {
        openChannel.write(ByteBuffer.wrap(XmppUtils.openStream(host)));
        String result = "";
        while (!result.contains("</stream:features>")) {
            ByteBuffer response = ByteBuffer.allocate(2048);
            int read = openChannel.read(response);
            result += new String(response.array(), 0, read);
        }
        result += "</stream:stream>";
        Stream stream = XmppUtils.unmarshal(result.getBytes());
        return stream;
    }

    public void connect() throws IOException, JAXBException {
        openChannel = SocketChannel.open();
        try {
            openChannel.connect(new InetSocketAddress(host, 5222));
            Stream stream = openStream();
            streamId = stream.getId();
            initialFeatures = stream.getFeatures();
            connected = true;
        } catch (Exception ex) {
            try {
                openChannel.close();
            } catch (IOException s) {
                s.addSuppressed(ex);
            }
            throw ex;
        }
    }

    private ByteBuffer myAppData;
    private ByteBuffer myNetData;
    private ByteBuffer peerAppData;
    private ByteBuffer peerNetData;

    public void startTls(TrustManager[] trustManagers) throws JAXBException, IOException, KeyStoreException,
            CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        if (null == initialFeatures.getStarttls()) {
            throw new IllegalStateException("Server does not support tls");
        }
        byte[] marshal = XmppUtils.marshal(new StartTls());
        openChannel.write(ByteBuffer.wrap(marshal));
        ByteBuffer proceedBuffer = ByteBuffer.allocate(4096);
        int proceedRead = openChannel.read(proceedBuffer);
        Object proceed = XmppUtils.unmarshal(proceedBuffer.array(), 0, proceedRead);
        if (!(proceed instanceof Proceed)) {
            throw new IllegalArgumentException(String.format("Server returned wrong responce for starttls: %s", proceed));
        }

        KeyStore jks = KeyStore.getInstance("JKS");
        jks.load(null, null);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(jks, null);
        SSLContext tls = SSLContext.getInstance("TLS");
        tls.init(kmf.getKeyManagers(), trustManagers, new SecureRandom());
        SSLEngine sslEngine = tls.createSSLEngine();
        sslEngine.setUseClientMode(true);
        SSLSession session = sslEngine.getSession();

        myAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        myNetData = ByteBuffer.allocate(session.getPacketBufferSize());
        peerAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        peerNetData = ByteBuffer.allocate(session.getPacketBufferSize());

        openChannel.configureBlocking(false);
        doHandshake(openChannel, sslEngine, myNetData, peerNetData);

        System.out.println("assad");
    }

    void doHandshake(SocketChannel socketChannel, SSLEngine engine,
                     ByteBuffer myNetData, ByteBuffer peerNetData) throws IOException {

        int appBufferSize = engine.getSession().getApplicationBufferSize();
        ByteBuffer myAppData = ByteBuffer.allocate(appBufferSize);
        ByteBuffer peerAppData = ByteBuffer.allocate(appBufferSize);
        engine.beginHandshake();
        SSLEngineResult.HandshakeStatus hs = engine.getHandshakeStatus();
        while (hs != SSLEngineResult.HandshakeStatus.FINISHED &&
                hs != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
            switch (hs) {
                case NEED_UNWRAP:
                    if (socketChannel.read(peerNetData) < 0) {
                        throw new IllegalStateException("Can't handshake as engine closed");
                    }
                    peerNetData.flip();
                    SSLEngineResult res = engine.unwrap(peerNetData, peerAppData);
                    peerNetData.compact();
                    hs = res.getHandshakeStatus();
                    switch (res.getStatus()) {
                        case OK:
                            break;
                    }
                    break;
                case NEED_WRAP:
                    myNetData.clear();
                    res = engine.wrap(myAppData, myNetData);
                    hs = res.getHandshakeStatus();
                    switch (res.getStatus()) {
                        case OK:
                            myNetData.flip();
                            while (myNetData.hasRemaining()) {
                                if (socketChannel.write(myNetData) < 0) {
                                    throw  new IllegalStateException("Almost did hs, but failed");
                                }
                            }
                            break;
                    }
                    break;
                case NEED_TASK:
                    Runnable task;
                    while ((task=engine.getDelegatedTask()) != null) {
                        task.run();
                    }
                    hs = engine.getHandshakeStatus();
                    break;
            }
        }
        // success
    }

    public <P> P plainAuth(byte[] password) throws JAXBException, IOException {
        byte[] secret = ArrayUtils.addAll((user + "\0").getBytes(), password);
        Auth auth = new Auth().setMechanism("PLAIN")
                .setValue(new Base64().encodeAsString(secret));
        openChannel.write(ByteBuffer.wrap(XmppUtils.marshal(auth)));
        ByteBuffer respBuffer = ByteBuffer.allocate(4096);
        int authLength = openChannel.read(respBuffer);
        Object result = XmppUtils.unmarshal(respBuffer.array(), 0, authLength);
        if (result instanceof Failure) {
            System.out.println(result);
            throw new RuntimeException();
        }
        return (P)result;
    }

    public Features digestMd5Auth(byte[] password) throws JAXBException, IOException {
        Auth auth = new Auth().setMechanism("DIGEST-MD5");
        openChannel.write(ByteBuffer.wrap(XmppUtils.marshal(auth)));
        ByteBuffer respBuffer = ByteBuffer.allocate(4096);
        int authLength = openChannel.read(respBuffer);
        Challenge result = XmppUtils.unmarshal(respBuffer.array(), 0, authLength);

        DigestHandler digestHandler = new DigestHandler(user, new String(password));
        SaslClient sc = Sasl.createSaslClient(new String[]{"DIGEST-MD5"}, null, "xmpp", host,
                Collections.<String, Object>emptyMap(), digestHandler);
        byte[] bytes = sc.evaluateChallenge(new Base64().decode(result.getValue()));

        Response response1 = new Response().setValue(new Base64().encodeAsString(bytes));
        byte[] marshal = XmppUtils.marshal(response1);
        openChannel.write(ByteBuffer.wrap(marshal));

        respBuffer.clear();
        int read = openChannel.read(respBuffer);
        Object unmarshal = XmppUtils.unmarshal(respBuffer.array(), 0, read);
        if (!(unmarshal instanceof Challenge)) {
            if (unmarshal instanceof Failure && null != ((Failure) unmarshal).getNotAuthorized()) {
                throw new IllegalArgumentException("Not authorized");
            }
            throw new IllegalArgumentException();
        }
        openChannel.write(ByteBuffer.wrap(XmppUtils.marshal(new Response())));
        respBuffer.clear();
        int readEnd = openChannel.read(respBuffer);
        Object end = XmppUtils.unmarshal(respBuffer.array(), 0, readEnd);
        if (!(end instanceof Success)) {
            System.out.println(end);
            throw new IllegalArgumentException();
        }
        Stream stream = openStream();
        streamId = stream.getId();
        authorizedFeatures = stream.getFeatures();
        return authorizedFeatures;
    }



    public Iq setResource(String resource) throws IOException, JAXBException {
        Iq iq = new Iq().setType("set").setBind(
                new Bind().setJid(user + "@" + host + "/" + resource));
        byte[] marshal = XmppUtils.marshal(iq);
        openChannel.write(ByteBuffer.wrap(marshal));
        ByteBuffer respBuffer = ByteBuffer.allocate(4096);
        int read = openChannel.read(respBuffer);
        return XmppUtils.unmarshal(respBuffer.array(), 0, read);
    }

    public Presence joinRoom(String room, String nick, long timeout) throws JAXBException, IOException {
        long endTime = System.currentTimeMillis() + timeout;
        String joinAs = StringUtils.joinWith("/", room, nick);
        Presence join = new Presence().setTo(joinAs).setMuc(new Muc());
        openChannel.write(ByteBuffer.wrap(XmppUtils.marshal(join)));
        Presence result = null;
        List<Stanza> stanzas = new ArrayList<>();
        while (null == result && System.currentTimeMillis() < endTime) {
            List<Stanza> nextStanzas = nextStanzas();
            if (null != nextStanzas) {
                stanzas.addAll(nextStanzas);
                for (Stanza nextStanza : nextStanzas) {
                    if (nextStanza instanceof Presence && ((Presence) nextStanza).getFrom().equals(joinAs)) {
                        result = ((Presence) nextStanza);
                        break;
                    }
                }
            }
        }
        unreported.addAll(stanzas);
        return result;
    }

    private static final Pattern OPEN_TAG_PATTERN = Pattern.compile("<([a-z:]+).*?>");

    public List<Stanza> nextStanzas() throws JAXBException {
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        int read;
        try {
            while (0 < (read = openChannel.read(buffer))) {
                unparsed += new String(buffer.array(), 0, read);
                buffer.clear();
            }
        } catch (IOException e) {
            log.warn("Socket error", e);
            connected = false;
        }
        List<Stanza> result = new ArrayList<>();
        boolean drained = false;
        while (!drained) {
            Matcher openTagMatcher = OPEN_TAG_PATTERN.matcher(unparsed);
            if (openTagMatcher.find()) {
                String tag = openTagMatcher.group(1);
                Pattern compile = Pattern.compile(String.format("<%1$s.*?</%1$s>", tag),
                        Pattern.DOTALL);
                Matcher stanzaMatcher = compile.matcher(unparsed);
                if (!stanzaMatcher.find()) {
                    drained = true;
                } else {
                    unparsed = unparsed.substring(stanzaMatcher.end());
                    String obj = stanzaMatcher.group();
                    try {
                        result.add(XmppUtils.<Stanza>unmarshal(obj.getBytes()));
                    } catch (JAXBException e) {
                        log.warn("Cannot unmarshal string '{}'", obj);
                        throw e;
                    }
                }
            } else {
                drained = true;
            }
        }
        if (unreported.isEmpty()) {
            return result;
        } else {
            ArrayList<Stanza> allResult = new ArrayList<>();
            allResult.addAll(unreported);
            unreported.clear();
            allResult.addAll(result);
            return allResult;
        }
    }

    public void sendMucMessage(String room, String body) throws JAXBException, IOException {
        Message groupchat = new Message().setType("groupchat").setTo(room).setBody(body);
        openChannel.write(ByteBuffer.wrap(XmppUtils.marshal(groupchat)));
    }

    public void sendMucMessage(Message message) throws JAXBException, IOException {
        message.setType("groupchat");
        openChannel.write(ByteBuffer.wrap(XmppUtils.marshal(message)));
    }

    public void leaveMuc(String room) throws JAXBException, IOException {
        Presence unavailable = new Presence().setTo(room).setType("unavailable");
        openChannel.write(ByteBuffer.wrap(XmppUtils.marshal(unavailable)));
    }

    public void goUnavailable() throws JAXBException, IOException {
        openChannel.write(ByteBuffer.wrap(XmppUtils.marshal(new Presence().setType("unavailable"))));
    }

    public void disconnect() {
        if (null != openChannel) {
            try {
                openChannel.close();
            } catch (IOException ignore) {
            }
        }
        connected = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public void scramSha1Auth(byte[] password) throws IOException, NoSuchAlgorithmException, JAXBException, InvalidKeySpecException {
        // TODO: Implement mechanism
        SecureRandom sha1PRNG = SecureRandom.getInstance("SHA1PRNG");
        byte[] clientNonceArray = sha1PRNG.generateSeed(16);
        String clientNonce = Hex.encodeHexString(clientNonceArray);
        String initialMessage = "n=" + user + ",r=" + clientNonce;
        Base64 base64 = new Base64();
        String initial64 = base64.encodeToString(("n,," + initialMessage).getBytes());
        Auth auth = new Auth().setMechanism("SCRAM-SHA-1").setValue(initial64);
        byte[] marshal = XmppUtils.marshal(auth);
        openChannel.write(ByteBuffer.wrap(marshal));
        ByteBuffer resp = ByteBuffer.allocate(4096);
        int read = openChannel.read(resp);
        Challenge unmarshal = XmppUtils.unmarshal(resp.array(), 0, read);
        String challengeText = new String(base64.decode(unmarshal.getValue()));
        Map<String, String> challengeMap = new HashMap<>();
        for (String element : challengeText.split(",")) {
            String[] split = element.split("=", 2);
             challengeMap.put(split[0], split[1]);
        }
        String serverNonce = challengeMap.get("r");
        if (!serverNonce.startsWith(clientNonce)) {
            throw new RuntimeException("Server nonce is incorrect");
        }
        byte[] salt = base64.decode(challengeMap.get("s"));
        System.out.println(Hex.encodeHexString(salt));
        long iterations = Integer.parseInt(challengeMap.get("i"));
        String clientFinalMessageBare = "c=biws,r=" + serverNonce;
        System.out.println(clientFinalMessageBare);
        PBEKeySpec spec = new PBEKeySpec(new String(password).toCharArray(), salt,
                (int) iterations, 20 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] saltedPassword = skf.generateSecret(spec).getEncoded();
        System.out.println(Hex.encodeHexString(saltedPassword));
        byte[] clientKey = HmacUtils.hmacSha1(saltedPassword, "Client Key".getBytes());
        System.out.println(Hex.encodeHexString(clientKey));
        // error above ^^^
        byte[] storedKey = DigestUtils.sha1(clientKey);
        System.out.println(Hex.encodeHexString(storedKey));
        String authMessage = initialMessage + "," + challengeText + "," + clientFinalMessageBare;
        System.out.println(authMessage);
        byte[] clientSignature = HmacUtils.hmacSha1(storedKey, authMessage.getBytes());
        System.out.println(Hex.encodeHexString(clientSignature));
        int len = Math.max(clientKey.length, clientSignature.length);

        byte[] clientProof = new byte[len];
        for (int i = 0; i < len; i++) {
            clientProof[i] = (byte) (clientKey[i] ^ clientSignature[i]);
        }
        System.out.println(Hex.encodeHexString(clientProof));
        byte[] serverKey = HmacUtils.hmacSha1(saltedPassword, "Server Key".getBytes());
        byte[] serverSignature = HmacUtils.hmacSha1(serverKey, authMessage.getBytes());
        String clientFinalMessage = clientFinalMessageBare + ",p=" + base64.encodeToString(clientProof);
        String respToChallenge = base64.encodeToString(clientFinalMessage.getBytes());
        openChannel.write(ByteBuffer.wrap(XmppUtils.marshal(
                new Response().setValue(respToChallenge))));
        ByteBuffer successBuffer = ByteBuffer.allocate(4096);
        int readSuccess = openChannel.read(successBuffer);
        Object unmarshal1 = XmppUtils.unmarshal(successBuffer.array(), 0, readSuccess);
        System.out.println(unmarshal1);
    }

    public SelectableChannel getChannel() {
        return openChannel;
    }
}
