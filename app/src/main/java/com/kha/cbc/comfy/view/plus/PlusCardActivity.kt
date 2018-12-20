package com.kha.cbc.comfy.view.plus

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.bumptech.glide.Glide
import com.kha.cbc.comfy.ComfyApp
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.view.common.*
import com.kha.cbc.comfy.entity.GDPersonalCard
import com.kha.cbc.comfy.greendao.gen.GDAvatarDao
import com.kha.cbc.comfy.model.User
import com.kha.cbc.comfy.presenter.PlusCardPresenter
import com.kha.cbc.comfy.view.common.AvatarView
import com.kha.cbc.comfy.view.common.BaseActivityWithPresenter
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_personal_plus_card.*
import kotlinx.android.synthetic.main.content_personal_plus_card.*
import java.text.SimpleDateFormat
import java.util.*
import com.kha.cbc.comfy.greendao.gen.GDPersonalCardDao
import com.kha.cbc.comfy.presenter.Notification.AlarmHelper
import kotlinx.android.synthetic.main.content_personal_plus_card.view.*


/**
 * Created by ABINGCBC
 * on 2018/11/4
 */

class PlusCardActivity : BaseActivityWithPresenter(), PlusCardView,
    TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener, AvatarView {

    override lateinit var avatarDao: GDAvatarDao

    override var presenter = PlusCardPresenter(this)
    lateinit var taskId: String
    lateinit var cardId: String
    internal var type: Int = 0
    private lateinit var executorName: String
    private lateinit var executorObjectId: String
    private lateinit var stageObjectId: String
    private lateinit var taskObjectId: String
    private lateinit var preTitle: String
    private var reminderDate = Calendar.getInstance().time
    private var isReminderSet = false
    private var DATEFORMAT = "d MMM, yyyy"
    private var DATEANDTIMEFORMAT = "d MMM, yyyy, h:mm a"
    private var TIMEFORMAT = "h:mm a"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_plus_card)

        setSupportActionBar(personal_plus_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        personal_plus_toolbar.setNavigationOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }

        val intentType = intent
        val bundle = intentType.extras
        type = bundle!!.getInt("type")

        /**
         * 0为新增个人任务
         * 1为新增团队任务
         * 2为修改个人任务
         * 3为修改团队任务
         */
        when (type) {
            0 ->
                taskId = bundle.getString("taskId")
            1 -> {
                stageObjectId = bundle.getString("stageObjectId")
                taskObjectId = bundle.getString("taskObjectId")
                executor_assign.visibility = View.VISIBLE
                assign_button.setOnClickListener {
                    val materialDialog = MaterialDialog(this)
                    materialDialog.input { _, charSequence ->
                        executorName = charSequence.toString()
                        presenter.queryMember(charSequence.toString())
                    }.positiveButton(text = "确认")
                        .title(text = "输入要添加成员的名称")
                        .show()
                }
            }
            2 -> {
                taskId = bundle.getString("taskId")
                cardId = bundle.getString("cardId")
                initialSavedCard(cardId)
            }
            3 -> {

            }

        }
        executorObjectId = User.comfyUserObjectId!!

