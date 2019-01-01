package com.kha.cbc.comfy.view.team

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.kennyc.bottomsheet.BottomSheet
import com.kennyc.bottomsheet.BottomSheetListener
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.model.TeamCard
import com.kha.cbc.comfy.presenter.Notification.CloudPushHelper
import com.kha.cbc.comfy.presenter.StageFragPresenter
import com.kha.cbc.comfy.view.common.BaseRefreshView
import com.kha.cbc.comfy.view.plus.PlusCardActivity
import kotlinx.android.synthetic.main.team_plus_frag.view.*
import java.util.*

/**
 * Created by ABINGCBC
 * on 2018/11/24
 */
class StageFragment : Fragment(), BaseRefreshView, BottomSheetListener, StageFragView{


    override fun onChatReady(conversationId: String) {}

    override fun onComplete() {}

    override fun refresh(b: Boolean) {
        activity!!.recreate()
    }

    private var teamCardList: List<TeamCard>? = null
    private lateinit var stageTitle: String
    var presenter: StageFragPresenter = StageFragPresenter(this)
    private var taskObjectId: String? = null
    private var stageObjectId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val bundle = arguments
        stageTitle = bundle!!.getString("stageName")!!
        taskObjectId = bundle.getString("taskObjectId")
        stageObjectId = bundle.getString("stageObjectId")
        val view: View
        if (stageTitle === "plus") {
            var index = bundle.getInt("index")
            view = inflater.inflate(R.layout.team_plus_frag, container, false)
            val button = view.team_plus_button
            button.setOnClickListener {
                val materialDialog = MaterialDialog(context!!)
                materialDialog.input { _, charSequence ->
                    presenter.postStage(taskObjectId, charSequence.toString(), index)
                }.positiveButton(R.string.submit)
                    .show()
            }
        } else {
            view = inflater.inflate(R.layout.stage_fragment, container, false)
            teamCardList = bundle.getParcelableArrayList("TeamCardList")

            var stageRecyclerView = view.findViewById(R.id.stage_recycler) as RecyclerView
            stageRecyclerView.layoutManager = LinearLayoutManager(this.context)
            stageRecyclerView.adapter = StageRecyclerAdapter(teamCardList, this, taskObjectId)


            var stageNameView = view.findViewById(R.id.stage_name) as TextView
            stageNameView.text = stageTitle + " · " + teamCardList!!.size.toString()

            var teamPlusCardView = view.findViewById(R.id.team_plus_card) as TextView
            teamPlusCardView.setOnClickListener{
                var intent = Intent(activity, PlusCardActivity::class.java)
                intent.putExtra("type", 1)
                intent.putExtra("stageObjectId", stageObjectId)
                intent.putExtra("taskObjectId", taskObjectId)
                startActivityForResult(intent, 1)
            }

            var editStageView = view.findViewById(R.id.edit_stage) as ImageButton
            editStageView.setOnClickListener {
                BottomSheet.Builder(this.context)
                    .setSheet(R.menu.bottom_grid)
                    .grid()
                    .setListener(this)
                    .show()
            }

        }

        return view
    }

    fun cardListSizeChange(size: Int) {
        var stageNameView = view!!.findViewById(R.id.stage_name) as TextView
        stageNameView.text = stageTitle + " · " + size.toString()
    }


    override fun onSheetItemSelected(p0: BottomSheet, item: MenuItem?, p2: Any?) {
        when (item!!.itemId) {
            R.id.edit_item -> {
                MaterialDialog(this.context!!)
                    .input { materialDialog, charSequence ->
                        presenter.editStage(stageObjectId, charSequence.toString())
                        CloudPushHelper.pushUpdateOnStage(taskObjectId)
                    }
                    .positiveButton()
                    .negativeButton()
                    .show()
            }

            R.id.delete_item -> {
                MaterialDialog(this.context!!)
                    .title(text = "确认删除该任务列表")
                    .message(text = "删除该任务列表将删除其中所有任务")
                    .positiveButton() {
                        presenter.deleteStage(stageObjectId)
                        CloudPushHelper.pushOperationOnStage(taskObjectId, stageTitle, false)
                    }
                    .negativeButton()
                    .show()
            }

            R.id.complete_item -> {
                MaterialDialog(this.context!!)
                    .title(text = "确认完成该任务列表")
                    .message(text = "完成该任务列表将完成其中所有任务")
                    .positiveButton() {
                        presenter.deleteStage(stageObjectId)
                        CloudPushHelper.pushOperationOnStage(taskObjectId, stageTitle, true)
                    }
                    .negativeButton()
                    .show()
            }
        }
    }

    override fun reload() {
        activity!!.recreate()
    }

    override fun onSheetDismissed(p0: BottomSheet, p1: Any?, p2: Int) {
        Log.d("bottom sheet", "dismiss")
    }

    override fun onSheetShown(p0: BottomSheet, p1: Any?) {
        Log.d("bottom sheet", "success")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            1 -> if (resultCode == RESULT_OK) {
                activity!!.recreate()
            }
        }
    }

    companion object {

        internal fun getInstance(stageName: String, list: ArrayList<TeamCard>,
                                 taskObjectId: String, stageObjectId: String,
                                 index: Int): StageFragment {
            val stageFragment = StageFragment()
            val bundle = Bundle()
            bundle.putString("stageName", stageName)
            bundle.putParcelableArrayList("TeamCardList", list)
            bundle.putString("stageObjectId", stageObjectId)
            bundle.putString("taskObjectId", taskObjectId)
            bundle.putInt("index", index)
            stageFragment.arguments = bundle
            return stageFragment
        }
    }

}
