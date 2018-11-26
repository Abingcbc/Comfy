package com.kha.cbc.comfy.view.team

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.model.Stage
import com.kha.cbc.comfy.presenter.TeamDetailPresenter
import com.kha.cbc.comfy.view.common.BaseRefreshView
import java.util.*

class TeamDetailActivity : AppCompatActivity(), TeamDetailView, BaseRefreshView {

    lateinit var bar: ProgressBar
    lateinit var viewPager: ViewPager
    lateinit var presenter: TeamDetailPresenter
    lateinit var fragmentList: MutableList<StageFragment>
    lateinit var stageList: List<Stage>
    lateinit var taskTitle: String
    lateinit var objectId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        bar = findViewById(R.id.loading_progressBar)
        viewPager = findViewById(R.id.stage_viewpager)
        val toolbar = findViewById<Toolbar>(R.id.team_detail_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val intent = intent
        val taskTitleView = findViewById<TextView>(R.id.task_detail_name)
        taskTitle = intent.getStringExtra("taskTitle")
        objectId = intent.getStringExtra("objectId")
        taskTitleView.text = taskTitle

        presenter = TeamDetailPresenter(this)

        reload()
    }

    internal fun reload() {
        stageList = ArrayList()
        fragmentList = ArrayList()
        presenter.loadAllStages(objectId, stageList)
    }

    override fun refresh(b: Boolean) {
        bar.visibility = if (b) View.VISIBLE else View.GONE
    }

    override fun onComplete() {
        for (stage in stageList) {
            fragmentList.add(StageFragment.getInstance(stage.title, stage.teamCardList, objectId))
        }
        fragmentList.add(
            StageFragment.getInstance(
                "plus",
                ArrayList(), objectId
            )
        )
        viewPager.adapter = TeamDetailFragAdapter(
            supportFragmentManager,
            fragmentList
        )
    }
}
