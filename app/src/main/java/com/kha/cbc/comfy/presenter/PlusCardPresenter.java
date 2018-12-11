package com.kha.cbc.comfy.presenter;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import com.avos.avoscloud.*;
import com.google.android.material.textfield.TextInputEditText;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.view.common.AvatarView;
import com.kha.cbc.comfy.view.plus.PlusCardActivity;
import com.kha.cbc.comfy.view.plus.PlusCardView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/5
 */

public class PlusCardPresenter extends AvatarPresenter {

    PlusCardView plusCardView;

    public PlusCardPresenter(PlusCardView plusCardView) {
        super((AvatarView) plusCardView);
        this.plusCardView = plusCardView;
    }

    //将新增卡片信息上传至服务器
    public void postCard(String title,
                         String description,
                         String executorObjectId,
                         String stageObjectId) {
        AVObject card = new AVObject("TeamCard");
        AVObject user = AVObject.createWithoutData("ComfyUser",
                executorObjectId);
        card.put("Executor", user);
        AVObject stage = AVObject.createWithoutData("Stage",
                stageObjectId);
        card.put("Stage", stage);
        card.put("CardTitle", title);
        card.put("Description", description);
        card.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null)
                    e.printStackTrace();
            }
        });

        AVObject map = new AVObject("UserTaskMap");
    }


    public void setLocalReminder(LinearLayout linearLayout) {

    }

    public void setCloudReminder(LinearLayout linearLayout) {

    }

    public void queryMember(String memberName) {
        List<String> nameList = new ArrayList<>();
        nameList.add(memberName);
        super.loadAvatar(nameList);
    }
}
