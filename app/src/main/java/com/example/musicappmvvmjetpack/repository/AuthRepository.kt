package com.example.musicappmvvmjetpack.repository

import com.example.musicappmvvmjetpack.Model.User
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun register(email: String, password: String, username: String, phoneNumber: String): Result<Boolean> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("Không thể lấy UID"))

            val user = User(uid, email, username, phoneNumber)

            try {
                db.collection("users").document(uid).set(user).await()
                Result.success(true)
            } catch (e: Exception) {
                //lưu Firestore thất bại vẫn đã tạo user Firebase
                Result.failure(Exception("không lưu được thông tin người dùng."))
            }

        } catch (e: Exception) {
            // createUser thất bại
            Result.failure(e)
        }
    }


    suspend fun login(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            false
        }
    }
    suspend fun getCurrentUser(): User? {
        val uid = auth.currentUser?.uid ?: return null
        val doc = db.collection("users").document(uid).get().await()  // Lấy dữ liệu từ Firestore
        return doc.toObject(User::class.java)  // Chuyển dữ liệu thành đối tượng User
    }
    suspend fun updateUserProfile(updatedUser: User): Result<Boolean> {
        return try {
            db.collection("users").document(updatedUser.uid).set(updatedUser).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun loginWithGoogleCredential(idToken: String, onResult: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }

    fun loginWithFacebookCredential(token: AccessToken, onResult: (Boolean) -> Unit) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }

}

