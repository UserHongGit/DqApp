

package com.hong.ui.adapter.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;


import com.hong.R;
import com.hong.mvp.model.Issue;
import com.hong.mvp.model.Repository;
import com.hong.ui.fragment.ActivityFragment;
import com.hong.ui.fragment.IssuesFragment;
import com.hong.ui.fragment.NotificationsFragment;
import com.hong.ui.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ThirtyDegreesRay on 2017/8/15 21:10:14
 */

public class FragmentPagerModel {

    private String title;
    private BaseFragment fragment;

    public FragmentPagerModel(String title, BaseFragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    /**
     * 日记本的点击进入Fragment  通过这里增加ViewPager
     */
    public static List<FragmentPagerModel> createRepoPagerList(@NonNull Context context
            , @NonNull final Repository repository, @NonNull ArrayList<Fragment> fragments) {

        return setPagerFragmentFlag(Arrays.asList(
//                new FragmentPagerModel(context.getString(R.string.info),
//                        getFragment(fragments, 0, () -> RepoInfoFragment.create(repository))),
//                new FragmentPagerModel(context.getString(R.string.files),
//                        getFragment(fragments, 1, () -> RepoFilesFragment.create(repository))),
//                new FragmentPagerModel(context.getString(R.string.commits),
//                        getFragment(fragments, 2,
//                                () -> CommitsFragment.createForRepo(repository.getOwner().getLogin(),
//                                repository.getName(), repository.getDefaultBranch()))),
                new FragmentPagerModel(context.getString(R.string.activity),
                        getFragment(fragments, 0,
                                () -> ActivityFragment.create(ActivityFragment.ActivityType.Repository,
                                repository.getName(), repository.getDailyid())))
        ));
    }

//    public static List<FragmentPagerModel> createProfilePagerList(Context context, final User user
//            , @NonNull ArrayList<Fragment> fragments) {
//        List<FragmentPagerModel> list = new ArrayList<>();
//        list.add(new FragmentPagerModel(context.getString(R.string.info),
//                getFragment(fragments, 0, () -> ProfileInfoFragment.create(user))));
//        list.add(new FragmentPagerModel(context.getString(R.string.activity),
//                getFragment(fragments, 1, () -> ActivityFragment.create(ActivityFragment.ActivityType.User, user.getLogin(), null))));
//        if (user.isUser()) {
//            list.add(new FragmentPagerModel(context.getString(R.string.starred),
//                    getFragment(fragments, 2, () -> RepositoriesFragment.create(RepositoriesFragment.RepositoriesType.STARRED, user.getLogin()))));
//        }
//        return setPagerFragmentFlag(list);
//    }
//
//    public static List<FragmentPagerModel> createSearchPagerList(@NonNull Context context
//            , @NonNull final ArrayList<SearchModel> searchModels, @NonNull ArrayList<Fragment> fragments) {
//        return setPagerFragmentFlag(Arrays.asList(
//                new FragmentPagerModel(context.getString(R.string.repositories),
//                        getFragment(fragments, 0, () -> RepositoriesFragment.createForSearch(searchModels.get(0)))),
//                new FragmentPagerModel(context.getString(R.string.users),
//                        getFragment(fragments, 1, () -> UserListFragment.createForSearch(searchModels.get(1))))
//        ));
//    }
//
//    public static List<FragmentPagerModel> createTrendingPagerList(
//            @NonNull Context context, @NonNull ArrayList<Fragment> fragments) {
//        return setPagerFragmentFlag(Arrays.asList(
//                new FragmentPagerModel(context.getString(R.string.daily),
//                        getFragment(fragments, 0, () -> RepositoriesFragment.createForTrending(TrendingSince.Daily))),
//                new FragmentPagerModel(context.getString(R.string.weekly),
//                        getFragment(fragments, 1, () -> RepositoriesFragment.createForTrending(TrendingSince.Weekly))),
//                new FragmentPagerModel(context.getString(R.string.monthly),
//                        getFragment(fragments, 2, () -> RepositoriesFragment.createForTrending(TrendingSince.Monthly)))
//        ));
//    }

    public static List<FragmentPagerModel> createRepoIssuesPagerList(@NonNull Context context
            , @NonNull final String userId, @NonNull final String repoName, @NonNull ArrayList<Fragment> fragments) {
        return setPagerFragmentFlag(Arrays.asList(
                //这里设置点开之后ViewPager的标题
                new FragmentPagerModel(context.getString(R.string.open),
                        getFragment(fragments, 0, () -> IssuesFragment.createForRepo(Issue.IssueState.open, userId, repoName))),
                new FragmentPagerModel(context.getString(R.string.closed),
                        getFragment(fragments, 1, () -> IssuesFragment.createForRepo(Issue.IssueState.closed, userId, repoName)))
        ));
    }

    public static List<FragmentPagerModel> createUserIssuesPagerList(@NonNull Context context
            , @NonNull ArrayList<Fragment> fragments) {
        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.open),
                        getFragment(fragments, 0, () -> IssuesFragment.createForUser(Issue.IssueState.open))),
                new FragmentPagerModel(context.getString(R.string.closed),
                        getFragment(fragments, 1, () -> IssuesFragment.createForUser(Issue.IssueState.closed)))
        ));
    }

