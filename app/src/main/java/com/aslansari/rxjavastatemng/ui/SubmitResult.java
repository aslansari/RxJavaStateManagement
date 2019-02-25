package com.aslansari.rxjavastatemng.ui;

import com.aslansari.rxjavastatemng.network.models.User;

public final class SubmitResult {
    public boolean inProgress;
    public boolean success;
    public boolean error;
    public String errorMessage;
    public User user;

    public static final int IN_FLIGHT = 1;
    public static final int SUCCESS = 2;

    private SubmitResult(boolean inProgress, boolean success, boolean error,String errorMessage) {
        this.inProgress = inProgress;
        this.success = success;
        this.error = error;
        this.errorMessage = errorMessage;
    }

    private SubmitResult(boolean inProgress, boolean success, boolean error,String errorMessage,User user) {
        this.inProgress = inProgress;
        this.success = success;
        this.error = error;
        this.errorMessage = errorMessage;
        this.user = user;
    }

    public static SubmitResult idle(){
        return new SubmitResult(false,false,false,"");
    }

    public static SubmitResult inProgress(){
        return new SubmitResult(true,false, false,"");
    }
    public static SubmitResult success(User user){
        return new SubmitResult(false,true, false,"",user);
    }
    public static SubmitResult failure(String errorMessage){
        return new SubmitResult(false,false, true, errorMessage);
    }

}
