package com.kha.cbc.comfy.view.team;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.model.TeamCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/24
 */
public class StageFragment extends Fragment {

    List<TeamCard> teamCardList;
    String stageName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stage_fragment, container, false);
        TextView textView = view.findViewById(R.id.stage_name);
        textView.setText(savedInstanceState.getString("stageName"));
        return view;
    }

    static StageFragment getInstance(String stageName) {
        StageFragment stageFragment = new StageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stageName", stageName);
        stageFragment.setArguments(bundle);
        return stageFragment;
    }
}
