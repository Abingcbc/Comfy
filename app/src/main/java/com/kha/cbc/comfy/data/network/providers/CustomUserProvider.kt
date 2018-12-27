package com.kha.cbc.comfy.data.network.providers

import cn.leancloud.chatkit.LCChatKitUser
import cn.leancloud.chatkit.LCChatProfilesCallBack
import cn.leancloud.chatkit.LCChatProfileProvider


class CustomUserProvider private constructor() : LCChatProfileProvider {

    val allUsers: ArrayList<LCChatKitUser>
        get() = partUsers

    override fun fetchProfiles(list: List<String>, callBack: LCChatProfilesCallBack) {
        val userList = ArrayList<LCChatKitUser>()
        for (userId in list) {
            for (user in partUsers) {
                if (user.userId == userId) {
                    userList.add(user)
                    break
                }
            }
        }
        callBack.done(userList, null)
    }

    companion object {

        private val customUserProvider: CustomUserProvider = CustomUserProvider()


        fun getInstance(): CustomUserProvider {
            return customUserProvider
        }

        val partUsers = ArrayList<LCChatKitUser>()

        // 此数据均为模拟数据，仅供参考
//        init {
//            partUsers.add(LCChatKitUser("Tom", "Tom", "http://www.avatarsdb.com/avatars/tom_and_jerry2.jpg"))
//            partUsers.add(LCChatKitUser("Jerry", "Jerry", "http://www.avatarsdb.com/avatars/jerry.jpg"))
//            partUsers.add(LCChatKitUser("Harry", "Harry", "http://www.avatarsdb.com/avatars/young_harry.jpg"))
//            partUsers.add(
//                LCChatKitUser(
//                    "William",
//                    "William",
//                    "http://www.avatarsdb.com/avatars/william_shakespeare.jpg"
//                )
//            )
//            partUsers.add(LCChatKitUser("Bob", "Bob", "http://www.avatarsdb.com/avatars/bath_bob.jpg"))
//        }
    }
}