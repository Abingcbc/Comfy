package com.kha.cbc.comfy.view.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.kha.cbc.comfy.ComfyApp;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.entity.GDPersonalTask;
import com.kha.cbc.comfy.model.PersonalTask;
import com.kha.cbc.comfy.greendao.gen.DaoSession;
import com.kha.cbc.comfy.greendao.gen.GDPersonalTaskDao;
import com.kha.cbc.comfy.view.plus.PersonalPlusCardActivity;
import com.kha.cbc.comfy.view.plus.PersonalPlusTaskActivity;
import com.loopeer.cardstack.AllMoveDownAnimatorAdapter;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.UpDownAnimatorAdapter;
import com.loopeer.cardstack.UpDownStackAnimatorAdapter;

import java.util.LinkedList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by ABINGCBC
 * on 2018/11/17
 */

public class PersonalFragment extends Fragment
        implements CardStackView.ItemExpendListener, PersonalFragView{


    CardStackView cardStackView;
    List<PersonalTask> taskList;
    PersonalTaskAdapter personalTaskAdapter;
    List<Integer> backColor;
    View view;
    PersonalFragPresenter presenter = new PersonalFragPresenter(this);
    GDPersonalTaskDao taskDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.personal_fragment, container, false);
        init();
        return view;
    }

    void init() {
        backColor = new LinkedList<>();
        cardStackView = view.findViewById(R.id.cardStackView);
        personalTaskAdapter = new PersonalTaskAdapter(getContext(), cardStackView, this);
        cardStackView.setAdapter(personalTaskAdapter);
        cardStackView.setItemExpendListener(this);
        //动画效果
        cardStackView.setAnimatorAdapter(new AllMoveDownAnimatorAdapter(cardStackView));
        cardStackView.setAnimatorAdapter(new UpDownAnimatorAdapter(cardStackView));
        cardStackView.setAnimatorAdapter(new UpDownStackAnimatorAdapter(cardStackView));
        presenter.loadAllTasksFromDB(taskDao);
    }

    @Override
    public void onItemExpend(boolean expend) {
    }

    public static PersonalFragment getInstance(GDPersonalTaskDao taskDao) {
        PersonalFragment fragment = new PersonalFragment();
        fragment.taskDao = taskDao;
        return fragment;
    }

    public void update() {
        presenter.loadAllTasksFromDB(taskDao);
    }

    public void plusTask() {
        if (cardStackView.isExpending()) {
            cardStackView.clearSelectPosition();
        }
        Intent intent = new Intent(getContext(), PersonalPlusTaskActivity.class);
        startActivityForResult(intent, 1);
    }

    public void plusCard(String taskId) {
        if (cardStackView.isExpending()) {
            cardStackView.clearSelectPosition();
        }
        Intent intent = new Intent(getContext(), PersonalPlusCardActivity.class);
        intent.putExtra("taskId", taskId);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        DaoSession daoSession = ((ComfyApp) getActivity().getApplication()).getDaoSession();
        GDPersonalTaskDao taskDao = daoSession.getGDPersonalTaskDao();
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //1为添加task
            case 1:
                if (resultCode == RESULT_OK) {
                    presenter.loadAllTasksFromDB(taskDao);
                }
                break;
            case 2:
                //成功添加card
                if (resultCode == RESULT_OK) {
                    presenter.loadAllTasksFromDB(taskDao);
                }
                //取消添加card
                else if (resultCode == RESULT_CANCELED) {
                }
                break;
        }
    }

    @Override
    public void OnLoadAllFromDBSuccess(List<PersonalTask> taskList) {
            GDPersonalTaskDao taskDao = ((ComfyApp) getActivity().getApplication())
                    .getDaoSession().getGDPersonalTaskDao();
            List<GDPersonalTask> personalTasks = taskDao.queryBuilder().list();
            taskList = new LinkedList<>();
            personalTaskAdapter = new PersonalTaskAdapter(getContext(),
                    cardStackView, this);
            if (personalTasks.isEmpty())
                return;
            else {
                for (GDPersonalTask task : personalTasks) {
                    taskList.add(new PersonalTask(task));
                }
                //TODO:这里的backColor是固定的，到后期确定涂色表要进行添加
                backColor = new LinkedList<>();
                for (int i = 0; i < taskList.size(); i++) {
                    backColor.add(R.color.avoscloud_blue);
                }
                cardStackView.setAdapter(personalTaskAdapter);
                personalTaskAdapter.updateData(backColor, taskList);
            }
    }
}
