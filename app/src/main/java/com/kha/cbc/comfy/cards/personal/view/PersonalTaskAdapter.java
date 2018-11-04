package com.kha.cbc.comfy.cards.personal.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.cards.personal.model.PersonalTask;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;

import java.util.List;

/**
 * Created by CBC
 * on 2018/11/2
 */

public class PersonalTaskAdapter extends StackAdapter<Integer> {

    List<PersonalTask> personalTaskList;

    public PersonalTaskAdapter(Context context) {
        super(context);
    }

    public void updateData(List<Integer> data, List<PersonalTask> personalTaskList) {
        this.personalTaskList = personalTaskList;
        //通过基类调用notifyDataSetChanged()，才能有显示
        updateData(data);
    }

    @Override
    public void bindView(Integer data, int position, CardStackView.ViewHolder holder) {
        PersonalTaskViewHolder personalTaskViewHolder = (PersonalTaskViewHolder)holder;
        personalTaskViewHolder.onBind(data, position, personalTaskList);
    }

    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stack_task, parent, false);
        return new PersonalTaskViewHolder(view);
    }

    public class PersonalTaskViewHolder extends CardStackView.ViewHolder {

        View root;
        FrameLayout header;
        RecyclerView cardsList;
        TextView taskTitle;

        public PersonalTaskViewHolder(View view) {
            super(view);
            root = view;
            header = view.findViewById(R.id.task_header);
            cardsList = view.findViewById(R.id.cards_list);
            taskTitle = view.findViewById(R.id.task_title_text);
        }

        public void onBind(Integer backgroundColorId,int position,List<PersonalTask> dataList) {
            header.getBackground().setColorFilter(
                    ContextCompat.getColor(getContext(), backgroundColorId),
                    PorterDuff.Mode.SRC_IN);
            taskTitle.setText(dataList.get(position).getTitle());
            PersonalCardAdapter personalCardAdapter = new PersonalCardAdapter(dataList.get(position).getCards());
            cardsList.setLayoutManager(new LinearLayoutManager(getContext()));
            cardsList.setAdapter(personalCardAdapter);
        }

        @Override
        public void onItemExpand(boolean b) {
            cardsList.setVisibility(b ? View.VISIBLE : View.GONE);
        }
    }
}
