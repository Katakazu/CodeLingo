package com.example.codelingo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codelingo.data.repository.FirebaseRepository
import kotlinx.coroutines.launch

class QuestViewModel : ViewModel() {
    
    private val firebaseRepository = FirebaseRepository()
    
    private val _dailyQuests = MutableLiveData<List<Map<String, Any>>>()
    val dailyQuests: LiveData<List<Map<String, Any>>> = _dailyQuests
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    init {
        loadDailyQuests()
    }
    
    fun loadDailyQuests() {
        _isLoading.value = true
        _error.value = null
        
        viewModelScope.launch {
            try {
                val result = firebaseRepository.getDailyQuests()
                result.fold(
                    onSuccess = { quests ->
                        _dailyQuests.value = quests
                        _isLoading.value = false
                    },
                    onFailure = { exception ->
                        _error.value = "Gagal memuat quest: ${exception.message}"
                        _isLoading.value = false
                    }
                )
            } catch (e: Exception) {
                _error.value = "Gagal memuat quest: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    
    fun completeQuest(uid: String, questId: String, reward: Int) {
        viewModelScope.launch {
            try {
                val result = firebaseRepository.completeQuest(uid, questId, reward)
                result.fold(
                    onSuccess = {
                        // Quest completed successfully
                        loadDailyQuests() // Refresh quests
                    },
                    onFailure = { exception ->
                        _error.value = "Gagal menyelesaikan quest: ${exception.message}"
                    }
                )
            } catch (e: Exception) {
                _error.value = "Gagal menyelesaikan quest: ${e.message}"
            }
        }
    }
    
    fun refreshQuests() {
        loadDailyQuests()
    }
} 