package com.kha.cbc.comfy.view.personal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import com.kha.cbc.comfy.ComfyApp;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.entity.GDPersonalCard;
import com.kha.cbc.comfy.entity.GDPersonalTask;
import com.kha.cbc.comfy.greendao.gen.DaoSession;
import com.kha.cbc.comfy.greendao.gen.GDPersonalCardDao;
import com.kha.cbc.comfy.greendao.gen.GDPersonalTaskDao;
import com.kha.cbc.comfy.model.PersonalCard;
import com.kha.cbc.comfy.model.PersonalTask;
import com.kha.cbc.comfy.presenter.Notification.AlarmHelper;
import com.kha.cbc.comfy.presenter.PersonalFragPresenter;
import com.kha.cbc.comfy.view.plus.PlusCardActivity;
import com.kha.cbc.comfy.view.plus.PlusTaskActivity;
import com.loopeer.cardstack.AllMoveDownAnimatorAdapter;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.UpDownAnimatorAdapter;
import com.loopeer.cardstack.UpDownStackAnimatorAdapter;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PersonalFragment extends Fragment
        implements CardStackView.ItemExpendListener, PersonalFragView {


    private CardStackView cardStackView;
    List<PersonalTask> taskList;
    private PersonalTaskAdapter personalTaskAdapter;
    private List<Integer> backColor;
    private View view;
    private PersonalFragPresenter presenter = new PersonalFragPresenter(this);
    private GDPersonalTaskDao taskDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.personal_fragment, container, false);
        init();
        return view;
    }

    void init() {
        backColor = new LinkedList<>();
        cardStackView = view.findViewById(R.id.cardStackView);
        personalTaskAdapter = new PersonalTaskAdapter(Objects.requireNonNull(getContext()), cardStackView, this, getActivity());
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
        Intent intent = new Intent(getContext(), PlusTaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", 0);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    public void plusCard(String taskId) {
        if (cardStackView.isExpending()) {
            cardStackView.clearSelectPosition();
        }
        Intent intent = new Intent(getContext(), PlusCardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", 0);
        bundle.putString("taskId", taskId);
        intent.putExtras(bundle);
        startActivityForResult(intent, 2);
    }

    public void plusCard(PersonalCard card) {
        if (cardStackView.isExpending()) {
            cardStackView.clearSelectPosition();
        }
        Intent intent = new Intent(getContext(), PlusCardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", 2);
        bundle.putString("taskId", card.getTaskId());
        bundle.putString("cardId", card.getId());
        intent.putExtras(bundle);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        DaoSession daoSession = ((ComfyApp) Objects.requireNonNull(getActivity()).getApplication()).getDaoSession();
        GDPersonalTaskDao taskDao = daoSession.getGDPersonalTaskDao();
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
    public void onLoadAllFromDBSuccess(List<PersonalTask> taskList) {

        personalTaskAdapter = new PersonalTaskAdapter(Objects.requireNonNull(getContext()),
                cardStackView, this, Objects.requireNonNull(getActivity()));
        ImageView imageView = view.findViewById(R.id.empty_image);
        TextView textView = view.findViewById(R.id.empty_text);
        if (taskList.isEmpty()) {
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
        else {
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            //TODO:这里的backColor是固定的，到后期确定涂色表要进行添加
            backColor = new LinkedList<>();
            for (int i = 0; i < taskList.size(); i++) {
                backColor.add(R.color.avoscloud_blue);
            }
            cardStackView.setAdapter(personalTaskAdapter);
            personalTaskAdapter.updateData(backColor, taskList);
        }
    }

    @Override
    public void onLoadAllFromDBError(Throwable e) {
        e.printStackTrace();
    }

    public void onDeleteItemInDB (PersonalCard card) {
        GDPersonalCardDao cardDao = ((ComfyApp) Objects.requireNonNull(getActivity()).getApplication())
                .getDaoSession().getGDPersonalCardDao();
        GDPersonalCard gdPersonalCard = new GDPersonalCard(card);
        if (gdPersonalCard.getIsRemind()) {
            AlarmHelper.deleteLocalReminder(gdPersonalCard);
        }
        cardDao.delete(gdPersonalCard);
    }

    public void deleteTaskFromDB(PersonalTask task) {
        GDPersonalTaskDao taskDao = ((ComfyApp) Objects.requireNonNull(getActivity()).getApplication())
                .getDaoSession().getGDPersonalTaskDao();
        taskDao.queryBuilder().where(GDPersonalTaskDao.Properties.Id.eq(task.getId())).
                buildDelete().executeDeleteWithoutDetachingEntities();
        GDPersonalCardDao cardDao = ((ComfyApp) getActivity().getApplication())
                .getDaoSession().getGDPersonalCardDao();
        List<GDPersonalCard> cardList = cardDao.queryBuilder().
                where(GDPersonalCardDao.Properties.TaskId.eq(task.getId())).list();
        for (GDPersonalCard card : cardList) {
            if (card.getIsRemind()) {
                AlarmHelper.deleteLocalReminder(card);
            }
        }
        cardDao.queryBuilder().
                where(GDPersonalCardDao.Properties.TaskId.eq(task.getId())).
                buildDelete().executeDeleteWithoutDetachingEntities();
        presenter.loadAllTasksFromDB(taskDao);
    }

    public void onUpdateTask (PersonalTask task, String taskId) {
        GDPersonalTaskDao taskDao = ((ComfyApp) getActivity().getApplication())
            .getDaoSession().getGDPersonalTaskDao();
        GDPersonalTask gdPersonalTask = taskDao.queryBuilder().
                where(GDPersonalTaskDao.Properties.Id.eq(taskId)).unique();
        gdPersonalTask.setTitle(task.getTitle());
        presenter.loadAllTasksFromDB(taskDao);
    }

    public void onCompleteCard (PersonalCard card) {
        onDeleteItemInDB(card);
        Snackbar.make(view, "恭喜你 完成任务！", Snackbar.LENGTH_SHORT).show();
    }
}
