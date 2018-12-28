package com.kha.cbc.comfy.view.team

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.kennyc.bottomsheet.BottomSheet
import com.kennyc.bottomsheet.BottomSheetListener
import cn.leancloud.chatkit.LCChatKit
import cn.leancloud.chatkit.activity.LCIMConversationActivity
import cn.leancloud.chatkit.utils.LCIMConstants
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.data.network.providers.CustomUserProvider
import com.kha.cbc.comfy.model.Stage
import com.kha.cbc.comfy.presenter.Notification.CloudPushHelper
import com.kha.cbc.comfy.model.User
import com.kha.cbc.comfy.presenter.TeamDetailPresenter
import com.kha.cbc.comfy.view.common.BaseRefreshView
import com.kha.cbc.comfy.view.team.grouptrack.GroupTrackActivity
import com.tmall.ultraviewpager.UltraViewPager
import com.tmall.ultraviewpager.UltraViewPagerAdapter
import kotlinx.android.synthetic.main.activity_team_detail.*
import java.util.*

class TeamDetailActivity : AppCompatActivity(),
    TeamDetailView,
    BaseRefreshView,
    BottomSheetListener {

    lateinit var viewPager: UltraViewPager
    lateinit var presenter: TeamDetailPresenter
    lateinit var fragmentList: MutableList<StageFragment>
    lateinit var stageList: List<Stage>
    lateinit var taskTitle: String
    lateinit var taskObjectId: String


    override fun onChatReady(conversationId: String) {
        //弹出选择窗口
        val intent = Intent(this@TeamDetailActivity, LCIMConversationActivity::class.java)
        intent.putExtra(LCIMConstants.CONVERSATION_ID, conversationId)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        viewPager = stage_viewpager
        viewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL)

        setSupportActionBar(team_detail_toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val intent = intent
        val taskTitleView = task_detail_name
        taskTitle = intent.getStringExtra("taskTitle")
        taskObjectId = intent.getStringExtra("taskObjectId")
        taskTitleView.text = taskTitle

        presenter = TeamDetailPresenter(this)
        reload()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //TODO: Add menu
        menuInflater.inflate(R.menu.location_sharing, menu)
        menuInflater.inflate(R.menu.setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.location_sharing -> {
                val intent = Intent(this, GroupTrackActivity::class.java)
                intent.putExtra("taskObjectId", taskObjectId)
                startActivity(intent)
            }
            R.id.setting -> {
                BottomSheet.Builder(this)
                    .setSheet(R.menu.bottom_grid)
                    .grid()
                    .setListener(this)
                    .show()
            R.id.group_chat -> {
                presenter.initiateGroupChat(taskObjectId, CustomUserProvider.getInstance())
            }
        }
        return true
    }

    internal fun reload() {
        stageList = ArrayList()
        fragmentList = ArrayList()
        presenter.loadAllStages(taskObjectId, stageList)
    }

    override fun refresh(b: Boolean) {
        team_detail_progress_wheel.visibility = if (b) View.VISIBLE else View.GONE
    }

    override fun onComplete() {
        stageList = stageList.sortedBy { it.index }
        for (stage in stageList) {
            fragmentList.add(StageFragment.getInstance(stage.title,
                stage.teamCardList, taskObjectId, stage.objectId, stageList.size))
        }
        var stageFragment =
            StageFragment.getInstance(
                "plus",
                ArrayList(), taskObjectId, taskObjectId, stageList.size
            )
        fragmentList.add(stageFragment)
        var pagerAdapter = TeamDetailFragAdapter(
                supportFragmentManager, fragmentList
        )
        var ultraViewPagerAdapter = UltraViewPagerAdapter(pagerAdapter)
        viewPager.adapter = ultraViewPagerAdapter
        viewPager.initIndicator()
        viewPager.indicator.setOrientation(UltraViewPager.Orientation.HORIZONTAL)
            .setFocusColor(R.color.material_blue_grey_800)
            .setNormalColor(R.color.avoscloud_feedback_text_gray)
            .setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
        viewPager.indicator.setRadius(20)
            .setIndicatorPadding(40)
        viewPager.indicator.build()
    }

    override fun onSheetItemSelected(p0: BottomSheet, item: MenuItem?, p2: Any?) {
        when (item!!.itemId) {
            R.id.edit_item -> {
                MaterialDialog(this)
                    .input { materialDialog, charSequence ->
                        presenter.editTask(taskObjectId, charSequence.toString())
                        CloudPushHelper.pushUpdateOnTask(taskObjectId)
                        val taskTitleView = task_detail_name
                        taskTitleView.text = charSequence.toString()
                    }
                    .positiveButton()
                    .negativeButton()
                    .show()
            }

            R.id.delete_item -> {
                MaterialDialog(this)
                    .title(text = "确认删除该项目")
                    .message(text = "删除该项目将删除其中所有任务")
                    .positiveButton {
                        CloudPushHelper.pushOperationOnTask(taskObjectId, false)
                        val intent = Intent()
                        intent.putExtra("taskObjectId", taskObjectId)
                        setResult(Activity.RESULT_CANCELED, intent)
                        finish()
                    }
                    .negativeButton()
                    .show()
            }

            R.id.complete_item -> {
                MaterialDialog(this)
                    .title(text = "确认完成该项目")
                    .message(text = "完成该项目将完成其中所有任务")
                    .positiveButton {
                        CloudPushHelper.pushOperationOnTask(taskObjectId, true)
                        val intent = Intent()
                        intent.putExtra("taskObjectId", taskObjectId)
                        setResult(Activity.RESULT_CANCELED, intent)
                        finish()
                    }
                    .negativeButton()
                    .show()
            }
        }
    }

    override fun onSheetDismissed(p0: BottomSheet, p1: Any?, p2: Int) {
    }

    override fun onSheetShown(p0: BottomSheet, p1: Any?) {
    }
}
