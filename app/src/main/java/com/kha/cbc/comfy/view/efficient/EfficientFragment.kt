package com.kha.cbc.comfy.view.efficient

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.leancloud.chatkit.LCChatKit
import cn.leancloud.chatkit.activity.LCIMConversationActivity
import cn.leancloud.chatkit.utils.LCIMConstants
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.view.efficient.chart.EfficientChartActivity
import com.leon.lib.settingview.LSettingItem


class EfficientFragment : Fragment(){
    lateinit var fragmentView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.efficient_menu, container, false)
        initTouch()
        return fragmentView
    }

    private fun initTouch(){
        val appUsageItem = fragmentView.findViewById<LSettingItem>(R.id.recent_app)
        appUsageItem.setmOnLSettingItemClick {
            val intent = Intent(activity, UsageActivity::class.java)
            startActivity(intent)
        }
        val appUsageChartItem = fragmentView.findViewById<LSettingItem>(R.id.recent_graph)
        appUsageChartItem.setmOnLSettingItemClick {
            val intent = Intent(activity, EfficientChartActivity::class.java)
            startActivity(intent)
        }

        val chat_test = fragmentView.findViewById<LSettingItem>(R.id.chat)
        chat_test.setmOnLSettingItemClick {
            LCChatKit.getInstance().open("Tom", object : AVIMClientCallback() {
                override fun done(avimClient: AVIMClient, e: AVIMException?) {
                    if (null == e) {
                        val intent = Intent(activity, LCIMConversationActivity::class.java)
                        intent.putExtra(LCIMConstants.PEER_ID, "Jerry")
                        startActivity(intent)
                    } else {
                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}