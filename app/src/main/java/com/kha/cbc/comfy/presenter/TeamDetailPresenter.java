package com.kha.cbc.comfy.presenter;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.yiiguxing.compositionavatar.CompositionAvatarView;
import com.avos.avoscloud.*;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.kha.cbc.comfy.data.network.providers.CustomUserProvider;
import com.kha.cbc.comfy.model.Stage;
import com.kha.cbc.comfy.model.TeamCard;
import com.kha.cbc.comfy.model.User;
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

    public void initiateGroupChat(String taskId, CustomUserProvider provider){
        AVQuery<AVObject> queryForMember = new AVQuery<>("UserTaskMap");
        AVObject task = AVObject.createWithoutData("TeamTask", taskId);
        queryForMember.whereEqualTo("TeamTask", task);
        queryForMember.include("Member");
        queryForMember.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> memberMaplist, AVException e) {
                ArrayList<String> userNameList = new ArrayList<>();
                provider.getAllUsers().clear();
                for(AVObject memberMap: memberMaplist){
                    AVObject member = memberMap.getAVObject("Member");
                    provider.getAllUsers().add(new LCChatKitUser(member.getString("username"),
                            member.getString("username"), member.getString("avatarUrl")));
                    userNameList.add(member.getString("username"));
                }
                LCChatKit.getInstance().open(User.INSTANCE.getUsername(), new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient avimClient, AVIMException e) {
                        AVQuery<AVObject> queryForTask = new AVQuery<>("TeamTask");
                        queryForTask.whereEqualTo("objectId", taskId);
                        queryForTask.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                Boolean update = false;
                                if(list != null && list.size() > 0){
                                    for(AVObject memberMap: memberMaplist){
                                        if(memberMap.getUpdatedAt().after(list.get(0).getUpdatedAt())){
                                            update = true;
                                        }
                                    }
                                    if(update){
                                        avimClient.createConversation(userNameList, "", null,
                                                false, true,
                                                new AVIMConversationCreatedCallback() {
                                                    @Override
                                                    public void done(AVIMConversation avimConversation,
                                                                     AVIMException e) {
                                                        list.get(0).put("conversationId",
                                                                avimConversation.getConversationId());
                                                        list.get(0).saveInBackground(new SaveCallback() {
                                                            @Override
                                                            public void done(AVException e) {
                                                                view.onChatReady(avimConversation.getConversationId());
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                    else{
                                        view.onChatReady(list.get(0).getString("conversationId"));
                                    }
                                }
                            }
                        });
                    }
                });

            }
        });

    }

    //TODO:！！！扩展
    public void loadMembers(String taskId){
        AVQuery<AVObject> queryForMember = new AVQuery<>("UserTaskMap");
        AVObject task = AVObject.createWithoutData("TeamTask", taskId);
        queryForMember.whereEqualTo("TeamTask", task);
        queryForMember.include("Member");
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
                    queryForCard.include("Executor");
                    queryForCard.whereEqualTo("Stage", stage);
                    queryForCard.orderByAscending("createdAt");
                    queryForCard.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> list, AVException e) {
                            ArrayList<TeamCard> tempCardList = new ArrayList<>();
                            for (AVObject card : list) {
                                tempCardList.add(new TeamCard(stage.getString("TaskId"),
                                        card.getAVObject("Executor").getString("username"),
                                        card.getString("CardTitle"),
                                        card.getObjectId(),
                                        card.getString("Description")));
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

    public void editTask(String taskObjectId, String newTitle) {
        AVObject stage = AVObject.createWithoutData("TeamTask", taskObjectId);
        stage.put("TaskTitle", newTitle);
        stage.saveInBackground();
    }
}
