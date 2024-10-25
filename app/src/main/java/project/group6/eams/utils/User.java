package project.group6.eams.utils;

public abstract class User {
    private boolean loginStatus;
    private String email;
    private String password;

    public User(String email, String password){
        this.email = email;
        this.password = password;
        this.loginStatus = false;
    }

    //Getters
    public boolean isLoggedIn() {return loginStatus;}
    public String getEmail() {return email;}
    public String getPassword(){return password;}

    //Setters

    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}

    public void login(){
        this.loginStatus = true;
    }
    public void logout(){
        this.loginStatus = false;
    }

}
