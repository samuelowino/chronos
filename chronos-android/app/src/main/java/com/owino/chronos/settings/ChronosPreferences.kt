package com.owino.chronos.settings

import android.content.Context

object ChronosPreferences {
    private const val chronosPreferencesFile: String = "chronos.prefs"
    private const val currentActiveSessionUUIDPref: String = "current.active.session"

    fun setActiveSession(context: Context, sessionUUID: String?){
        setStringPref(context, currentActiveSessionUUIDPref, sessionUUID)
    }

    fun getActiveSession(context: Context): String?{
        return getStringPrefs(context, currentActiveSessionUUIDPref, null)
    }

    private fun getBooleanPrefs(context: Context, prefName: String, defValue: Boolean): Boolean {
        return context.getSharedPreferences(chronosPreferencesFile, Context.MODE_PRIVATE)
            .getBoolean(prefName, defValue)
    }

    private fun setBooleanPrefs(context: Context, prefName: String, value: Boolean) {
        context.getSharedPreferences(chronosPreferencesFile, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(prefName, value)
            .apply()
    }

    private fun setIntPrefs(context: Context, key: String, value: Int) {
        context.getSharedPreferences(chronosPreferencesFile, Context.MODE_PRIVATE)
            .edit()
            .putInt(key, value)
            .apply()
    }

    private fun getIntPref(context: Context, prefName: String, defaultVal: Int): Int {
        return context.getSharedPreferences(chronosPreferencesFile, Context.MODE_PRIVATE)
            .getInt(prefName, defaultVal)
    }

    private fun setStringPref(context: Context, prefName: String, value: String?) {
        context.getSharedPreferences(chronosPreferencesFile, Context.MODE_PRIVATE)
            .edit()
            .putString(prefName, value)
            .apply()
    }

    private fun getStringPrefs(context: Context, prefName: String, defaultVal: String?): String? {
        return context.getSharedPreferences(chronosPreferencesFile, Context.MODE_PRIVATE)
            .getString(prefName, defaultVal)
    }


    private fun setLongPreference(context: Context, prefName: String, value: Long) {
        context.getSharedPreferences(chronosPreferencesFile, Context.MODE_PRIVATE)
            .edit()
            .putLong(prefName, value)
            .apply()
    }

    private fun getLongPreference(context: Context, prefName: String, defValue: Long): Long? {
        return try {
            context.getSharedPreferences(chronosPreferencesFile, Context.MODE_PRIVATE)
                .getLong(prefName, defValue)
        } catch (ex: ClassCastException) {
            1L
        }
    }
}