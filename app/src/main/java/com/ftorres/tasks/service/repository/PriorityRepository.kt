package com.ftorres.tasks.service.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.ftorres.tasks.R
import com.ftorres.tasks.service.constants.TaskConstants
import com.ftorres.tasks.service.listener.APIListener
import com.ftorres.tasks.service.model.PriorityModel
import com.ftorres.tasks.service.repository.local.TaskDatabase
import com.ftorres.tasks.service.repository.remote.PriorityService
import com.ftorres.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(PriorityService::class.java)
    private val database = TaskDatabase.getDatabase(context).priorityDAO()

    //CACHE
    companion object {
        private val cache = mutableMapOf<Int, String>()
        fun getDescription(id: Int): String {
            return cache[id] ?: ""
        }
        fun setDescription(id: Int, str: String){
            cache[id] = str
        }
    }

    fun getDescription(id: Int): String{
        val cached = PriorityRepository.getDescription(id)
        return if (cached == ""){
            val description = database.getDescription(id)
            PriorityRepository.setDescription(id, description)
            description
        }else {
            cached
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun list(listener: APIListener<List<PriorityModel>>) {
        if (!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.list(), listener)
    }

    fun list(): List<PriorityModel> {
        return database.list()
    }

    fun save(list: List<PriorityModel>) {
        database.clear()
        database.save(list)
    }


}