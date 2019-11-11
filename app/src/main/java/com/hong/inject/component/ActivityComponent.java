package com.hong.inject.component;

import com.hong.MainActivity;
import com.hong.inject.ActivityScope;
import com.hong.inject.module.ActivityModule;
import com.hong.ui.activity.IssuesActivity;
import com.hong.ui.activity.LoginActivity;
import com.hong.ui.activity.RepositoryActivity;
import com.hong.ui.activity.SettingsActivity;
import com.hong.ui.activity.SplashActivity;
import com.hong.ui.activity.TrendingActivity;
import com.hong.ui.activity.WebActivity;
import dagger.Component;

@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity mainActivity);


    void inject(IssuesActivity issuesActivity);

    void inject(LoginActivity loginActivity);

    void inject(RepositoryActivity repositoryActivity);

    void inject(SettingsActivity settingsActivity);

    void inject(SplashActivity splashActivity);

    void inject(TrendingActivity trendingActivity);

    void inject(WebActivity webActivity);
}
