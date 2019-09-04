package com.hong.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.hong.AppConfig;
import com.hong.AppData;
import com.hong.R;
import com.hong.common.Event.SearchEvent;
import com.hong.dao.DaoSession;
import com.hong.http.core.HttpObserver;
import com.hong.http.core.HttpResponse;
import com.hong.http.error.HttpPageNoFoundError;
import com.hong.mvp.contract.IRepositoriesContract.Presenter;
import com.hong.mvp.contract.IRepositoriesContract.View;
import com.hong.mvp.model.Repository;
import com.hong.mvp.model.SearchModel;
import com.hong.mvp.model.SearchModel.SearchType;
import com.hong.mvp.model.TrendingLanguage;
import com.hong.mvp.model.User;
import com.hong.mvp.model.filter.RepositoriesFilter;
import com.hong.mvp.model.filter.TrendingSince;
import com.hong.mvp.presenter.base.BasePagerPresenter;
import com.hong.ui.fragment.RepositoriesFragment;
import com.hong.ui.fragment.RepositoriesFragment.RepositoriesType;
import com.hong.util.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import org.greenrobot.eventbus.Subscribe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RepositoriesPresenter extends BasePagerPresenter<View> implements Presenter {
    private String TAG;
    @AutoAccess
    RepositoriesFilter filter;
    @AutoAccess
    TrendingLanguage language;
    @AutoAccess
    String repo;
    private ArrayList<Repository> repos;
    @AutoAccess
    SearchModel searchModel;
    @AutoAccess
    TrendingSince since;
    @AutoAccess
    RepositoriesType type;
    @AutoAccess
    String user;

    /* renamed from: com.upc.mvp.presenter.RepositoriesPresenter$5 */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$upc$ui$fragment$RepositoriesFragment$RepositoriesType = new int[RepositoriesType.values().length];

        static {
            try {
                $SwitchMap$com$upc$ui$fragment$RepositoriesFragment$RepositoriesType[RepositoriesType.OWNED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
        }
    }

    @Inject
    public RepositoriesPresenter(DaoSession daoSession) {
        super(daoSession);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(RepositoriesPresenter.class.getSimpleName());
        stringBuilder.append("====================");
        this.TAG = stringBuilder.toString();
    }

    public void onViewInitialized() {
        super.onViewInitialized();
        Log.i(this.TAG, "onViewInitialized: ");
        if (this.type.equals(RepositoriesType.SEARCH)) {
            setEventSubscriber(true);
        }
    }

    /* access modifiers changed from: protected */
    public void loadData() {
        Log.i(this.TAG, "loadData: ");
        if (RepositoriesType.SEARCH.equals(this.type)) {
            Log.i(this.TAG, "loadData: Search....");
        } else if (RepositoriesType.TRACE.equals(this.type)) {
            Log.i(this.TAG, "loadData: Trace...");
        } else if (RepositoriesType.BOOKMARK.equals(this.type)) {
            Log.i(this.TAG, "loadData: BookMarks...");
        } else if (RepositoriesType.COLLECTION.equals(this.type)) {
            Log.i(this.TAG, "loadData: COLLECTION....");
            loadCollection(false);
        } else if (RepositoriesType.TOPIC.equals(this.type)) {
            Log.i(this.TAG, "loadData: TOPIC....");
            initSearchModelForTopic();
            searchRepos(1);
        } else if (RepositoriesType.TRENDING.equals(this.type)) {
            Log.i(this.TAG, "loadData: TRENDING.....");
            loadTrending(false);
        } else {
            loadRepositories(false, 1);
        }
    }

    @Override
    protected void loadData(int itemId) {

    }

    public void loadRepositories(final boolean isReLoad, final int page) {
        Log.i(TAG, "loadRepositories: ");
        filter = getFilter();
        if (type.equals(RepositoriesFragment.RepositoriesType.SEARCH)) {
            Log.i(TAG, "loadRepositories: SERACH2....");
            searchRepos(page);
            return;
        }
        if (RepositoriesFragment.RepositoriesType.TRACE.equals(type)) {
            Log.i(TAG, "loadRepositories: TRACE2...");
            loadTrace(page);
            return;
        }
        if (RepositoriesFragment.RepositoriesType.BOOKMARK.equals(type)) {
            Log.i(TAG, "loadRepositories: BOOKMARK2....");
//            loadBookmarks(page);
            return;
        }
        if (RepositoriesFragment.RepositoriesType.COLLECTION.equals(type)) {
            Log.i(TAG, "loadRepositories: COLLECTION2...");
            loadCollection(isReLoad);
            return;
        }
        if (RepositoriesFragment.RepositoriesType.TOPIC.equals(type)) {
            Log.i(TAG, "loadRepositories: TOPIC2...");
            initSearchModelForTopic();
            searchRepos(page);
            return;
        }
        if(RepositoriesFragment.RepositoriesType.TRENDING.equals(type)){
            Log.i(TAG, "loadRepositories: TRENDING2....");
            loadTrending(isReLoad);
            return;
        }
        mView.showLoading();
        final boolean readCacheFirst = !isReLoad && page == 1;

        HttpObserver<ArrayList<Repository>> httpObserver = new HttpObserver<ArrayList<Repository>>() {
            @Override
            public void onError(@NonNull Throwable error) {
                Log.i(TAG, "onError: "+error);
                mView.hideLoading();
                handleError(error);
            }

            @Override
            public void onSuccess(@NonNull HttpResponse<ArrayList<Repository>> response) {
                Log.i(TAG, "onSuccess: "+response);
                mView.hideLoading();
                if (isReLoad || readCacheFirst || repos == null || page == 1) {

                    repos = response.body();
                } else {
                    repos.addAll(response.body());
                }
                if (response.body().size() == 0 && repos.size() != 0) {
                    mView.setCanLoadMore(false);
                } else {
                    mView.showRepositories(repos);
                }
            }
        };

        generalRxHttpExecute(new IObservableCreator<ArrayList<Repository>>() {
            @Nullable
            @Override
            public Observable<Response<ArrayList<Repository>>> createObservable(boolean forceNetWork) {
                return getObservable(forceNetWork, page);
            }
        }, httpObserver, readCacheFirst);
    }

    public void loadRepositories(RepositoriesFilter filter) {
        Log.i(this.TAG, "loadRepositories: ");
        this.filter = filter;
        loadRepositories(false, 1);
    }

    private Observable<Response<ArrayList<Repository>>> getObservable(boolean forceNetWork, int page) {
        String str = this.TAG;
        if (AnonymousClass5.$SwitchMap$com$upc$ui$fragment$RepositoriesFragment$RepositoriesType[this.type.ordinal()] != 1) {
            return null;
        }
        str = this.TAG;
        Log.i(TAG, "getObservable: ____看看油田"+AppData.INSTANCE.getLoggedUser().getOilfield());
        return getRepoService().getUserRepos(forceNetWork, page, AppData.INSTANCE.getLoggedUser().getOilfield());
    }

    private void searchRepos(int page) {
        Log.i(this.TAG, "searchRepos: ");
        ((View) this.mView).showLoading();
    }

    @Subscribe
    public void onSearchEvent(@NonNull SearchEvent searchEvent) {
        Log.i(this.TAG, "onSearchEvent: ");
        prepareLoadData();
    }

    private void handleError(Throwable error) {
        if (!StringUtils.isBlankList(this.repos)) {
            ((View) this.mView).showErrorToast(getErrorTip(error));
        } else if (error instanceof HttpPageNoFoundError) {
            ((View) this.mView).showRepositories(new ArrayList());
        } else {
            ((View) this.mView).showLoadError(getErrorTip(error));
        }
    }

    public String getUser() {
        return this.user;
    }

    public RepositoriesType getType() {
        return this.type;
    }

    public RepositoriesFilter getFilter() {
        Log.i(this.TAG, "getFilter: ");
        if (this.filter == null) {
            this.filter = RepositoriesType.STARRED.equals(this.type) ? RepositoriesFilter.DEFAULT_STARRED_REPO : RepositoriesFilter.DEFAULT;
        }
        return this.filter;
    }

    private void loadTrace(int page) {
        long start = System.currentTimeMillis();
        Log.i(this.TAG, "loadTrace: ");
    }

    private void showQueryRepos(ArrayList<Repository> queryRepos, int page) {
        Log.i(this.TAG, "showQueryRepos: ");
        ArrayList arrayList = this.repos;
        if (arrayList == null || page == 1) {
            this.repos = queryRepos;
        } else {
            arrayList.addAll(queryRepos);
        }
        ((View) this.mView).showRepositories(this.repos);
        ((View) this.mView).hideLoading();
    }

    public void setLanguage(TrendingLanguage language) {
        this.language = language;
    }

    private void loadCollection(boolean isReload) {
        Log.i(this.TAG, "loadCollection: ");
        ((View) this.mView).showLoading();
        HttpObserver<ResponseBody> httpObserver = new HttpObserver<ResponseBody>() {
            public void onError(Throwable error) {
                ((View) RepositoriesPresenter.this.mView).hideLoading();
                ((View) RepositoriesPresenter.this.mView).showLoadError(RepositoriesPresenter.this.getErrorTip(error));
            }

            public void onSuccess(HttpResponse<ResponseBody> response) {
                try {
                    RepositoriesPresenter.this.parseCollectionsPageData(((ResponseBody) response.body()).string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void parseCollectionsPageData(String page) {
        Log.i(TAG, "parseCollectionsPageData: ");
        Observable.just(page)
                .map(s -> {
                    ArrayList<Repository> repos = new ArrayList<>();
                    try {
                        Document doc = Jsoup.parse(s, AppConfig.GITHUB_BASE_URL);
                        Elements elements = doc.getElementsByTag("article");
                        for (Element element : elements) {
                            //maybe a user or an org, so add catch
                            try{
                                repos.add(parseCollectionsRepositoryData(element));
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    return repos;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    if(mView == null) return;
                    if(results.size() != 0){
                        repos = results;
                        mView.hideLoading();
                        mView.showRepositories(repos);
                    } else {
                        String errorTip = String.format(getString(R.string.github_page_parse_error),
                                getString(R.string.repo_collections));
                        mView.showLoadError(errorTip);
                        mView.hideLoading();
                    }
                });
    }

    public /* synthetic */ ArrayList lambda$parseCollectionsPageData$0$RepositoriesPresenter(String s) {
        ArrayList<Repository> repos = new ArrayList();
        try {
            Iterator it = Jsoup.parse(s, AppConfig.GITHUB_BASE_URL).getElementsByTag("article").iterator();
            while (it.hasNext()) {
                try {
                    repos.add(parseCollectionsRepositoryData((Element) it.next()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return repos;
    }

    public /* synthetic */ void lambda$parseCollectionsPageData$1$RepositoriesPresenter(ArrayList results) {
        if (this.mView != null) {
            if (results.size() != 0) {
                this.repos = results;
                ((View) this.mView).hideLoading();
                ((View) this.mView).showRepositories(this.repos);
            } else {
                ((View) this.mView).showLoadError(String.format(getString(R.string.github_page_parse_error), new Object[]{getString(R.string.repo_collections)}));
                ((View) this.mView).hideLoading();
            }
        }
    }

    private Repository parseCollectionsRepositoryData(Element element) throws Exception {
        Element element2 = element;
        Log.i(this.TAG, "parseCollectionsRepositoryData: ");
        String fullName = element2.select("div > h1 > a").attr("href").substring(1);
        String str = "/";
        String owner = fullName.substring(0, fullName.lastIndexOf(str));
        str = fullName.substring(fullName.lastIndexOf(str) + 1);
        String ownerAvatar = "";
        Elements articleElements = element2.getElementsByTag("div");
        Element descElement = (Element) articleElements.get(articleElements.size() - 2);
        String str2 = "";
        StringBuilder desc = new StringBuilder(str2);
        for (TextNode textNode : descElement.textNodes()) {
            desc.append(textNode.getWholeText());
        }
        Element numElement = articleElements.last();
        String str3 = "a";
        String starNumStr = ((TextNode) ((Element) numElement.select(str3).get(0)).textNodes().get(1)).toString();
        str3 = ((TextNode) ((Element) numElement.select(str3).get(1)).textNodes().get(1)).toString();
        String language = "";
        String str4 = "span > span";
        if (numElement.select(str4).size() > 0) {
            language = ((TextNode) ((Element) numElement.select(str4).get(1)).textNodes().get(0)).toString();
        }
        Repository repo = new Repository();
        repo.setFullName(fullName);
        repo.setName(str);
        User user = new User();
        user.setLogin(owner);
        user.setAvatarUrl(ownerAvatar);
        repo.setOwner(user);
        repo.setDescription(desc.toString());
        String str5 = " ";
        repo.setStargazersCount(Integer.parseInt(starNumStr.replaceAll(str5, str2)));
        repo.setForksCount(Integer.parseInt(str3.replaceAll(str5, str2)));
        repo.setLanguage(language);
        return repo;
    }

    private void initSearchModelForTopic() {
        Log.i(this.TAG, "initSearchModelForTopic: ");
        if (this.searchModel == null) {
            this.searchModel = new SearchModel(SearchType.Repository);
        }
    }

    private void loadTrending(boolean isReload) {
        Log.i(this.TAG, "loadTrending: ");
        ((View) this.mView).showLoading();
        HttpObserver<ResponseBody> httpObserver = new HttpObserver<ResponseBody>() {
            public void onError(Throwable error) {
                ((View) RepositoriesPresenter.this.mView).hideLoading();
                ((View) RepositoriesPresenter.this.mView).showLoadError(RepositoriesPresenter.this.getErrorTip(error));
            }

            public void onSuccess(HttpResponse<ResponseBody> response) {
                try {
                    RepositoriesPresenter.this.parseTrendingPageData(((ResponseBody) response.body()).string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void parseTrendingPageData(String page) {Log.i(TAG, "parseTrendingPageData: ");
        Observable.just(page)
                .map(s -> {
                    ArrayList<Repository> repos = new ArrayList<>();
                    try {
                        Document doc = Jsoup.parse(s, AppConfig.GITHUB_BASE_URL);
                        Elements elements = doc.getElementsByClass("col-12 d-block width-full py-4 border-bottom");
                        if(elements.size() != 0){
                            for (Element element : elements) {
                                try{
                                    repos.add(parseTrendingRepositoryData(element));
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        repos = null;
                    }

                    return repos;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    if(mView == null) return;
                    if(results != null){
                        repos = results;
                        mView.hideLoading();
                        mView.showRepositories(repos);
                    } else {
                        String errorTip = String.format(getString(R.string.github_page_parse_error),
                                getString(R.string.trending));
                        mView.showLoadError(errorTip);
                        mView.hideLoading();
                    }
                });

    }

    public /* synthetic */ ArrayList lambda$parseTrendingPageData$2$RepositoriesPresenter(String s) {
        ArrayList<Repository> repos = new ArrayList();
        try {
            Elements elements = Jsoup.parse(s, AppConfig.GITHUB_BASE_URL).getElementsByClass("col-12 d-block width-full py-4 border-bottom");
            if (elements.size() == 0) {
                return repos;
            }
            Iterator it = elements.iterator();
            while (it.hasNext()) {
                try {
                    repos.add(parseTrendingRepositoryData((Element) it.next()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return repos;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public /* synthetic */ void lambda$parseTrendingPageData$3$RepositoriesPresenter(ArrayList results) {
        if (this.mView != null) {
            if (results != null) {
                this.repos = results;
                ((View) this.mView).hideLoading();
                ((View) this.mView).showRepositories(this.repos);
            } else {
                ((View) this.mView).showLoadError(String.format(getString(R.string.github_page_parse_error), new Object[]{getString(R.string.trending)}));
                ((View) this.mView).hideLoading();
            }
        }
    }

    private Repository parseTrendingRepositoryData(Element element) throws Exception {
        Element element2 = element;
        Log.i(this.TAG, "parseTrendingRepositoryData: ");
        String fullName = element2.select("div > h3 > a").attr("href").substring(1);
        String str = "/";
        String owner = fullName.substring(0, fullName.lastIndexOf(str));
        str = fullName.substring(fullName.lastIndexOf(str) + 1);
        Element descElement = element2.select("div > p").first();
        String str2 = "";
        StringBuilder desc = new StringBuilder(str2);
        for (TextNode textNode : descElement.textNodes()) {
            desc.append(textNode.getWholeText());
        }
        Element numElement = element2.getElementsByClass("f6 text-gray mt-2").first();
        String language = "";
        String str3 = "span > span";
        if (numElement.select(str3).size() > 0) {
            language = ((TextNode) ((Element) numElement.select(str3).get(1)).textNodes().get(0)).toString().trim();
        }
        str3 = "a";
        String str4 = " ";
        String str5 = ",";
        String starNumStr = ((TextNode) ((Element) numElement.select(str3).get(0)).textNodes().get(1)).toString().replaceAll(str4, str2).replaceAll(str5, str2);
        String forkNumStr = ((TextNode) ((Element) numElement.select(str3).get(1)).textNodes().get(1)).toString().replaceAll(str4, str2).replaceAll(str5, str2);
        Element periodElement = numElement.getElementsByClass("d-inline-block float-sm-right").first();
        String periodNumStr = "0";
        if (periodElement != null) {
            String periodNumStr2 = ((Node) periodElement.childNodes().get(2)).toString().trim();
            periodNumStr = periodNumStr2.substring(0, periodNumStr2.indexOf(str4)).replaceAll(str5, str2);
        }
        Repository repo = new Repository();
        repo.setFullName(fullName);
        repo.setName(str);
        User user = new User();
        user.setLogin(owner);
        repo.setOwner(user);
        repo.setDescription(desc.toString().trim().replaceAll("\n", str2));
        repo.setStargazersCount(Integer.parseInt(starNumStr));
        repo.setForksCount(Integer.parseInt(forkNumStr));
        repo.setSinceStargazersCount(Integer.parseInt(periodNumStr));
        repo.setLanguage(language);
        repo.setSince(this.since);
        return repo;
    }

    public TrendingLanguage getLanguage() {
        return this.language;
    }
}
