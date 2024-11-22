package project.group6.eams.utils;

import project.group6.eams.users.User;
public class AppInfo {
    private static AppInfo appInfo;
    private User currentUser;
    private AppInfo(){
        currentUser = null;
    }

    public static AppInfo getInstance(){
        if (appInfo == null){
            appInfo = new AppInfo();
        }
        return appInfo;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public void setCurrentUser(User user){
        this.currentUser = user;

    }
}
