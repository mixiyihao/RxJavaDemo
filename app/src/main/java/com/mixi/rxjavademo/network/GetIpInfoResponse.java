package com.mixi.rxjavademo.network;

import com.mixi.rxjavademo.entity.UserInfo;

/**
 * Created by mixi on 2017/2/14.
 */

public class GetIpInfoResponse {
    public int id;
    public String username;
    public String country;
    public String city;

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
