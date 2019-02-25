package com.aslansari.rxjavastatemng.network;

import com.aslansari.rxjavastatemng.network.models.User;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubService {

    public final static String BASE_URL = "https://api.github.com";

    @GET("/users/{username}")
    Observable<User> getUser(
            @Path("username") String username);
}
