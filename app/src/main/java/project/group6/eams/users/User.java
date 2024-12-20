package project.group6.eams.users;

public abstract class User{
    private boolean loginStatus;
    private String email;
    private String password;
    protected String userType;

    public User () {
        loginStatus = false;
    }
    /**
     * @param email
     * @param password
     */
    public User (String email, String password) {
        this.email = email;
        this.password = password;
        this.loginStatus = false;
    }

    //Getters
    public boolean getLoginStatus () {return loginStatus;}
    public String getEmail () {return email;}
    public String getPassword () {return password;}
    public String getUserType () {return userType;}

    //Setters
    public void setEmail (String email) {this.email = email;}
    public void setPassword (String password) {this.password = password;}
    public void setLoginStatus (boolean loginStatus) {this.loginStatus = loginStatus;}
}
