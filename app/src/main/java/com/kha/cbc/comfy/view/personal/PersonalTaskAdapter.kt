package com.kha.cbc.comfy.view.personal

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.kennyc.bottomsheet.BottomSheet
import com.kennyc.bottomsheet.BottomSheetListener
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.model.PersonalTask
import com.loopeer.cardstack.CardStackView
import com.loopeer.cardstack.StackAdapter
import kotlinx.android.synthetic.main.stack_task.view.*

/**
 * Created by ABINGCBC
 * on 2018/11/2
 */

class PersonalTaskAdapter(
    context: Context,
    internal var cardStackView: CardStackView,
    internal var fragment: PersonalFragment,
    internal var motherActivity: Activity
) : StackAdapter<Int>(context), BottomSheetListener {

    lateinit var personalTaskList: List<PersonalTask>
    var selectedPosition: Int = 0

    fun updateData(data: List<Int>, personalTaskList: List<PersonalTask>) {
        this.personalTaskList = personalTaskList
        //通过基类调用notifyDataSetChanged()，才能有显示
        updateData(data)
        notifyDataSetChanged()
    }

    override fun bindView(data: Int?, position: Int, holder: CardStackView.ViewHolder) {
        val personalTaskViewHolder = holder as PersonalTaskViewHolder
        personalTaskViewHolder.onBind(data, position, personalTaskList)
    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): CardStackView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.stack_task, parent, false)
        return PersonalTaskViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    inner class PersonalTaskViewHolder(internal var root: View) : CardStackView.ViewHolder(root) {
        internal var header: FrameLayout
        internal var cardsListView: RecyclerView
        internal var taskTitle: TextView

        init {
            header = root.task_header
            cardsListView = root.cards_list
            taskTitle = root.task_title_text
        }

        fun onBind(backgroundColorId: Int?, position: Int, dataList: List<PersonalTask>) {
            header.background.setColorFilter(
                ContextCompat.getColor(context, backgroundColorId!!),
                PorterDuff.Mode.SRC_IN
            )
            header.setOnClickListener {
                cardStackView.performItemClick(this)
            }
            header.setOnLongClickListener {
                selectedPosition = position
                BottomSheet.Builder(this.context)
                    .setSheet(R.menu.bottom_grid)
                    .grid()
                    .setListener(this@PersonalTaskAdapter)
                    .show()
                return@setOnLongClickListener true
            }
            taskTitle.text = dataList[position].title
            val personalCardAdapter = PersonalCardAdapter(dataList[position].cards, fragment, motherActivity);
            cardsListView.layoutManager = LinearLayoutManager(this.context)
            cardsListView.adapter = personalCardAdapter
        }


        override fun onItemExpand(b: Boolean) {
            cardsListView.visibility = if (b) View.VISIBLE else View.GONE
        }
    }


    override fun onSheetItemSelected(p0: BottomSheet, item: MenuItem?, p2: Any?) {
        when (item!!.itemId) {
            R.id.edit_item -> {
                MaterialDialog(this.context!!)
                    .input { materialDialog, charSequence ->
                        fragment.onUpdateTask(PersonalTask(charSequence.toString()),
                            personalTaskList[selectedPosition].id)
                    }
                    .positiveButton()
                    .negativeButton()
                    .show()
            }

            R.id.delete_item -> {
                MaterialDialog(context).title(text = "确认删除此任务列表？")
                    .message(text = "删除任务列表将会删除其中所有的任务")
                    .positiveButton {
                        fragment.deleteTaskFromDB(personalTaskList[selectedPosition])
                        cardStackView.removeViewAt(selectedPosition)
                    }.positiveButton(text = "确认")
                    .negativeButton(text = "取消")
                    .show()
            }

            R.id.complete_item -> {
                MaterialDialog(context).title(text = "确认完成此任务列表？")
                    .message(text = "完成任务列表将会删除其中所有的任务")
                    .positiveButton {
                        fragment.deleteTaskFromDB(personalTaskList[selectedPosition])
                        cardStackView.removeViewAt(selectedPosition)
                    }.positiveButton(text = "确认")
                    .negativeButton(text = "取消")
                    .show()
            }
        }
    }

    override fun onSheetDismissed(p0: BottomSheet, p1: Any?, p2: Int) {
    }

    override fun onSheetShown(p0: BottomSheet, p1: Any?) {
    }
}
