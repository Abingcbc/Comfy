package com.kha.cbc.comfy.view.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.model.TeamCard
import com.kha.cbc.comfy.presenter.StageFragPresenter
import java.util.*

/**
 * Created by ABINGCBC
 * on 2018/11/24
 */
class StageFragment : Fragment() {

    private var teamCardList: List<TeamCard>? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var stageTitle: String
    var presenter: StageFragPresenter = StageFragPresenter()
    private var objectId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val bundle = arguments
        stageTitle = bundle!!.getString("stageName")
        objectId = bundle!!.getString("objectId")
        val view: View
        if (stageTitle === "plus") {
            view = inflater.inflate(R.layout.team_plus_frag, container, false)
            val button = view.findViewById<Button>(R.id.team_plus_button)
            button.setOnClickListener {
                val materialDialog = MaterialDialog(context!!)
                materialDialog.input { _, charSequence ->
                    presenter.postStage(objectId, charSequence.toString())
                }.positiveButton(R.string.submit)
                    .show()
            }
        } else {
            view = inflater.inflate(R.layout.stage_fragment, container, false)
            val textView = view.findViewById<TextView>(R.id.stage_name)
            recyclerView = view.findViewById(R.id.stage_recycler)
            textView.text = stageTitle
            teamCardList = bundle.getParcelableArrayList("TeamCardList")
        }

        return view
    }

    companion object {

        internal fun getInstance(stageName: String, list: ArrayList<TeamCard>, objectId: String): StageFragment {
            val stageFragment = StageFragment()
            val bundle = Bundle()
            bundle.putString("stageName", stageName)
            bundle.putParcelableArrayList("TeamCardList", list)
            bundle.putString("objectId", objectId);
            stageFragment.arguments = bundle
            return stageFragment
        }
    }
}
