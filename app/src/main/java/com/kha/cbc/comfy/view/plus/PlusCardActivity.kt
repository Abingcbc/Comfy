package com.kha.cbc.comfy.view.plus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.avos.avoscloud.*
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
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
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ABINGCBC
 * on 2018/11/4
 */

class PlusCardActivity : BaseActivityWithPresenter(), PlusCardView,
    TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener, AvatarView {

    override lateinit var avatarDao: GDAvatarDao

    override var presenter = PlusCardPresenter(this)
    internal var taskId: String? = null
    internal var type: Int = 0
    private lateinit var executorName: String
    private lateinit var executorObjectId: String
    private lateinit var stageObjectId: String
    private lateinit var taskObjectId: String
    private var reminderDate = Calendar.getInstance().time
    private var isReminderSet = false

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
        if (type == 1) {
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
        } else {
            taskId = bundle.getString("taskId")
        }
        executorObjectId = User.comfyUserObjectId!!

//        presenter.setListeners(personal_plus_card_layout)
        reminderSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enterDateLinearLayout.visibility = View.VISIBLE
                isReminderSet = true
            } else {
                enterDateLinearLayout.visibility = View.INVISIBLE
                isReminderSet = false
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
            //dateChooseDialog.setOkText("OK")
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
            timeChooseDialog.setOkText("OK")
            timeChooseDialog.setCancelText("CANCEL")
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

    private fun formatDate(formatString: String) : String {
        val simpleDateFormat = SimpleDateFormat(formatString)
        return simpleDateFormat.format(reminderDate)
    }

    private fun setDateText() {
        val dateFormat = "d MMM, yyyy"
        val dateAndTimeFormat = "d MMM, yyyy, h:mm a"
        if (reminderDate > Calendar.getInstance().time) {
            reminderTimeTextView.text =
                    "将在" + formatDate(dateAndTimeFormat) + "提醒您"
            reminderDateEditText.setText(formatDate(dateFormat))
        } else {
            reminderTimeTextView.text = "不能将提醒设在过去"
            if (reminderDateEditText.text != null)
                reminderDateEditText.text!!.clear()
        }
    }

    private fun setTimeText() {
        val timeFormat = "h:mm a"
        val dateAndTimeFormat = "d MMM, yyyy, h:mm a"
        if (reminderDate > Calendar.getInstance().time) {
            reminderTimeTextView.text =
                    "将在" + formatDate(dateAndTimeFormat) + "提醒您"
            reminderTimeEditText.setText(formatDate(timeFormat))
        } else {
            reminderTimeTextView.text = "不能将提醒设在过去"

            if (reminderTimeEditText.text != null)
                reminderTimeEditText.text!!.clear()
        }
    }

    //绑定完成按钮到toolbar中
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.plus_card, menu)
        return true
    }

    private fun successAddPersonal(title: String, description:String) {
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
        presenter.setLocalReminder(reminderLayout)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.plus_success -> {
                val title = input_card_title.text.toString()
                val description = input_card_description.text.toString()
                if (type == 0) {
                    successAddPersonal(title, description)
                    if (isReminderSet) {
                        presenter.setLocalReminder(input_reminder_date)
                    }
                } else {
                    successAddTeam(title, description)
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
