package com.owino.chronos.viewModel

import android.content.Context
import com.owino.chronos.ApplicationContext
import com.owino.chronos.database.dao.ChronosSessionDao
import com.owino.chronos.database.entities.ChronosSession
import com.owino.chronos.events.SessionReloadEvent
import com.owino.chronos.events.SessionsListLoadedEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class TrendsViewModel(private val context: Context) {
    var sessions = mutableListOf<ChronosSession>()
    private lateinit var sessionDao: ChronosSessionDao

    init {
        try {
            loadSessionDao()
            EventBus.getDefault().register(this)
        } catch (exception: java.lang.Exception) {
            exception.printStackTrace()
        }
    }

    fun loadOrReloadSessions() {
        loadCoroutineScope(context).launch {
            sessions.clear()
            sessions.addAll(sessionDao.findAllSessions())

            withContext(Dispatchers.Main.immediate){
                EventBus.getDefault().post(SessionsListLoadedEvent(context))
            }
        }
    }

    private fun loadCoroutineScope(context: Context): CoroutineScope {
        val applicationContext = context.applicationContext as ApplicationContext
        return applicationContext.getCoroutineScope()
    }

    private fun loadSessionDao() {
        val applicationContext = context.applicationContext as ApplicationContext
        sessionDao = applicationContext.getAppDatabase(context).sessionDao()
    }

    fun deleteSession(session: ChronosSession) {
        loadCoroutineScope(context).launch {
            sessionDao.delete(session)

            loadOrReloadSessions()
        }
    }

    @Subscribe
    public fun onSessionReloadEvent(event: SessionReloadEvent) {
        loadOrReloadSessions()
    }
}