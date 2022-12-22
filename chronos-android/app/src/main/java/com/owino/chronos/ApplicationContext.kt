package com.owino.chronos

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import androidx.room.Room
import com.owino.chronos.database.ChronosDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors.newFixedThreadPool

class ApplicationContext : MultiDexApplication() {
    private var database: ChronosDatabase? = null
    private var coroutineScope: CoroutineScope? = null

    fun getCoroutineScope(): CoroutineScope {
        if (coroutineScope == null){
            coroutineScope = CoroutineScope(newFixedThreadPool(10).asCoroutineDispatcher())
        }
        return coroutineScope!!
    }


    fun getAppDatabase(context: Context): ChronosDatabase {
        return database?: Room.databaseBuilder(
            context,
            ChronosDatabase::class.java,
            "CHRONOS_DATABASE"
        ).build()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}