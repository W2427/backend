package com.ose.util;

import jakarta.mail.*;

public class MailAuthenticator extends Authenticator {
    String userName = null;
    String password = null;

    public MailAuthenticator() {
    }

    public MailAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}
