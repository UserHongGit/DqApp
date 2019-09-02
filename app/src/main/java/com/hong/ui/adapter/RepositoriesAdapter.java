package com.hong.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;

import com.hong.R;
import com.hong.mvp.model.Repository;
import com.hong.ui.adapter.base.BaseAdapter;
import com.hong.ui.adapter.base.BaseViewHolder;
import com.hong.ui.fragment.base.BaseFragment;
import com.hong.util.MyUtils;
import com.hong.util.StringUtils;
import com.hong.util.ViewUtils;
import es.dmoral.toasty.Toasty;
import javax.inject.Inject;

public class RepositoriesAdapter extends BaseAdapter<RepositoriesAdapter.ViewHolder, Repository> {
    private String TAG;


    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_user_avatar)
        ImageView ivUserAvatar;
        @BindView(R.id.language_color)
        ImageView languageColor;
        @BindView(R.id.tv_fork_num)
        TextView tvForkNum;
        @BindView(R.id.tv_language)
        TextView tvLanguage;
        @BindView(R.id.tv_owner_name2)
        TextView tvOwnerName2;
        @BindView(R.id.tv_repo_description)
        TextView tvRepoDescription;
        @BindView(R.id.tv_repo_description2)
        TextView tvRepoDescription2;
        @BindView(R.id.tv_repo_description3)
        TextView tvRepoDescription3;
        @BindView(R.id.tv_repo_description4)
        TextView tvRepoDescription4;
        @BindView(R.id.tv_repo_name)
        TextView tvRepoName;
        @BindView(R.id.tv_star_num)
        TextView tvStarNum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @OnClick(R.id.iv_user_avatar)
        public void onUserClick() {
            Log.i(RepositoriesAdapter.this.TAG, "onUserClick: ____________");
            Toasty.normal((Activity) RepositoriesAdapter.this.context, "ResponseAdpter___");
        }
    }

    @Inject
    public RepositoriesAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(RepositoriesAdapter.class.getName());
        stringBuilder.append("==========================");
        this.TAG = stringBuilder.toString();
    }

    /* access modifiers changed from: protected */
    public int getLayoutId(int viewType) {
        return R.layout.layout_item_repository;
    }

    /* access modifiers changed from: protected */
    @NonNull
    public ViewHolder getViewHolder(@NonNull View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Repository repository = (Repository) this.data.get(position);
        boolean hasOwnerAvatar = !StringUtils.isBlank(repository.getAvatarurl());
        holder.tvRepoName.setText(repository.getWellCommonName());
        holder.tvLanguage.setText(repository.getIntelligenceCode());
        TextView textView = holder.tvRepoDescription;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("作业内容:  ");
        stringBuilder.append(repository.getWorkBrief());
        ViewUtils.setTextView(textView, stringBuilder.toString());
        textView = holder.tvRepoDescription2;
        stringBuilder = new StringBuilder();
        stringBuilder.append("施工队伍:  ");
        stringBuilder.append(repository.getTeamName());
        ViewUtils.setTextView(textView, stringBuilder.toString());
        textView = holder.tvRepoDescription3;
        stringBuilder = new StringBuilder();
        stringBuilder.append("开工日期:  ");
        stringBuilder.append(MyUtils.dateConvertStr(repository.getWorkDate()));
        ViewUtils.setTextView(textView, stringBuilder.toString());
        textView = holder.tvRepoDescription4;
        stringBuilder = new StringBuilder();
        stringBuilder.append("上报日期:  ");
        stringBuilder.append(MyUtils.dateConvertStr(repository.getReportTime()));
        ViewUtils.setTextView(textView, stringBuilder.toString());
        holder.tvStarNum.setText(repository.getBanbaoType());
        textView = holder.tvForkNum;
        stringBuilder = new StringBuilder();
        stringBuilder.append(repository.getOrderClasses());
        stringBuilder.append(" 班次");
        textView.setText(stringBuilder.toString());
        holder.tvOwnerName2.setText(repository.getCompleteJudgement().equals("no") ? "未完井" : "已完井");
    }
}
