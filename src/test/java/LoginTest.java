import com.github.hdghg.xmpplib.api.XmppConnection;
import com.github.hdghg.xmpplib.model.stanza.Iq;
import com.github.hdghg.xmpplib.model.stanza.Presence;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;

public class LoginTest {

    @Test
    public void testDigestMd5Auth() throws IOException, JAXBException {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("jabber.ru.properties");
        Properties properties = new Properties();
        properties.load(resourceAsStream);
        XmppConnection connection = new XmppConnection(properties.getProperty("login"));
        connection.connect();
        connection.digestMd5Auth(properties.getProperty("password").getBytes());
        Iq iq = connection.setResource(RandomStringUtils.randomAlphabetic(8));
        System.out.println(iq);
        connection.getChannel().configureBlocking(false);

        Presence room = connection.joinRoom(properties.getProperty("room"), RandomStringUtils.randomAlphabetic(8), 30000);
        System.out.println(room);
        assertNotNull("Cannot join room", room);
        connection.disconnect();
    }
}
