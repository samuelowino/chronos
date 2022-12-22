package com.owino.chronos.events

import android.content.Context

data class SessionLengthUpdateEvent(val isInc: Boolean, val by: Int) {}

data class SessionLengthUpdatedEvent(val context: Context, val success: Boolean) {}

data class SessionsListLoadedEvent(val context: Context)

class SessionReloadEvent()

class LaunchNewSessionEvent()

