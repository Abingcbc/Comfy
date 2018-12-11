package com.kha.cbc.comfy.service

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.content.ServiceConnection

// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_FOO = "com.kha.cbc.comfy.service.action.FOO"
private const val ACTION_BAZ = "com.kha.cbc.comfy.service.action.BAZ"

// TODO: Rename parameters
private const val EXTRA_PARAM1 = "com.kha.cbc.comfy.service.extra.PARAM1"
private const val EXTRA_PARAM2 = "com.kha.cbc.comfy.service.extra.PARAM2"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class AmapTrackUploadService : IntentService("AmapTrackUploadService") {

    lateinit var userId: String

    override fun onHandleIntent(intent: Intent?) {
        if(intent != null){
            userId = intent.getStringExtra("userId")
        }
    }

    override fun unbindService(conn: ServiceConnection) {
        super.unbindService(conn)
    }
}
