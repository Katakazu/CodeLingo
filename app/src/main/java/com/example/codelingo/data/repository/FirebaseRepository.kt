package com.example.codelingo.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.example.codelingo.data.model.AppUser
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FirebaseRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    
    // Authentication methods
    suspend fun signUp(email: String, password: String, username: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                // Create user document in Firestore
                val appUser = AppUser(
                    uid = user.uid,
                    email = email,
                    username = username,
                    selectedLanguage = "",
                    level = 1,
                    experience = 0,
                    totalScore = 0,
                    currentLesson = 1,
                    firstTime = true
                )
                firestore.collection("users").document(user.uid).set(appUser).await()
            }
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signOut() {
        auth.signOut()
    }
    
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
    
    // User data methods
    suspend fun getUserData(uid: String): Result<AppUser> {
        return try {
            val document = firestore.collection("users").document(uid).get().await()
            val user = document.toObject(AppUser::class.java)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateUserData(user: AppUser): Result<Unit> {
        return try {
            firestore.collection("users").document(user.uid).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateUserLanguage(uid: String, language: String): Result<Unit> {
        return try {
            firestore.collection("users").document(uid).update("selectedLanguage", language).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateUserProgress(uid: String, experience: Int, level: Int, totalScore: Int): Result<Unit> {
        return try {
            val updates = mapOf(
                "experience" to experience,
                "level" to level,
                "totalScore" to totalScore
            )
            firestore.collection("users").document(uid).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateUserLesson(uid: String, currentLesson: Int): Result<Unit> {
        return try {
            firestore.collection("users").document(uid).update("currentLesson", currentLesson).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateUserUsername(uid: String, username: String): Result<Unit> {
        return try {
            firestore.collection("users").document(uid).update("username", username).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Leaderboard methods
    fun getLeaderboard(): Flow<List<AppUser>> = flow {
        try {
            val snapshot = firestore.collection("users")
                .orderBy("totalScore", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .await()
            
            val users = snapshot.documents.mapNotNull { doc ->
                doc.toObject(AppUser::class.java)
            }
            emit(users)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
    
    // Quest/Challenge methods
    suspend fun getDailyQuests(): Result<List<Map<String, Any>>> {
        return try {
            val snapshot = firestore.collection("quests")
                .whereEqualTo("type", "daily")
                .get()
                .await()
            
            val quests = snapshot.documents.map { doc ->
                doc.data ?: emptyMap()
            }
            Result.success(quests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun completeQuest(uid: String, questId: String, reward: Int): Result<Unit> {
        return try {
            val userRef = firestore.collection("users").document(uid)
            firestore.runTransaction { transaction ->
                val userDoc = transaction.get(userRef)
                val currentExp = userDoc.getLong("experience") ?: 0
                val currentScore = userDoc.getLong("totalScore") ?: 0
                
                transaction.update(userRef, mapOf(
                    "experience" to (currentExp + reward),
                    "totalScore" to (currentScore + reward)
                ))
                
                // Mark quest as completed
                transaction.set(
                    firestore.collection("userQuests").document("${uid}_${questId}"),
                    mapOf(
                        "uid" to uid,
                        "questId" to questId,
                        "completedAt" to com.google.firebase.Timestamp.now()
                    )
                )
            }.await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEmailByUsername(username: String): Result<String> {
        return try {
            val snapshot = firestore.collection("users")
                .whereEqualTo("username", username)
                .get()
                .await()
            val userDoc = snapshot.documents.firstOrNull()
            val email = userDoc?.getString("email")
            if (email != null) {
                Result.success(email)
            } else {
                Result.failure(Exception("Username tidak ditemukan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 