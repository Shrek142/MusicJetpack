package com.example.musicappmvvmjetpack.repository

import android.app.Activity
import com.example.musicappmvvmjetpack.Model.User
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
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

    fun updateUserEmail(newEmail: String, onResult: (Boolean) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user.updateEmail(newEmail)
                .addOnCompleteListener { task ->
                    onResult(task.isSuccessful)
                }
        } else {
            onResult(false)
        }
    }


    fun loginWithGoogleCredential(idToken: String, onResult: (FirebaseUser?, Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        // Tạo đối tượng User
                        val userData = User(
                            uid = user.uid,
                            username = user.displayName ?: "",
                            email = user.email ?: "",
                            phoneNumber = user.phoneNumber ?: "",
                            photoUrl = user.photoUrl?.toString() ?: "",
                            favoriteMusicId = emptyList()
                        )

                        // Lưu vào Firestore nếu chưa có
                        val userDoc = FirebaseFirestore.getInstance().collection("users").document(user.uid)
                        userDoc.get().addOnSuccessListener { snapshot ->
                            if (!snapshot.exists()) {
                                userDoc.set(userData)
                            }
                        }

                        onResult(user, true)
                    } else {
                        onResult(null, false)
                    }
                } else {
                    onResult(null, false)
                }
            }
    }

    fun loginWithFacebookCredential(token: AccessToken, onResult: (Boolean) -> Unit) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }

    fun loginWithGitHub(activity: Activity, onResult: (Boolean, String?) -> Unit) {
        val provider = OAuthProvider.newBuilder("github.com")
        provider.addCustomParameter("allow_signup", "false")
        provider.setScopes(listOf("user:email"))

        val auth = FirebaseAuth.getInstance()
        val pending = auth.pendingAuthResult

        if (pending != null) {
            pending
                .addOnSuccessListener { onResult(true, null) }
                .addOnFailureListener { onResult(false, it.message) }
        } else {
            auth.startActivityForSignInWithProvider(activity, provider.build())
                .addOnSuccessListener { authResult ->
                    // Lưu thông tin người dùng vào Firestore nếu chưa có
                    val user = authResult.user
                    val uid = user?.uid ?: return@addOnSuccessListener
                    val userDoc = FirebaseFirestore.getInstance().collection("users").document(uid)

                    userDoc.get().addOnSuccessListener { doc ->
                        if (!doc.exists()) {
                            val newUser = User(
                                uid = uid,
                                email = user.email ?: "",
                                username = user.displayName ?: "GitHub User",
                                phoneNumber = "",
                                photoUrl = user.photoUrl?.toString() ?: ""
                            )
                            userDoc.set(newUser)
                        }
                    }

                    onResult(true, null)
                }
                .addOnFailureListener { onResult(false, it.message) }
        }
    }


    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

}

