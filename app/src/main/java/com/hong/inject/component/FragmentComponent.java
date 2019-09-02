package com.hong.inject.component;

import com.hong.inject.FragmentScope;
import com.hong.inject.module.FragmentModule;
import com.hong.ui.fragment.ActivityFragment;
import com.hong.ui.fragment.IssuesFragment;
import com.hong.ui.fragment.NotificationsFragment;
import com.hong.ui.fragment.RepositoriesFragment;
import com.hong.ui.fragment.ViewerFragment;
import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {FragmentModule.class})
public interface FragmentComponent {
    void inject(ActivityFragment activityFragment);

    void inject(IssuesFragment issuesFragment);

    void inject(NotificationsFragment notificationsFragment);

    void inject(RepositoriesFragment repositoriesFragment);

    void inject(ViewerFragment viewerFragment);
}
