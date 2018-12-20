package com.kha.cbc.comfy.view.team

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.model.Stage
import com.kha.cbc.comfy.presenter.TeamDetailPresenter
import com.kha.cbc.comfy.view.common.BaseRefreshView
import com.kha.cbc.comfy.view.team.grouptrack.GroupTrackActivity
import com.tmall.ultraviewpager.UltraViewPager
import com.tmall.ultraviewpager.UltraViewPagerAdapter
import kotlinx.android.synthetic.main.activity_team_detail.*
import java.util.*

class TeamDetailActivity : AppCompatActivity(), TeamDetailView, BaseRefreshView {

    lateinit var bar: ProgressBar
    lateinit var viewPager: UltraViewPager
    lateinit var presenter: TeamDetailPresenter
    lateinit var fragmentList: MutableList<StageFragment>
    lateinit var stageList: List<Stage>
    lateinit var taskTitle: String
    lateinit var taskObjectId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        bar = loading_progressBar
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
        menuInflater.inflate(R.menu.location_sharing, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.location_sharing -> {
                val intent = Intent(this, GroupTrackActivity::class.java)
                intent.putExtra("taskObjectId", taskObjectId)
                startActivity(intent)
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
        bar.visibility = if (b) View.VISIBLE else View.GONE
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
}
