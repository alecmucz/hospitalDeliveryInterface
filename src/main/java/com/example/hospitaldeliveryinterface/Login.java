package com.example.hospitaldeliveryinterface;

public class Login {

    private String username;
    private String password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Login(boolean login) {
        Login = login;
    }

    private boolean Login = false;

    public boolean isLogin() {
        return Login;
    }

    public void setLogin(boolean login) {
        Login = login;
    }
}
