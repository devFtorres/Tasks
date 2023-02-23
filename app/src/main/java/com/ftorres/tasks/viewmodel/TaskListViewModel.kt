package com.ftorres.tasks.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ftorres.tasks.service.constants.TaskConstants
import com.ftorres.tasks.service.listener.APIListener
import com.ftorres.tasks.service.model.TaskModel
import com.ftorres.tasks.service.model.ValidationModel
import com.ftorres.tasks.service.repository.TaskRepository

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    val taskRepository = TaskRepository(application.applicationContext)
    private var taskFilter = 0

    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    private val _delete = MutableLiveData<ValidationModel>()
    val delete: LiveData<ValidationModel> = _delete

    private val _status = MutableLiveData<ValidationModel>()
    val status: LiveData<ValidationModel> = _status

    @RequiresApi(Build.VERSION_CODES.M)
    fun list(filter: Int) {
        taskFilter = filter
        val listener = object : APIListener<List<TaskModel>> {
            override fun onSucess(result: List<TaskModel>) {
                _tasks.value = result
            }
            override fun onFailure(message: String) {
            }
        }

        when (filter) {
            TaskConstants.FILTER.ALL -> {
                taskRepository.list(listener)
            }
            TaskConstants.FILTER.NEXT -> {
                taskRepository.listNext(listener)
            }
            TaskConstants.FILTER.EXPIRED -> {
                taskRepository.listOverdue(listener)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun delete(id: Int) {
        taskRepository.delete(id, object : APIListener<Boolean> {
            override fun onSucess(result: Boolean) {
                list(taskFilter)
            }

            override fun onFailure(message: String) {
                _delete.value = ValidationModel(message)
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun status(id: Int, complete: Boolean){

        val listener = object : APIListener<Boolean> {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onSucess(result: Boolean) {
                list(taskFilter)
            }

            override fun onFailure(message: String) {
                _status.value = ValidationModel(message)
            }

        }


        if (complete){
            taskRepository.complete(id, listener)
        }else {
            taskRepository.undo(id, listener)
        }
    }

}