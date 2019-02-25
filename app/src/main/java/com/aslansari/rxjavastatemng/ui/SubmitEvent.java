package com.aslansari.rxjavastatemng.ui;

import io.reactivex.Observable;

public class SubmitEvent{
    public String username;

    public SubmitEvent(String username) {
        this.username = username;
    }
}
