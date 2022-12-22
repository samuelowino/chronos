package com.owino.chronos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.owino.chronos.database.entities.ChronosSession

@Dao
interface ChronosSessionDao {

    @Insert
    fun saveSession(session: ChronosSession)

    @Query("SELECT * FROM CHRONOS_SESSION")
    fun findAllSessions(): List<ChronosSession>

    @Query("SELECT * FROM CHRONOS_SESSION WHERE uuid = :uuid LIMIT 1")
    fun findSessionByUuid(uuid: String): ChronosSession

    @Query("SELECT * FROM CHRONOS_SESSION WHERE uuid = :uuid LIMIT 1")
    fun liveSession(uuid: String): LiveData<ChronosSession>

    @Query("UPDATE CHRONOS_SESSION SET COMPLETED_HOURS = :hours WHERE uuid = :sessionUuid")
    fun updateSessionHours(hours: Int, sessionUuid: String)

    @Delete
    fun delete(session: ChronosSession)
}