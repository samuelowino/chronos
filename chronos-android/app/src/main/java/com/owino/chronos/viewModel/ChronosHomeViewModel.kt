package com.owino.chronos.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.owino.chronos.ApplicationContext
import com.owino.chronos.database.dao.ChronosSessionDao
import com.owino.chronos.database.entities.ChronosSession
import com.owino.chronos.settings.ChronosPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class ChronosHomeViewModel(private val chronosSessionDao: ChronosSessionDao) {
    private val TAG = "ChronosHomeViewModel"
    var currentSessionUUID: String? = null
    var chronosSession: ChronosSession? = null
    var allSessions: LiveData<List<ChronosSession>>? = null

    fun increaseSessionHours(context: Context) {
        var session: ChronosSession? = chronosSession
        val hours = chronosSession?.completedHours?.inc()

        loadCoroutineScope(context).launch {
            currentSessionUUID?.let {
                hours.let {
                    chronosSessionDao.updateSessionHours(hours!!, currentSessionUUID!!)
                }
            }
        }
    }

    fun reduceSessionHours(context: Context) {
        var session: ChronosSession? = chronosSession
        val hours = chronosSession?.completedHours?.minus(1)

        loadCoroutineScope(context).launch {
            currentSessionUUID?.let {
                chronosSessionDao.updateSessionHours(hours!!, currentSessionUUID!!)
            }
        }
    }

    fun loadSession(context: Context, failedCallback: InitSessionFailedCallback) {
        loadCoroutineScope(context).launch {
            chronosSession = currentSessionUUID?.let {
                chronosSessionDao.findSessionByUuid(it)
            }
            failedCallback.onSessionSuccess()
            Log.e(TAG, "loadSession: success " + chronosSession )
        }
    }

    fun loadAllSessions(context: Context) {
        loadCoroutineScope(context).launch {
            allSessions = chronosSessionDao.findAllSessions()
        }
    }

    fun createNewSession(context: Context, targetHours: Int) {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val currentDate: String = LocalDateTime.now().format(formatter)
        val session = ChronosSession(UUID.randomUUID().toString(), currentDate, currentDate, targetHours, 0)
        currentSessionUUID = session.uuid
        loadCoroutineScope(context).launch {
            chronosSessionDao.saveSession(session)
        }
    }

    fun saveSession(context: Context){
        loadCoroutineScope(context).launch {
            chronosSessionDao.saveSession(chronosSession!!)
        }
    }

    fun initSession(context: Context, failedCallback: InitSessionFailedCallback) {
        currentSessionUUID = ChronosPreferences.getActiveSession(context)
        if (currentSessionUUID != null){
            loadSession(context,failedCallback)
        } else {
            failedCallback.onSessionInitFailed()
        }
    }

    fun loadCoroutineScope(context: Context): CoroutineScope{
        val applicationContext = context.applicationContext as ApplicationContext
        return applicationContext.getCoroutineScope()
    }

    interface InitSessionFailedCallback {
        fun onSessionInitFailed()
        fun onSessionSuccess()
    }
}
