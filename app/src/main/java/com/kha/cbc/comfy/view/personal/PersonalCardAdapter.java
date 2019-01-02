package com.kha.cbc.comfy.view.personal;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.daimajia.swipe.SwipeLayout;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.model.PersonalCard;
import com.kha.cbc.comfy.model.common.BaseCardModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/2
 */

public class PersonalCardAdapter extends RecyclerView.Adapter<PersonalCardAdapter.PersonalCardViewHolder> {

    List<BaseCardModel> personalCardList;
    PersonalFragment fragment;
    Boolean cannotBeDeleted;
    Activity motherActivity;

    PersonalCardAdapter(List<BaseCardModel> personalCardList,
                        PersonalFragment fragment, Activity motherActivity) {
        this.personalCardList = personalCardList;
        this.fragment = fragment;
        this.cannotBeDeleted = false;
        this.motherActivity = motherActivity;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == personalCardList.size() - 1) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public PersonalCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.personal_cards, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.plus_card, parent, false);
        }
        return new PersonalCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonalCardViewHolder holder, final int position) {
        PersonalCardViewHolder cardViewHolder = holder;
        if (position != personalCardList.size() - 1) {
            if (personalCardList.get(position).isRemind()) {
                cardViewHolder.alarmView.setVisibility(View.VISIBLE);
            }
            cardViewHolder.name.setText(personalCardList.get(position).getTitle());
            cardViewHolder.description.setText(personalCardList.get(position).getDescription());cardViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            cardViewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {

                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onClose(SwipeLayout layout) {

                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                }
            });
            cardViewHolder.checkView.setOnClickListener(v -> {
                fragment.onCompleteCard((PersonalCard) personalCardList.get(position));
                personalCardList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(0, getItemCount());
            });
            cardViewHolder.deleteView.setOnClickListener(v -> {
                fragment.onDeleteItemInDB((PersonalCard) personalCardList.get(position));
                personalCardList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(0, getItemCount());
            });
            cardViewHolder.changeView.setOnClickListener(v -> {
                fragment.plusCard((PersonalCard) personalCardList.get(position));
                notifyItemChanged(position);
            });
            cardViewHolder.shareView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                List<ResolveInfo> resolveInfos = motherActivity.getPackageManager().queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                if (!resolveInfos.isEmpty()) {
                    List<Intent> targetIntents = new ArrayList<>();
                    for (ResolveInfo info : resolveInfos) {
                        ActivityInfo ainfo = info.activityInfo;
                        addShareIntent(targetIntents, ainfo, position);
                    }
                    Intent chooserIntent = null;
                    if (targetIntents.size() != 0) {
                        chooserIntent = Intent.createChooser(targetIntents.remove(0), "请选择分享平台");
                    }
                    if (chooserIntent != null) {
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetIntents.toArray(new Parcelable[]{}));
                    }
                    try {
                        motherActivity.startActivity(chooserIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(motherActivity, "找不到该分享应用组件", Toast.LENGTH_SHORT).show();
                    }
                    //startActivity(Intent.createChooser(intent, getTitle()));
                }
            });
        } else {
            holder.itemView.setOnClickListener(view
                    -> fragment.plusCard(personalCardList.get(position).getTaskId()));
        }
    }

    private void addShareIntent(List<Intent> list,ActivityInfo ainfo, int position) {
        Intent target = new Intent(Intent.ACTION_SEND);
        target.setType("text/plain");
        target.putExtra(Intent.EXTRA_TEXT,
            "今天，我的任务是 " + this.personalCardList.get(position).getTitle() + " ,我要完成: "
                + this.personalCardList.get(position).getDescription() + " \n 冲鸭！！！！！"
        );
        target.setPackage(ainfo.packageName);
        target.setClassName(ainfo.packageName, ainfo.name);
        list.add(target);
    }

    @Override
    public int getItemCount() {
        return personalCardList.size();
    }

    public class PersonalCardViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;
        SwipeLayout swipeLayout;
        ImageView checkView;
        ImageView deleteView;
        ImageView alarmView;
        ImageView changeView;
        ImageButton shareView;

        public PersonalCardViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.personal_card_name);
            description = itemView.findViewById(R.id.personal_card_description);
            swipeLayout = itemView.findViewById(R.id.personal_card_swipe_layout);
            checkView = itemView.findViewById(R.id.personal_card_check);
            deleteView = itemView.findViewById(R.id.personal_card_delete);
            alarmView = itemView.findViewById(R.id.alarmImage);
            changeView = itemView.findViewById(R.id.personal_card_change);
            shareView = itemView.findViewById(R.id.share_button);
        }
    }
}
