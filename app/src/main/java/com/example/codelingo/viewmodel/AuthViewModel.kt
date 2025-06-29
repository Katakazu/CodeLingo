package com.example.codelingo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codelingo.data.model.AppUser
import com.example.codelingo.data.preferences.UserPreferences
import com.example.codelingo.data.repository.FirebaseRepository
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    private var userPreferences: UserPreferences? = null

    private val _loginResult = MutableLiveData<AuthResult>()
    val loginResult: LiveData<AuthResult> = _loginResult

    private val _registerResult = MutableLiveData<AuthResult>()
    val registerResult: LiveData<AuthResult> = _registerResult

    private val _currentUser = MutableLiveData<AppUser?>()
    val currentUser: LiveData<AppUser?> = _currentUser

    fun setUserPreferences(prefs: UserPreferences) {
        userPreferences = prefs
    }

    sealed class AuthResult {
        data class Success(val user: AppUser, val isFirstTime: Boolean = false) : AuthResult()
        data class Error(val message: String) : AuthResult()
        data class Loading(val isLoading: Boolean) : AuthResult()
    }

    fun loginUser(usernameOrEmail: String, password: String) {
        _loginResult.value = AuthResult.Loading(true)
        viewModelScope.launch {
            try {
                val email = if (android.util.Patterns.EMAIL_ADDRESS.matcher(usernameOrEmail).matches()) {
                    usernameOrEmail
                } else {
                    // Cari email berdasarkan username
                    val emailResult = firebaseRepository.getEmailByUsername(usernameOrEmail)
                    emailResult.getOrElse {
                        _loginResult.value = AuthResult.Error(it.localizedMessage ?: it.message ?: "Username tidak ditemukan")
                        _loginResult.value = AuthResult.Loading(false)
                        return@launch
                    }
                }
                val result = firebaseRepository.signIn(email, password)
                result.fold(
                    onSuccess = { firebaseUser ->
                        val userDataResult = firebaseRepository.getUserData(firebaseUser.uid)
                        userDataResult.fold(
                            onSuccess = { appUser ->
                                userPreferences?.setUserLoggedIn(true)
                                userPreferences?.setCurrentUserId(appUser.uid)
                                userPreferences?.setUserLevel(appUser.level)
                                userPreferences?.setUserXp(appUser.experience)
                                userPreferences?.setUserDays(appUser.streak)
                                userPreferences?.setTotalXp(appUser.totalScore)
                                userPreferences?.setUsername(appUser.username)
                                userPreferences?.setSelectedLanguage(appUser.selectedLanguage)
                                userPreferences?.setCurrentLesson(appUser.currentLesson)
                                _currentUser.value = appUser
                                _loginResult.value = AuthResult.Success(appUser, false)
                            },
                            onFailure = { exception ->
                                _loginResult.value = AuthResult.Error("Gagal mengambil data user: ${exception.localizedMessage ?: exception.message}")
                            }
                        )
                    },
                    onFailure = { exception ->
                        _loginResult.value = AuthResult.Error(exception.localizedMessage ?: exception.message ?: "Login gagal")
                    }
                )
            } catch (e: Exception) {
                _loginResult.value = AuthResult.Error(e.localizedMessage ?: e.message ?: "Terjadi kesalahan saat login")
            } finally {
                _loginResult.value = AuthResult.Loading(false)
            }
        }
    }

    fun registerUser(username: String, email: String, password: String) {
        _registerResult.value = AuthResult.Loading(true)
        viewModelScope.launch {
            try {
                val result = firebaseRepository.signUp(email, password, username)
                result.fold(
                    onSuccess = { firebaseUser ->
                        val userDataResult = firebaseRepository.getUserData(firebaseUser.uid)
                        userDataResult.fold(
                            onSuccess = { appUser ->
                                userPreferences?.setUserLoggedIn(true)
                                userPreferences?.setCurrentUserId(appUser.uid)
                                userPreferences?.setUserLevel(appUser.level)
                                userPreferences?.setUserXp(appUser.experience)
                                userPreferences?.setUserDays(appUser.streak)
                                userPreferences?.setTotalXp(appUser.totalScore)
                                userPreferences?.setUsername(appUser.username)
                                userPreferences?.setSelectedLanguage(appUser.selectedLanguage)
                                userPreferences?.setCurrentLesson(appUser.currentLesson)
                                _currentUser.value = appUser
                                _registerResult.value = AuthResult.Success(appUser, true)
                            },
                            onFailure = { exception ->
                                _registerResult.value = AuthResult.Error("Gagal mengambil data user: ${exception.localizedMessage ?: exception.message}")
                            }
                        )
                    },
                    onFailure = { exception ->
                        _registerResult.value = AuthResult.Error(exception.localizedMessage ?: exception.message ?: "Registrasi gagal")
                    }
                )
            } catch (e: Exception) {
                _registerResult.value = AuthResult.Error(e.localizedMessage ?: e.message ?: "Terjadi kesalahan saat registrasi")
            } finally {
                _registerResult.value = AuthResult.Loading(false)
            }
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            try {
                val firebaseUser = firebaseRepository.getCurrentUser()
                if (firebaseUser != null) {
                    val userDataResult = firebaseRepository.getUserData(firebaseUser.uid)
                    userDataResult.fold(
                        onSuccess = { appUser ->
                            _currentUser.value = appUser
                        },
                        onFailure = {
                            _currentUser.value = null
                        }
                    )
                } else {
                    _currentUser.value = null
                }
            } catch (e: Exception) {
                _currentUser.value = null
            }
        }
    }

    fun updateUserLanguage(language: String) {
        viewModelScope.launch {
            try {
                val uid = _currentUser.value?.uid
                    ?: com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    val result = firebaseRepository.updateUserLanguage(uid, language)
                    result.fold(
                        onSuccess = {
                            val oldUser = _currentUser.value
                            if (oldUser != null) {
                                _currentUser.value = oldUser.copy(selectedLanguage = language)
                            }
                        },
                        onFailure = { exception ->
                            android.util.Log.e("AuthViewModel", "Update selectedLanguage ke Firestore GAGAL: ${exception.localizedMessage ?: exception.message}")
                        }
                    )
                } else {
                    android.util.Log.e("AuthViewModel", "Update selectedLanguage gagal: UID null")
                }
            } catch (e: Exception) {
                android.util.Log.e("AuthViewModel", "Update selectedLanguage ke Firestore ERROR: ${e.localizedMessage ?: e.message}")
            }
        }
    }

    fun updateUserProgress(experience: Int, level: Int, totalScore: Int) {
        viewModelScope.launch {
            try {
                val uid = _currentUser.value?.uid
                    ?: FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    val result = firebaseRepository.updateUserProgress(uid, experience, level, totalScore)
                    result.fold(
                        onSuccess = {
                            val oldUser = _currentUser.value
                            if (oldUser != null) {
                                _currentUser.value = oldUser.copy(
                                    experience = experience,
                                    level = level,
                                    totalScore = totalScore
                                )
                            }
                            android.util.Log.d("AuthViewModel", "Update progress ke Firestore BERHASIL: XP=$experience, Level=$level, Score=$totalScore")
                        },
                        onFailure = { exception ->
                            android.util.Log.e("AuthViewModel", "Update progress ke Firestore GAGAL: ${exception.localizedMessage ?: exception.message}")
                        }
                    )
                } else {
                    android.util.Log.e("AuthViewModel", "Update progress gagal: UID null")
                }
            } catch (e: Exception) {
                android.util.Log.e("AuthViewModel", "Update progress ke Firestore ERROR: ${e.localizedMessage ?: e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                firebaseRepository.signOut()
                userPreferences?.setUserLoggedIn(false)
                userPreferences?.setCurrentUserId("")
                _currentUser.value = null
            } catch (e: Exception) { }
        }
    }

    fun isUserLoggedIn(): Boolean {
        return firebaseRepository.getCurrentUser() != null
    }

    fun updateUserLesson(currentLesson: Int) {
        viewModelScope.launch {
            try {
                val uid = _currentUser.value?.uid
                    ?: com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    val result = firebaseRepository.updateUserLesson(uid, currentLesson)
                    result.fold(
                        onSuccess = {
                            val oldUser = _currentUser.value
                            if (oldUser != null) {
                                _currentUser.value = oldUser.copy(currentLesson = currentLesson)
                            }
                        },
                        onFailure = { exception ->
                            android.util.Log.e("AuthViewModel", "Update currentLesson ke Firestore GAGAL: ${exception.localizedMessage ?: exception.message}")
                        }
                    )
                } else {
                    android.util.Log.e("AuthViewModel", "Update currentLesson gagal: UID null")
                }
            } catch (e: Exception) {
                android.util.Log.e("AuthViewModel", "Update currentLesson ke Firestore ERROR: ${e.localizedMessage ?: e.message}")
            }
        }
    }

    fun updateUserUsername(username: String) {
        viewModelScope.launch {
            try {
                val uid = _currentUser.value?.uid
                    ?: com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    val result = firebaseRepository.updateUserUsername(uid, username)
                    result.fold(
                        onSuccess = {
                            val oldUser = _currentUser.value
                            if (oldUser != null) {
                                _currentUser.value = oldUser.copy(username = username)
                            }
                            userPreferences?.setUsername(username)
                            android.util.Log.d("AuthViewModel", "Update username ke Firestore BERHASIL: $username")
                        },
                        onFailure = { exception ->
                            android.util.Log.e("AuthViewModel", "Update username ke Firestore GAGAL: ${exception.localizedMessage ?: exception.message}")
                        }
                    )
                } else {
                    android.util.Log.e("AuthViewModel", "Update username gagal: UID null")
                }
            } catch (e: Exception) {
                android.util.Log.e("AuthViewModel", "Update username ke Firestore ERROR: ${e.localizedMessage ?: e.message}")
            }
        }
    }

    fun refreshUserData() {
        viewModelScope.launch {
            try {
                val firebaseUser = firebaseRepository.getCurrentUser()
                if (firebaseUser != null) {
                    val userDataResult = firebaseRepository.getUserData(firebaseUser.uid)
                    userDataResult.fold(
                        onSuccess = { appUser ->
                            // Update UserPreferences dengan data terbaru dari Firestore
                            userPreferences?.setUsername(appUser.username)
                            userPreferences?.setUserLevel(appUser.level)
                            userPreferences?.setUserXp(appUser.experience)
                            userPreferences?.setUserDays(appUser.streak)
                            userPreferences?.setTotalXp(appUser.totalScore)
                            userPreferences?.setSelectedLanguage(appUser.selectedLanguage)
                            userPreferences?.setCurrentLesson(appUser.currentLesson)
                            _currentUser.value = appUser
                            android.util.Log.d("AuthViewModel", "Refresh user data BERHASIL: ${appUser.username}")
                        },
                        onFailure = { exception ->
                            android.util.Log.e("AuthViewModel", "Refresh user data GAGAL: ${exception.localizedMessage ?: exception.message}")
                        }
                    )
                }
            } catch (e: Exception) {
                android.util.Log.e("AuthViewModel", "Refresh user data ERROR: ${e.localizedMessage ?: e.message}")
            }
        }
    }
}