package com.kha.cbc.comfy.view.personal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.model.common.BaseCardModel;
import com.loopeer.cardstack.CardStackView;

import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/2
 */

public class PersonalCardAdapter extends RecyclerView.Adapter {

    List<BaseCardModel> personalCardList;
    CardStackView cardStackView;
    PersonalFragment fragment;

    PersonalCardAdapter(List<BaseCardModel> personalCardList,
                        CardStackView cardStackView,
                        PersonalFragment fragment) {
        this.personalCardList = personalCardList;
        this.cardStackView = cardStackView;
        this.fragment = fragment;
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
                    fragment.plusCard(personalCardList.get(position).getTaskId());
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
