package com.owino.chronos.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import com.owino.chronos.ApplicationContext
import com.owino.chronos.database.dao.ChronosSessionDao
import com.owino.chronos.database.entities.ChronosSession
import com.owino.chronos.events.LaunchNewSessionEvent
import com.owino.chronos.events.SessionLengthUpdatedEvent
import com.owino.chronos.events.SessionReloadEvent
import com.owino.chronos.settings.ChronosPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ChronosHomeViewModel(private val chronosSessionDao: ChronosSessionDao) {
    var currentSessionUUID: String? = null
    var chronosSession: ChronosSession? = null
    var allSessions: LiveData<List<ChronosSession>>? = null

    init {
        EventBus.getDefault().register(this)
    }

    fun increaseSessionHours(context: Context) {
        var hours = chronosSession?.completedHours?.inc()

        loadCoroutineScope(context).launch {
            if (currentSessionUUID != null) {
                if (hours == null) hours = 0
                chronosSessionDao.updateSessionHours(hours!!, currentSessionUUID!!)
                loadSession(context)
            } else {
                throw AssertionError("Failed to update session, session id is null")
            }
        }
    }

    fun reduceSessionHours(context: Context) {
        var hours = chronosSession?.completedHours?.minus(1)
        loadCoroutineScope(context).launch {
            if (currentSessionUUID != null) {
                if (hours == null) hours = 1
                if (hours!! < 0) hours = 0
                chronosSessionDao.updateSessionHours(hours!!, currentSessionUUID!!)
                loadSession(context)
            } else {
                throw AssertionError("Failed to update session, session id is null")
            }
        }
    }

    fun loadSession(context: Context) {
        loadCoroutineScope(context).launch {
            if (currentSessionUUID != null){
                chronosSession = chronosSessionDao.findSessionByUuid(currentSessionUUID!!)

                withContext(Dispatchers.Main.immediate) {
                    EventBus.getDefault().post(SessionReloadEvent())
                }

            } else {
                throw AssertionError("Failed to update session, session id is null")
            }
        }
    }

    fun createNewSession(context: Context, targetHours: Int) {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val currentDate: String = LocalDateTime.now().format(formatter)
        val session =
            ChronosSession(UUID.randomUUID().toString(), currentDate, currentDate, targetHours, 0)
        currentSessionUUID = session.uuid
        loadCoroutineScope(context).launch {
            chronosSessionDao.saveSession(session)
            ChronosPreferences.setActiveSession(
                context,
                currentSessionUUID
            )
            loadSession(context)
        }
    }

    fun saveSession(context: Context) {
        loadCoroutineScope(context).launch {
            chronosSessionDao.saveSession(chronosSession!!)
        }
    }

    fun initSession(context: Context) {
        currentSessionUUID = ChronosPreferences.getActiveSession(context)
        if (currentSessionUUID != null) {
            loadSession(context)
        } else {
            EventBus.getDefault().post(LaunchNewSessionEvent())
        }
    }

    private fun loadCoroutineScope(context: Context): CoroutineScope {
        val applicationContext = context.applicationContext as ApplicationContext
        return applicationContext.getCoroutineScope()
    }

    @Subscribe
    public fun sessionLengthUpdatedEvent(event: SessionLengthUpdatedEvent) {
        loadSession(event.context)
    }

    fun invalidateCurrentSession(context: Context) {
        currentSessionUUID = null
        ChronosPreferences.setActiveSession(context, currentSessionUUID)
    }
}
