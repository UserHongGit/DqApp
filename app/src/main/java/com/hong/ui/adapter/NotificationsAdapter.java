

package com.hong.ui.adapter;

import android.app.Notification;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hong.R;
import com.hong.common.GlideApp;
import com.hong.mvp.model.Repository;
import com.hong.ui.adapter.base.BaseAdapter;
import com.hong.ui.adapter.base.BaseViewHolder;
import com.hong.ui.fragment.base.BaseFragment;
import com.hong.ui.widget.ToastAbleImageButton;
import com.hong.util.PrefUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ThirtyDegreesRay on 2017/11/6 20:05:32
 */

public class NotificationsAdapter extends BaseAdapter<BaseViewHolder,
        DoubleTypesModel<Repository, Notification>> {
    private static String TAG = NotificationsAdapter.class.getSimpleName()+"===============";

    private NotificationAdapterListener listener;

    @Inject
    public NotificationsAdapter(Context context, BaseFragment fragment) {
        super(context, fragment);
    }

    public void setListener(NotificationAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutId(int viewType) {
        if (viewType == 0) {
            Log.i(TAG, "getLayoutId: 0000000000000000");
            return R.layout.layout_item_notification_repo;
        } else {
            Log.i(TAG, "getLayoutId: 111111111111111");
            return R.layout.layout_item_notification;
        }
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int viewType) {
        if(viewType == 0){
            Log.i(TAG, "getViewHolder: 000000000000000000");
            return new RepoViewHolder(itemView);
        } else {
            Log.i(TAG, "getViewHolder: 11111111111111111");
            return new NotificationViewHolder(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.i(TAG, "getItemViewType: ");
        return data.get(position).getTypePosition();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        Log.i(TAG, "onBindViewHolder: ");
        if(viewHolder instanceof RepoViewHolder){
            RepoViewHolder holder = (RepoViewHolder) viewHolder;
            Repository model = data.get(position).getM1();
            holder.repoName.setText(model.getFullName());
            GlideApp.with(fragment)
                    .load(model.getOwner().getAvatarUrl())
                    .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
                    .into(holder.userAvatar);
        } else {
            NotificationViewHolder holder = (NotificationViewHolder) viewHolder;
            Notification model = data.get(position).getM2();
//            holder.title.setText(model.getSubject().getTitle());
//            if(model.isUnread()){
//                holder.status.setImageResource(R.drawable.ic_mark_unread);
//                holder.status.setImageTintList(ColorStateList.valueOf(ViewUtils.getAccentColor(context)));
//            } else {
//                holder.status.setVisibility(View.INVISIBLE);
////                holder.status.setImageResource(R.drawable.ic_mark_readed);
////                holder.status.setImageTintList(ColorStateList.valueOf(ViewUtils.getSecondaryTextColor(context)));
//            }
//            holder.time.setText(StringUtils.getNewsTimeStr(context, model.getUpdateAt()));
//
//            int padding = WindowUtil.dipToPx(context, 2);
//            if(NotificationSubject.Type.Issue.equals(model.getSubject().getType())){
//                holder.typeIcon.setImageResource(R.drawable.ic_issues);
//                padding = 0;
//            } else if(NotificationSubject.Type.CommitwriteToParcel.equals(model.getSubject().getType())){
//                holder.typeIcon.setImageResource(R.drawable.ic_commit);
//            } else {
//                holder.typeIcon.setImageResource(R.drawable.ic_pull);
//            }
//            holder.typeIcon.setPadding(padding, padding, padding, padding);

        }
    }

    class NotificationViewHolder extends BaseViewHolder {

        @BindView(R.id.type_icon) AppCompatImageView typeIcon;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.status) AppCompatImageView status;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

    class RepoViewHolder extends BaseViewHolder {

        @BindView(R.id.user_avatar) CircleImageView userAvatar;
        @BindView(R.id.repo_name) TextView repoName;
        @BindView(R.id.mark_as_read_bn)
        ToastAbleImageButton markAsReadBn;

        public RepoViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @OnClick(R.id.user_avatar)
        public void onUserClicked() {
            Log.i(TAG, "onUserClicked: ");
            if(getAdapterPosition() != RecyclerView.NO_POSITION) {
//                ProfileActivity.show((Activity) context, userAvatar,
//                        getRepository().getOwner().getLogin(), getRepository().getOwner().getAvatarUrl());
            }
        }

        @OnClick(R.id.repo_name)
        public void onRepoClicked() {
            Log.i(TAG, "onRepoClicked: ");
            if(getAdapterPosition() != RecyclerView.NO_POSITION) {
//                RepositoryActivity.show(context, getRepository().getOwner().getLogin(),
//                        getRepository().getName());
            }
        }

        @OnClick(R.id.mark_as_read_bn)
        public void onMarkAsReadClicked() {
            Log.i(TAG, "onMarkAsReadClicked: ");
            if(getAdapterPosition() != RecyclerView.NO_POSITION) {
                listener.onRepoMarkAsReadClicked(data.get(getAdapterPosition()).getM1());
            }
        }

        private Repository getRepository(){
            return data.get(getAdapterPosition()).getM1();
        }

    }

    public interface NotificationAdapterListener{
        void onRepoMarkAsReadClicked(@NonNull Repository repository);
    }

}
