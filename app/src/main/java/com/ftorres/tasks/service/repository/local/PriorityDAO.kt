package com.ftorres.tasks.service.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ftorres.tasks.service.model.PriorityModel

@Dao
interface PriorityDAO {

    @Insert
    fun save(list: List<PriorityModel>)

    @Query("DELETE FROM Priority")
    fun clear()

    @Query("SELECT description FROM Priority WHERE id = :id")
    fun getDescription(id: Int): String

    @Query("SELECT * FROM Priority")
    fun list(): List<PriorityModel>

}