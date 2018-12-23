package com.kha.cbc.comfy.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.kha.cbc.comfy.model.common.BaseCardModel;

/**
 * Created by ABINGCBC
 * on 2018/11/24
 */
public class TeamCard extends BaseCardModel implements Parcelable {

    String executor;
    String objectId;

    TeamCard(String taskId) {
        super(taskId);
    }

    public TeamCard(String taskId, String executor, String title, String objectId) {
        super(taskId);
        this.title = title;
        this.executor = executor;
        this.objectId = objectId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(executor);
        dest.writeString(objectId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeamCard> CREATOR = new Creator<TeamCard>() {
        @Override
        public TeamCard createFromParcel(Parcel in) {
            TeamCard teamCard = new TeamCard(null);
            teamCard.title = in.readString();
            teamCard.executor = in.readString();
            teamCard.objectId = in.readString();
            return teamCard;
        }

        @Override
        public TeamCard[] newArray(int size) {
            return new TeamCard[size];
        }
    };

    public String getExecutor() {
        return executor;
    }

    public String getObjectId() {
        return objectId;
    }
}
