package com.kha.cbc.comfy.presenter;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.yiiguxing.compositionavatar.CompositionAvatarView;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.kha.cbc.comfy.model.Stage;
import com.kha.cbc.comfy.model.TeamCard;
import com.kha.cbc.comfy.view.common.BaseRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/24
 */
public class TeamDetailPresenter extends BasePresenter {

    BaseRefreshView view;

    public TeamDetailPresenter(BaseRefreshView view) {
        this.view = view;
    }

    //TODO:！！！扩展
    public void loadMembers(String taskId){
        AVQuery<AVObject> queryForMember = new AVQuery<>("UserTaskMap");
        AVObject task = AVObject.createWithoutData("TeamTask", taskId);
        queryForMember.whereEqualTo("TeamTask", task);
        queryForMember.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

            }
        });
    }

    public void loadMembersAvatar(String taskId, CompositionAvatarView view){
        AVQuery<AVObject> queryForMember = new AVQuery<>("UserTaskMap");
        AVObject task = AVObject.createWithoutData("TeamTask", taskId);
        queryForMember.whereEqualTo("TeamTask", task);
        queryForMember.include("Member");
        queryForMember.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                ArrayList<String> memberAvatarUrlList = new ArrayList<>();
                for(AVObject memberMap : list){
                    AVObject member = memberMap.getAVObject("Member");
                    memberAvatarUrlList.add(member.getString("avatarUrl"));
                }
                for(String url: memberAvatarUrlList){
                    Glide.with(view).load(url).into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onLoadStarted(@Nullable Drawable placeholder) {

                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {

                        }

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            view.addDrawable(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }

                        @Override
                        public void removeCallback(@NonNull SizeReadyCallback cb) {

                        }

                        @Override
                        public void setRequest(@Nullable Request request) {

                        }

                        @Nullable
                        @Override
                        public Request getRequest() {
                            return null;
                        }

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onStop() {

                        }

                        @Override
                        public void onDestroy() {

                        }
                    });
                }
            }
        });
    }

    public void loadAllStages(String objectId, List<Stage> stageList) {
        view.refresh(true);
        AVObject task = AVObject.createWithoutData("TeamTask", objectId);
        AVQuery<AVObject> queryForStage = new AVQuery<>("Stage");
        queryForStage.whereEqualTo("TeamTask", task);
        queryForStage.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> alist, AVException e) {
                if (alist == null)
                    return;
                if (alist.isEmpty()) {
                    view.onComplete();
                    view.refresh(false);
                }
                for (AVObject stage : alist) {
                    AVQuery<AVObject> queryForCard = new AVQuery<>("TeamCard");
                    queryForCard.whereEqualTo("Stage", stage);
                    queryForCard.orderByAscending("createdAt");
                    queryForCard.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> list, AVException e) {
                            ArrayList<TeamCard> tempCardList = new ArrayList<>();
                            for (AVObject card : list) {
                                tempCardList.add(new TeamCard(stage.getString("TaskId"),
                                        card.getString("Executor"),
                                        card.getString("CardTitle")));
                            }
                            stageList.add(new Stage(tempCardList,
                                    stage.getString("Title"),
                                    stage.getObjectId(),
                                    stage.getInt("Index")));
                            if (stageList.size() == alist.size()) {
                                view.onComplete();
                                view.refresh(false);
                            }
                        }
                    });
                }
            }
        });
    }
}
