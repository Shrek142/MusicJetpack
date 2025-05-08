package com.example.musicappmvvmjetpack.ViewModel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicappmvvmjetpack.Model.User
import com.example.musicappmvvmjetpack.repository.AuthRepository
import com.facebook.AccessToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _authResult = MutableStateFlow<Boolean?>(null)
    val authResult: StateFlow<Boolean?> = _authResult

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _updateResult = MutableStateFlow<Boolean?>(null)
    val updateResult: StateFlow<Boolean?> = _updateResult

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = repository.getCurrentUser()
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            val result = repository.updateUserProfile(user)
            _updateResult.value = result.isSuccess
            Log.d("AuthViewModel", "Update user result: ${result.isSuccess}, language: ${user.language}")
            if (result.isSuccess) _currentUser.value = user
        }
    }

    fun register(email: String, password: String, username: String, phoneNumber: String) {
        viewModelScope.launch {
            val result = repository.register(email, password, username, phoneNumber)
            _authResult.value = result.isSuccess
            _errorMessage.value = result.exceptionOrNull()?.message
            if (result.isSuccess) loadCurrentUser()
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val success = repository.login(email, password)
            _authResult.value = success
            if (success) loadCurrentUser()
        }
    }

    fun updateUserEmailAndFirestore(newEmail: String) {
        viewModelScope.launch {
            repository.updateUserEmail(newEmail) { success ->
                if (success) {
                    val updated = _currentUser.value?.copy(email = newEmail)
                    if (updated != null) updateUser(updated)
                } else {
                    _errorMessage.value = "Không thể cập nhật email đăng nhập"
                }
            }
        }
    }
    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            repository.loginWithGoogleCredential(idToken) { firebaseUser, success ->
                if (success && firebaseUser != null) {
                    val user = User(
                        uid = firebaseUser.uid,
                        username = firebaseUser.displayName ?: "",
                        email = firebaseUser.email ?: "",
                        phoneNumber = firebaseUser.phoneNumber ?: "",
                        photoUrl = firebaseUser.photoUrl?.toString() ?: "",
                        favoriteMusicId = emptyList()
                    )

                    _currentUser.value = user
                }
                _authResult.value = success
            }
        }
    }

    fun loginWithFacebook(token: AccessToken) {
        repository.loginWithFacebookCredential(token) { success ->
            _authResult.value = success
        }
    }
    fun loginWithGitHub(activity: Activity) {
        repository.loginWithGitHub(activity) { success, error ->
            _authResult.value = success
            _errorMessage.value = error
            if (success) {
                loadCurrentUser()
            }
        }
    }

    fun resetAuthResult() {
        _authResult.value = null
        _errorMessage.value = null
    }

    fun logout() {
        repository.logout()
        _currentUser.value = null
    }
}
