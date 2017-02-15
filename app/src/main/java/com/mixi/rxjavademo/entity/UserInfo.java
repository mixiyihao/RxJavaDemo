package com.mixi.rxjavademo.entity;

/**
 * Created by mixi on 2017/2/14.
 */

public class UserInfo {
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
