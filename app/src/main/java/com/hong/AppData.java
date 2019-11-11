package com.hong;

import android.support.annotation.Nullable;

import com.hong.http.model.UMenu;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.hong.dao.AuthUser;
import com.hong.mvp.model.User;
import java.util.ArrayList;
import java.util.Locale;

public enum AppData {
    INSTANCE;

    public static boolean isLogin;
    public static int isUpload;
    @AutoAccess(dataName = "appData_authUser")
    AuthUser authUser;
    @AutoAccess(dataName = "appData_loggedUser")
    User loggedUser;
    @AutoAccess(dataName = "appData_systemDefaultLocal")
    Locale systemDefaultLocal;

    public static ArrayList<UMenu> menus = new ArrayList<>();


    public User getLoggedUser() {
        return this.loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public AuthUser getAuthUser() {
        return this.authUser;
    }

    public void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }

    @Nullable
    public String getAccessToken() {
        AuthUser authUser = this.authUser;
        return authUser == null ? null : authUser.getAccessToken();
    }

    public Locale getSystemDefaultLocal() {
        if (this.systemDefaultLocal == null) {
            this.systemDefaultLocal = Locale.getDefault();
        }
        return this.systemDefaultLocal;
    }
}