//    public static List<FragmentPagerModel> createMarkdownEditorPagerList(@NonNull Context context
//            , final String text, @NonNull ArrayList<Fragment> fragments, final ArrayList<String> mentionUsers) {
//        return setPagerFragmentFlag(Arrays.asList(
//                new FragmentPagerModel(context.getString(R.string.write),
//                        getFragment(fragments, 0, () -> MarkdownEditorFragment.create(text, mentionUsers))),
//                new FragmentPagerModel(context.getString(R.string.preview),
//                        getFragment(fragments, 1, () -> MarkdownPreviewFragment.create()))
//        ));
//    }
//
    public static List<FragmentPagerModel> createNotificationsPagerList(
            @NonNull Context context, @NonNull ArrayList<Fragment> fragments) {
        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.unread),
                        getFragment(fragments, 0, () -> NotificationsFragment.create(NotificationsFragment.NotificationsType.Unread))),
                new FragmentPagerModel(context.getString(R.string.participating),
                        getFragment(fragments, 1, () -> NotificationsFragment.create(NotificationsFragment.NotificationsType.Participating))),
                new FragmentPagerModel(context.getString(R.string.all),
                        getFragment(fragments, 2, () -> NotificationsFragment.create(NotificationsFragment.NotificationsType.All)))
        ));
    }
//
//    public static List<FragmentPagerModel> createTracePagerList(
//            @NonNull Context context, @NonNull ArrayList<Fragment> fragments) {
//        return setPagerFragmentFlag(Arrays.asList(
//                new FragmentPagerModel(context.getString(R.string.repositories),
//                        getFragment(fragments, 0, () -> RepositoriesFragment.createForTrace())),
//                new FragmentPagerModel(context.getString(R.string.users),
//                        getFragment(fragments, 1, () -> UserListFragment.createForTrace()))
//        ));
//    }
//
//    public static List<FragmentPagerModel> createBookmarksPagerList(
//            @NonNull Context context, @NonNull ArrayList<Fragment> fragments) {
//        return setPagerFragmentFlag(Arrays.asList(
//                new FragmentPagerModel(context.getString(R.string.repositories),
//                        getFragment(fragments, 0, () -> RepositoriesFragment.createForBookmark())),
//                new FragmentPagerModel(context.getString(R.string.users),
//                        getFragment(fragments, 1, () -> UserListFragment.createForBookmark()))
//        ));
//    }

    private static List<FragmentPagerModel> setPagerFragmentFlag(List<FragmentPagerModel> list) {
        for (FragmentPagerModel model : list) {
            model.getFragment().setPagerFragment(true);
        }
        return list;
    }

    private static BaseFragment getFragment(ArrayList<Fragment> fragments
            , int position, FragmentCreator fragmentCreator){
        Fragment fragment  = fragments.get(position);
        if(fragment == null){
            fragment = fragmentCreator.createFragment();
//            Logger.d("create fragment " + fragment + (Math.random() * 1000 * 1000));
        }else{
//            Logger.d("reuse fragment" + fragment + (Math.random() * 1000 * 1000));
        }
        return (BaseFragment) fragment;
    }

    interface FragmentCreator<F extends Fragment>{
        F createFragment();
    }

}
