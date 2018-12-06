package com.kha.cbc.comfy.view.plus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.avos.avoscloud.*
import com.google.android.material.textfield.TextInputEditText
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ABINGCBC
 * on 2018/11/4
 */

class PlusCardActivity : BaseActivityWithPresenter(), PlusCardView,
    TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {

    override var presenter = PlusCardPresenter(this)
    internal var taskId: String? = null
    internal var type: Int = 0
    lateinit var executorName: String
    lateinit var executorObjectId: String
    lateinit var stageObjectId: String
     var reminderDate = Calendar.getInstance().time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_plus_card)

//        val query = AVQuery<AVObject>("ComfyUser")
//        query.whereEqualTo("username", User.username)
//        val comfyUser = query.findInBackground(object: FindCallback<AVObject>(){
//            override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
//                executorObjectId = p0!![0].objectId
//            }
//        })

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
            stageObjectId = bundle.getString("objectId")
            val linearLayout = findViewById<LinearLayout>(R.id.enterDateLinearLayout)
            linearLayout.visibility = View.VISIBLE

            val button = findViewById<Button>(R.id.assign_button)
            button.setOnClickListener {
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

        findViewById<SwitchCompat>(R.id.reminderSwitch)
            .setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    findViewById<LinearLayout>(R.id.enterDateLinearLayout)
                        .visibility = View.VISIBLE
                } else {
                    findViewById<LinearLayout>(R.id.enterDateLinearLayout)
                        .visibility = View.GONE
                }
            }

        findViewById<TextInputEditText>(R.id.reminderDateEditText)
            .setOnClickListener {
                val now = Calendar.getInstance()
                val dateChooseDialog = DatePickerDialog.newInstance(
                    this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
                )
                dateChooseDialog.setOkText("OK")
                dateChooseDialog.setCancelText("CANCEL")
                dateChooseDialog.show(supportFragmentManager, "DateChooseDialog")
            }
        findViewById<TextInputEditText>(R.id.reminderTimeEditText)
            .setOnClickListener {
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
            findViewById<TextView>(R.id.reminderTimeTextView).text =
                    "将在" + formatDate(dateAndTimeFormat) + "提醒您"
            findViewById<TextInputEditText>(R.id.reminderDateEditText).
                setText(formatDate(dateFormat))
        } else {
            findViewById<TextView>(R.id.reminderTimeTextView).text =
                    "不能将提醒设在过去"
            if (findViewById<TextInputEditText>(R.id.reminderDateEditText).text != null)
                findViewById<TextInputEditText>(R.id.reminderDateEditText).text!!.clear()
        }
    }

    private fun setTimeText() {
        val timeFormat = "h:mm a"
        val dateAndTimeFormat = "d MMM, yyyy, h:mm a"
        if (reminderDate > Calendar.getInstance().time) {
            findViewById<TextView>(R.id.reminderTimeTextView).text =
                    "将在" + formatDate(dateAndTimeFormat) + "提醒您"
            findViewById<TextInputEditText>(R.id.reminderTimeEditText).
                setText(formatDate(timeFormat))
        } else {
            findViewById<TextView>(R.id.reminderTimeTextView).text =
                    "不能将提醒设在过去"
            if (findViewById<TextInputEditText>(R.id.reminderTimeEditText).text != null)
                findViewById<TextInputEditText>(R.id.reminderTimeEditText).text!!.clear()
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
                    presenter.setLocalReminder(findViewById(R.id.reminderLayout))
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
