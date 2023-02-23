package com.ftorres.tasks.service.repository

import android.app.Person
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.ftorres.tasks.R
import com.ftorres.tasks.service.constants.TaskConstants
import com.ftorres.tasks.service.listener.APIListener
import com.ftorres.tasks.service.model.PersonModel
import com.ftorres.tasks.service.repository.remote.PersonService
import com.ftorres.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(PersonService::class.java)

    @RequiresApi(Build.VERSION_CODES.M)
    fun login(email: String, password: String, listener: APIListener<PersonModel>) {
        if (!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.login(email, password), listener)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun create(name: String, email: String, password: String, listener: APIListener<PersonModel>) {
        if (!isConnectionAvailable()){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }
        executeCall(remote.create(name, email, password), listener)
    }

}

