package com.example.codelingo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.codelingo.data.database.CodeLingoDatabase
import com.example.codelingo.data.model.User
import com.example.codelingo.data.preferences.UserPreferences
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val database = CodeLingoDatabase.getDatabase(application)
    private val userDao = database.userDao()
    private val userPreferences = UserPreferences(application)

    private val _loginResult = MutableLiveData<AuthResult>()
    val loginResult: LiveData<AuthResult> = _loginResult

    private val _registerResult = MutableLiveData<AuthResult>()
    val registerResult: LiveData<AuthResult> = _registerResult

    sealed class AuthResult {
        data class Success(val user: User, val isFirstTime: Boolean = false) : AuthResult()
        data class Error(val message: String) : AuthResult()
        data class Loading(val isLoading: Boolean) : AuthResult()
    }

    fun loginUser(username: String, password: String) {
        _loginResult.value = AuthResult.Loading(true)

        viewModelScope.launch {
            try {
                val user = userDao.loginUser(username, password)

                if (user != null) {
                    // Save login state
                    userPreferences.setUserLoggedIn(true)
                    userPreferences.setCurrentUserId(user.id)

                    _loginResult.value = AuthResult.Success(user, user.isFirstTime)
                } else {
                    _loginResult.value = AuthResult.Error("Username atau password salah")
                }
            } catch (e: Exception) {
                _loginResult.value = AuthResult.Error("Terjadi kesalahan: ${e.message}")
            } finally {
                _loginResult.value = AuthResult.Loading(false)
            }
        }
    }

    fun registerUser(username: String, email: String, password: String) {
        _registerResult.value = AuthResult.Loading(true)

        viewModelScope.launch {
            try {
                // Check if username already exists
                val existingUser = userDao.loginUser(username, "")
                if (existingUser != null) {
                    _registerResult.value = AuthResult.Error("Username sudah digunakan")
                    return@launch
                }

                // Create new user
                val newUser = User(
                    username = username,
                    email = email,
                    password = password,
                    level = 1,
                    experience = 0,
                    streak = 0,
                    gems = 100, // Starting gems
                    isFirstTime = true
                )

                val userId = userDao.insertUser(newUser)
                val insertedUser = newUser.copy(id = userId.toInt())

                // Save login state
                userPreferences.setUserLoggedIn(true)
                userPreferences.setCurrentUserId(insertedUser.id)

                _registerResult.value = AuthResult.Success(insertedUser, true)

            } catch (e: Exception) {
                _registerResult.value = AuthResult.Error("Terjadi kesalahan: ${e.message}")
            } finally {
                _registerResult.value = AuthResult.Loading(false)
            }
        }
    }

    fun getCurrentUser(): LiveData<User?> {
        val currentUser = MutableLiveData<User?>()

        viewModelScope.launch {
            try {
                val userId = userPreferences.getCurrentUserId()
                if (userId != -1) {
                    val user = userDao.getUserById(userId)
                    currentUser.value = user
                } else {
                    currentUser.value = null
                }
            } catch (e: Exception) {
                currentUser.value = null
            }
        }

        return currentUser
    }

    fun logout() {
        userPreferences.setUserLoggedIn(false)
        userPreferences.setCurrentUserId(-1)
    }
}