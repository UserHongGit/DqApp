package com.hong.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hong.dao.BookMarkUser;
import com.hong.dao.LocalUser;
import com.hong.dao.TraceUser;
import java.util.Date;

public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @SerializedName("avatar_url")
    private String avatarUrl;
    private String bio;
    private String blog;
    private String company;
    @SerializedName("created_at")
    private Date createdAt;
    private String email;
    private int followers;
    private int following;
    @SerializedName("html_url")
    private String htmlUrl;
    private String id;
    private String location;
    private String login;
    private String name;
    private String oilfield;
    @SerializedName("public_gists")
    private int publicGists;
    @SerializedName("public_repos")
    private int publicRepos;
    private UserType type;
    @SerializedName("updated_at")
    private Date updatedAt;

    public enum UserType {
        User,
        Organization
    }

    public LocalUser toLocalUser() {
        LocalUser localUser = new LocalUser();
        localUser.setLogin(this.login);
        localUser.setName(this.name);
        localUser.setOilfield(this.oilfield);
        localUser.setAvatarUrl(this.avatarUrl);
        localUser.setFollowers(Integer.valueOf(this.followers));
        localUser.setFollowing(Integer.valueOf(this.following));
        return localUser;
    }

    public User() {
    }

    public static User generateFromLocalUser(LocalUser localUser) {
        User user = new User();
        user.setLogin(localUser.getLogin());
        user.setName(localUser.getName());
        user.setOilfield(localUser.getOilfield());
        user.setFollowers(localUser.getFollowers().intValue());
        user.setFollowing(localUser.getFollowing().intValue());
        user.setAvatarUrl(localUser.getAvatarUrl());
        return user;
    }

    public static User generateFromTrace(TraceUser trace) {
        User user = new User();
        user.setLogin(trace.getLogin());
        user.setName(trace.getName());
        user.setOilfield(trace.getOilfield());
        user.setFollowers(trace.getFollowers().intValue());
        user.setFollowing(trace.getFollowing().intValue());
        user.setAvatarUrl(trace.getAvatarUrl());
        return user;
    }

    public static User generateFromBookmark(BookMarkUser bookMark) {
        User user = new User();
        user.setLogin(bookMark.getLogin());
        user.setName(bookMark.getName());
        user.setFollowers(bookMark.getFollowers().intValue());
        user.setFollowing(bookMark.getFollowing().intValue());
        user.setAvatarUrl(bookMark.getAvatarUrl());
        return user;
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

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public UserType getType() {
        return this.type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getCompany() {
        return this.company;
    }

    public String getOilfield() {
        return this.oilfield;
    }

    public void setOilfield(String oilfield) {
        this.oilfield = oilfield;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBlog() {
        return this.blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getPublicRepos() {
        return this.publicRepos;
    }

    public void setPublicRepos(int publicRepos) {
        this.publicRepos = publicRepos;
    }

    public int getPublicGists() {
        return this.publicGists;
    }

    public void setPublicGists(int publicGists) {
        this.publicGists = publicGists;
    }

    public int getFollowers() {
        return this.followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return this.following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isUser() {
        return UserType.User.equals(this.type);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.login);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.oilfield);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.htmlUrl);
        UserType userType = this.type;
        dest.writeInt(userType == null ? -1 : userType.ordinal());
        dest.writeString(this.company);
        dest.writeString(this.blog);
        dest.writeString(this.location);
        dest.writeString(this.email);
        dest.writeString(this.bio);
        dest.writeInt(this.publicRepos);
        dest.writeInt(this.publicGists);
        dest.writeInt(this.followers);
        dest.writeInt(this.following);
        Date date = this.createdAt;
        long j = -1;
        dest.writeLong(date != null ? date.getTime() : -1);
        date = this.updatedAt;
        if (date != null) {
            j = date.getTime();
        }
        dest.writeLong(j);
    }

    protected User(Parcel in) {
        this.login = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.oilfield = in.readString();
        this.avatarUrl = in.readString();
        this.htmlUrl = in.readString();
        int tmpType = in.readInt();
        Date date = null;
        this.type = tmpType == -1 ? null : UserType.values()[tmpType];
        this.company = in.readString();
        this.blog = in.readString();
        this.location = in.readString();
        this.email = in.readString();
        this.bio = in.readString();
        this.publicRepos = in.readInt();
        this.publicGists = in.readInt();
        this.followers = in.readInt();
        this.following = in.readInt();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        if (tmpUpdatedAt != -1) {
            date = new Date(tmpUpdatedAt);
        }
        this.updatedAt = date;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof User)) {
            return super.equals(obj);
        }
        return ((User) obj).getLogin().equals(this.login);
    }
}
