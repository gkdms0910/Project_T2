package com.example.project_t2.viewmodel.WSentiment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_t2.roomDB.WSentiment.WSentimentEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WSentimentViewModel(private val repository: WSentimentRepository) : ViewModel() {
    private var _wsentimentList = MutableStateFlow<List<WSentimentEntity>>(emptyList())
    val wsentimentList = _wsentimentList

    init {
        findAll()
    }

    fun findAll() {
        viewModelScope.launch {
            repository.findAll().collect {
                _wsentimentList.value = it
            }
        }
    }

    fun InsertItem(item: WSentimentEntity) {
        viewModelScope.launch {
            repository.insertItem(item)
        }
    }

    fun UpdateItem(item: WSentimentEntity) {
        viewModelScope.launch {
            repository.updateItem(item)
        }
    }

    fun DeleteItem(item: WSentimentEntity) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }
}