package com.github.hdghg.xmpplib.api;

import javax.security.auth.callback.*;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.RealmChoiceCallback;
import java.io.IOException;

public class DigestHandler implements CallbackHandler {

    private final String authenticationId;
    private final String password;

    public DigestHandler(String authenticationId, String password) {
        this.authenticationId = authenticationId;
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof NameCallback) {
                NameCallback ncb = (NameCallback)callbacks[i];
                ncb.setName(authenticationId);
            } else if(callbacks[i] instanceof PasswordCallback) {
                PasswordCallback pcb = (PasswordCallback)callbacks[i];
                pcb.setPassword(password.toCharArray());
            } else if(callbacks[i] instanceof RealmCallback) {
                RealmCallback rcb = (RealmCallback)callbacks[i];
                String text = rcb.getDefaultText();
                rcb.setText(text);
            } else if(callbacks[i] instanceof RealmChoiceCallback){
                throw new UnsupportedCallbackException(callbacks[i]);
            } else {
                throw new UnsupportedCallbackException(callbacks[i]);
            }
        }
    }
}
