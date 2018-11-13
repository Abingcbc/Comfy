package com.kha.cbc.comfy.view.personal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.common.BaseCardModel;
import com.kha.cbc.comfy.view.plus.PersonalPlusCardActivity;
import com.loopeer.cardstack.CardStackView;

import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/2
 */

public class PersonalCardAdapter extends RecyclerView.Adapter {

    List<BaseCardModel> personalCardList;
    CardStackView cardStackView;

    PersonalCardAdapter(List<BaseCardModel> personalCardList, CardStackView cardStackView) {
        this.personalCardList = personalCardList;
        this.cardStackView = cardStackView;
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        PersonalCardViewHolder cardViewHolder = (PersonalCardViewHolder)holder;
        if (position != personalCardList.size() - 1) {
            cardViewHolder.name.setText(personalCardList.get(position).getTitle());
            cardViewHolder.description.setText(personalCardList.get(position).getDescription());
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    if (cardStackView.isExpending()) {
                        cardStackView.clearSelectPosition();
                    }
                    Intent intent = new Intent(context, PersonalPlusCardActivity.class);
                    intent.putExtra("taskId", personalCardList.get(position).getTaskId());
                    ((Activity)context).startActivityForResult(intent, 2);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return personalCardList.size();
    }

    public class PersonalCardViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView description;

        public PersonalCardViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.personal_card_name);
            description = itemView.findViewById(R.id.personal_card_description);
        }
    }
}
