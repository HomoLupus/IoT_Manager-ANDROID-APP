package com.example.iotmanager01.login;

public interface LoginView {
    String getLogin();
    String getPassword();
    void loggingInView();
    void loggedInView();
    void errorView(String message);
    void openMainPageView();
    void openRegisterPageView();
}
