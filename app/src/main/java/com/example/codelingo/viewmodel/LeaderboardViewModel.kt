package com.example.codelingo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codelingo.data.model.AppUser
import com.example.codelingo.data.repository.FirebaseRepository
import kotlinx.coroutines.launch

class LeaderboardViewModel : ViewModel() {
    
    private val firebaseRepository = FirebaseRepository()
    
    private val _leaderboardData = MutableLiveData<List<AppUser>>()
    val leaderboardData: LiveData<List<AppUser>> = _leaderboardData
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    init {
        loadLeaderboard()
    }
    
    fun loadLeaderboard() {
        _isLoading.value = true
        _error.value = null
        
        viewModelScope.launch {
            try {
                firebaseRepository.getLeaderboard().collect { users ->
                    _leaderboardData.value = users
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = "Gagal memuat leaderboard: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    
    fun refreshLeaderboard() {
        loadLeaderboard()
    }
} 