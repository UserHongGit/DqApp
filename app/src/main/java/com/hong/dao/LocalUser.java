package com.hong.dao;

public class LocalUser {
    private String avatarUrl;
    private Integer followers;
    private Integer following;
    private String login;
    private String name;
    private String oilfield;

    public LocalUser(String login) {
        this.login = login;
    }

    public LocalUser(String login, String name, String avatarUrl, Integer followers, Integer following, String oilfield) {
        this.login = login;
        this.name = name;
        this.oilfield = oilfield;
        this.avatarUrl = avatarUrl;
        this.followers = followers;
        this.following = following;
    }

    public LocalUser() {
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOilfield() {
        return this.oilfield;
    }

    public void setOilfield(String oilfield) {
        this.oilfield = oilfield;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getFollowers() {
        return this.followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowing() {
        return this.following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }
}
