package com.kha.cbc.comfy.view.team

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.model.TeamCard
import com.kha.cbc.comfy.presenter.StageFragPresenter
import com.kha.cbc.comfy.view.common.BaseRefreshView
import com.kha.cbc.comfy.view.plus.PlusCardActivity
import kotlinx.android.synthetic.main.stage_fragment.view.*
import kotlinx.android.synthetic.main.team_plus_frag.view.*
import java.util.*

/**
 * Created by ABINGCBC
 * on 2018/11/24
 */
class StageFragment : Fragment(), BaseRefreshView{
    override fun onComplete() {

    }

    override fun refresh(b: Boolean) {
        activity!!.recreate()
    }

    private var stageList: List<TeamCard>? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var stageTitle: String
    var presenter: StageFragPresenter = StageFragPresenter(this)
    //当非plus页时为stage
    private var objectId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val bundle = arguments
        stageTitle = bundle!!.getString("stageName")!!
        objectId = bundle.getString("objectId")
        val view: View
        if (stageTitle === "plus") {
            var index = bundle.getInt("index")
            view = inflater.inflate(R.layout.team_plus_frag, container, false)
            val button = view.team_plus_button
            button.setOnClickListener {
                val materialDialog = MaterialDialog(context!!)
                materialDialog.input { _, charSequence ->
                    presenter.postStage(objectId, charSequence.toString(), index)
                }.positiveButton(R.string.submit)
                    .show()
            }
        } else {
            view = inflater.inflate(R.layout.stage_fragment, container, false)
            stageList = bundle.getParcelableArrayList("TeamCardList")
            val textView = view.stage_name

            recyclerView = view.stage_recycler
            recyclerView.layoutManager = LinearLayoutManager(this.context)
            recyclerView.adapter = StageRecyclerAdapter(stageList)

            textView.text = stageTitle
            var plusTextView = view.team_plus_card
            plusTextView.setOnClickListener{
                var intent = Intent(activity, PlusCardActivity::class.java)
                intent.putExtra("type", 1)
                intent.putExtra("stageObjectId", objectId)
//                intent.putExtra("taskObjectId", )
                startActivityForResult(intent, 1)
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            1 -> if (resultCode == RESULT_OK) {
                activity!!.recreate()
            }
        }
    }

    companion object {

        internal fun getInstance(stageName: String, list: ArrayList<TeamCard>, taskObjectId: String, index: Int): StageFragment {
            val stageFragment = StageFragment()
            val bundle = Bundle()
            bundle.putString("stageName", stageName)
            bundle.putParcelableArrayList("TeamCardList", list)
            bundle.putString("taskObjectId", taskObjectId)
            bundle.putInt("index", index)
            stageFragment.arguments = bundle
            return stageFragment
        }
    }

}
