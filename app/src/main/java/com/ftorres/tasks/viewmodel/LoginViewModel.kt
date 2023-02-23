package com.ftorres.tasks.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ftorres.tasks.service.constants.TaskConstants
import com.ftorres.tasks.service.helper.BiometricHelper
import com.ftorres.tasks.service.listener.APIListener
import com.ftorres.tasks.service.model.PersonModel
import com.ftorres.tasks.service.model.PriorityModel
import com.ftorres.tasks.service.model.ValidationModel
import com.ftorres.tasks.service.repository.PersonRepository
import com.ftorres.tasks.service.repository.PriorityRepository
import com.ftorres.tasks.service.repository.SecurityPreferences
import com.ftorres.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)


    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = _loggedUser

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object : APIListener<PersonModel> {
            override fun onSucess(result: PersonModel) {
                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, result.name)
                RetrofitClient.addHeaders(result.token, result.personKey)
                _login.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                _login.value = ValidationModel(message)
            }
        })

    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyAuthentication() {
        val token = securityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val person = securityPreferences.get(TaskConstants.SHARED.PERSON_KEY)

        RetrofitClient.addHeaders(token, person)
        val logged = (token != "" && person != "")


        //
        //_loggedUser.value = logged

        if (!logged) {
            priorityRepository.list(object : APIListener<List<PriorityModel>> {
                override fun onSucess(result: List<PriorityModel>) {
                    priorityRepository.save(result)
                }

                override fun onFailure(message: String) {
                }

            })
        }


        _loggedUser.value = (logged && BiometricHelper.isBiometricAvailable(getApplication<Application>().applicationContext))


    }

}