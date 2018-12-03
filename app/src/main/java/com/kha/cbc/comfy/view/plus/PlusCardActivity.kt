package com.kha.cbc.comfy.view.plus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.avos.avoscloud.*
import com.kha.cbc.comfy.ComfyApp
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.entity.GDPersonalCard
import com.kha.cbc.comfy.entity.GDPersonalTask
import com.kha.cbc.comfy.greendao.gen.GDPersonalCardDao
import com.kha.cbc.comfy.greendao.gen.GDPersonalTaskDao
import com.kha.cbc.comfy.model.User
import com.kha.cbc.comfy.presenter.PlusCardPresenter
import com.kha.cbc.comfy.presenter.Presenter
import com.kha.cbc.comfy.view.common.BaseActivityWithPresenter

/**
 * Created by ABINGCBC
 * on 2018/11/4
 */

class PlusCardActivity : BaseActivityWithPresenter(), PlusCardView {

    override var presenter = PlusCardPresenter(this)
    internal var taskId: String? = null
    internal var type: Int = 0
    lateinit var executorName: String
    lateinit var executorObjectId: String
    lateinit var stageObjectId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_plus_card)

        val query = AVQuery<AVObject>("ComfyUser")
        query.whereEqualTo("username", User.username)
        val comfyUser = query.findInBackground(object: FindCallback<AVObject>(){
            override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                executorObjectId = p0!![0].objectId
            }
        })

        val toolbar = findViewById<Toolbar>(R.id.personal_plus_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }

        val intentType = intent
        val bundle = intentType.extras
        type = bundle!!.getInt("type")
        if (type == 1) {
            stageObjectId = bundle.getString("objectId")!!
            val button = findViewById<Button>(R.id.assign_button)
            button.setOnClickListener {
                val materialDialog = MaterialDialog(this)
                materialDialog.input { _, charSequence ->
                    executorName = charSequence.toString()
                    presenter.queryMember(charSequence.toString())
                }.positiveButton(R.string.submit)
                    .show()
            }
        } else {
            taskId = bundle.getString("taskId")
        }
    }

    //绑定完成按钮到toolbar中
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.plus_card, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.plus_success -> {
                val titleView = findViewById<TextView>(R.id.input_card_title)
                val descriptionView = findViewById<TextView>(R.id.input_card_description)
                val title = titleView.text.toString()
                val description = descriptionView.text.toString()
                if (type == 0) {
                    //TODO: 此时只在本地添加一条card，暂时觉得没有必要开多线程，但以后服务端同步要开
                    val cardDao = (application as ComfyApp)
                        .daoSession.gdPersonalCardDao
                    cardDao.insertOrReplace(GDPersonalCard(title, description, taskId))
                    val taskDao = (application as ComfyApp)
                        .daoSession.gdPersonalTaskDao
                    val personalTaskList = taskDao.loadAll()
                    //多表链接时，GreenDao不会实时更新
                    for (task in personalTaskList) {
                        task.resetPersonalCardList()
                    }
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    //TODO:默认执行者为用户自己
                    presenter.postCard(title, description, executorObjectId, stageObjectId)
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}
