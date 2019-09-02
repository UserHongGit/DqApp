

package com.hong.mvp.presenter;


import com.hong.R;
import com.hong.dao.DaoSession;
import com.hong.dao.MyTrendingLanguage;
import com.hong.dao.MyTrendingLanguageDao;
import com.hong.mvp.contract.ITrendingContract;
import com.hong.mvp.model.TrendingLanguage;
import com.hong.mvp.presenter.base.BasePresenter;
import com.hong.util.JSONUtils;
import com.hong.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public class TrendingPresenter extends BasePresenter<ITrendingContract.View>
        implements ITrendingContract.Presenter{

    private ArrayList<TrendingLanguage> languages ;

    @Inject
    public TrendingPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public ArrayList<TrendingLanguage> getLanguagesFromLocal() {
        List<MyTrendingLanguage> myLanguages = daoSession.getMyTrendingLanguageDao().queryBuilder()
                .orderAsc(MyTrendingLanguageDao.Properties.Order)
                .list();
        if(StringUtils.isBlankList(myLanguages)){
            languages = JSONUtils.jsonToArrayList(getString(R.string.trending_languages), TrendingLanguage.class);
            languages.addAll(0, getFixedLanguages());
            languages = sortLanguages(languages);
        } else {
            languages = TrendingLanguage.generateFromDB(myLanguages);
            fixFixedLanguagesName(languages);
        }
        fixLanguagesSlug(languages);
        return languages;
    }

    private ArrayList<TrendingLanguage> sortLanguages(ArrayList<TrendingLanguage> languages){
        for(int i = 0; i < languages.size(); i++){
            languages.get(i).setOrder(i + 1);
        }
        return languages;
    }

    public ArrayList<TrendingLanguage> getLanguages() {
        return languages;
    }

    private ArrayList<TrendingLanguage> getFixedLanguages(){
        ArrayList<TrendingLanguage> fixedLanguages = new ArrayList<>();
        fixedLanguages.add(new TrendingLanguage(getString(R.string.all_languages), "all"));
        fixedLanguages.add(new TrendingLanguage(getString(R.string.unknown_languages), "unknown"));
        return fixedLanguages;
    }

    private void fixFixedLanguagesName(ArrayList<TrendingLanguage> languages){
        for(TrendingLanguage language : languages){
            if(language.getSlug().equals("all")){
                language.setName(getString(R.string.all_languages));
            } else  if(language.getSlug().equals("unknown")){
                language.setName(getString(R.string.unknown_languages));
            }
        }
    }

    private void fixLanguagesSlug(ArrayList<TrendingLanguage> languages){
        for(TrendingLanguage language : languages){
            String slug = language.getSlug();
            if(slug.contains("?")){
                slug = slug.substring(0, slug.indexOf("?"));
                language.setSlug(slug);
            }
        }
    }

}
