package com.AlekseyZakharov.Cloud_Common;

public class AuthorizationRequest extends Request {
    private String login;
    public AuthorizationRequest(User user) {
        login = user.getLogin();
    }

    public String getLogin() {
        return login;
    }
}
