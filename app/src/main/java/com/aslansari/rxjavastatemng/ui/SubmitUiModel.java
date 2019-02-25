package com.aslansari.rxjavastatemng.ui;

public final class SubmitUiModel {
    public boolean inProgress;
    public boolean success;
    public boolean error;
    public String errorMessage;

    private SubmitUiModel(boolean inProgress, boolean success, String errorMessage) {
        this.inProgress = inProgress;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public static SubmitUiModel idle(){
        return new SubmitUiModel(false,false,"");
    }

    public static SubmitUiModel inProgress(){
        return new SubmitUiModel(true,false,"");
    }
    public static SubmitUiModel success(String successMessage){
        return new SubmitUiModel(false,true,successMessage);
    }
    public static SubmitUiModel failure(String errorMessage){
        return new SubmitUiModel(false,false,errorMessage);
    }
}