//        presenter.setListeners(personal_plus_card_layout)
        reminderSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enterDateLinearLayout.visibility = View.VISIBLE
            } else {
                enterDateLinearLayout.visibility = View.INVISIBLE
            }
        }
        reminderDateEditText.setOnClickListener {
            val now = Calendar.getInstance()
            val dateChooseDialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            //字体颜色和按钮颜色一样。。。
            dateChooseDialog.setOkColor(resources.getColor(R.color.black))
            dateChooseDialog.setCancelColor(resources.getColor(R.color.black))
            dateChooseDialog.show(supportFragmentManager, "DateChooseDialog")
        }
        reminderTimeEditText.setOnClickListener {
            val now = Calendar.getInstance()
            val timeChooseDialog = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
            )
            timeChooseDialog.setOkColor(resources.getColor(R.color.black))
            timeChooseDialog.setCancelColor(resources.getColor(R.color.black))
            timeChooseDialog.show(supportFragmentManager, "TimeChooseDialog")
        }
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        val calendar = Calendar.getInstance()
        if (reminderDate != null) {
            calendar.time = reminderDate
        }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.set(year, month, dayOfMonth, hourOfDay, minute, second)
        reminderDate = calendar.time
        setTimeText()
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        if (reminderDate != null) {
            calendar.time = reminderDate
        }
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        calendar.set(year, monthOfYear, dayOfMonth, hourOfDay, minute, second)
        reminderDate = calendar.time
        setDateText()
    }

    private fun dateToString(formatString: String) : String {
        val simpleDateFormat = SimpleDateFormat(formatString)
        return simpleDateFormat.format(reminderDate)
    }

    private fun stringToDate(dateInString: String) {
        //只会出现将完整日期转换的情况
        var formatter = SimpleDateFormat(DATEANDTIMEFORMAT)
        reminderDate = formatter.parse(dateInString)
    }

    private fun setDateText() {
        if (reminderDate > Calendar.getInstance().time) {
            reminderTimeTextView.text =
                    "将在" + dateToString(DATEANDTIMEFORMAT) + "提醒您"
            isReminderSet = true
        } else {
            reminderTimeTextView.text = "不能将提醒设在过去"
            if (reminderDateEditText.text != null)
                reminderDateEditText.text!!.clear()
            isReminderSet = false
        }
        reminderDateEditText.setText(dateToString(DATEFORMAT))
    }

    private fun setTimeText() {
        if (reminderDate > Calendar.getInstance().time) {
            reminderTimeTextView.text =
                    "将在" + dateToString(DATEANDTIMEFORMAT) + "提醒您"
            isReminderSet = true
        } else {
            reminderTimeTextView.text = "不能将提醒设在过去"

            if (reminderTimeEditText.text != null)
                reminderTimeEditText.text!!.clear()
            isReminderSet = false
        }
        reminderTimeEditText.setText(dateToString(TIMEFORMAT))
    }

    //绑定完成按钮到toolbar中
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.plus_card, menu)
        return true
    }

    private fun successAddPersonal(title: String, description:String) {
        val cardDao = (application as ComfyApp)
            .daoSession.gdPersonalCardDao
        if (isReminderSet) {
            val personalCard = GDPersonalCard(title, description,
                taskId, dateToString(DATEANDTIMEFORMAT))
            cardDao.insertOrReplace(personalCard)
            AlarmHelper.getNewLocalReminder(personalCard, this, reminderDate)
        }
        else
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
    }

    private fun successAddTeam(title: String, description: String) {
        presenter.postCard(title, description, executorObjectId, stageObjectId, taskObjectId)
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun successUpdatePersonal(title: String, description: String) {
        val cardDao = (application as ComfyApp)
            .daoSession.gdPersonalCardDao
        val card = cardDao.queryBuilder().
            where(GDPersonalCardDao.Properties.Id.eq(cardId)).unique()
        if (isReminderSet) {
            card.isRemind = true
            card.remindDate = dateToString(DATEANDTIMEFORMAT)
            card.title = title
            card.description = description
        } else {
            card.isRemind = false
            card.remindDate = null
            card.title = title
            card.description = description
        }
        cardDao.update(card)
        AlarmHelper.updateLocalReminder(card, this, reminderDate)
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
    }

    private fun successUpdateTeam(title: String, description: String){}

    private fun initialSavedCard(cardId: String) {
        var personalCard = presenter.getLocalPersonalCard(cardId, application)
        input_card_title.setText(personalCard.title)
        input_card_description.setText(personalCard.description)
        preTitle = personalCard.title
        if (personalCard.isRemind) {
            reminderSwitch.isChecked = true
            enterDateLinearLayout.visibility = View.VISIBLE
            stringToDate(personalCard.remindDate)
            setDateText()
            setTimeText()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.plus_success -> {
                val newTitle = input_card_title.text.toString()
                val newDescription = input_card_description.text.toString()
                when (type) {
                    0 -> successAddPersonal(newTitle, newDescription)
                    1 -> successAddTeam(newTitle, newDescription)
                    2 -> successUpdatePersonal(newTitle, newDescription)
                    3 -> successUpdateTeam(newTitle, newDescription)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun uploadAvatarFinish(url: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun downloadAvatarFinish(urlPairs: MutableList<Pair<String, String>>) {
        if (urlPairs.isEmpty()) {
            personal_plus_card_layout.yum("Mistake").show()
        }
        executorName = urlPairs[0].first
        Glide.with(this).load(urlPairs[0].second).into(member_portrait)
    }

    override fun uploadProgressUpdate(progress: Int?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setProgressBarVisible() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun downloadAvatarFinish(url: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
