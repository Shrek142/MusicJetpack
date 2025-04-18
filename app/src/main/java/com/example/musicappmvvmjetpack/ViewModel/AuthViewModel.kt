package com.example.musicappmvvmjetpack.ViewModel

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

    fun loadCurrentUser() {
        viewModelScope.launch {
            val user = repository.getCurrentUser()  // Gọi repository để lấy thông tin người dùng
            _currentUser.value = user  // Lưu vào StateFlow để UI có thể quan sát
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            val result = repository.updateUserProfile(user)
            _updateResult.value = result.isSuccess
        }
    }


    fun register(email: String, password: String, username: String, phoneNumber: String) {
        viewModelScope.launch {
            val result = repository.register(email, password, username, phoneNumber)
            _authResult.value = result.isSuccess
            _errorMessage.value = result.exceptionOrNull()?.message
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val success = repository.login(email, password)
            _authResult.value = success
        }
    }

    fun resetAuthResult() {
        _authResult.value = null
        _errorMessage.value = null
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
    fun updateUserEmailAndFirestore(newEmail: String) {
        val currentUser = _currentUser.value ?: return

        repository.updateUserEmail(newEmail) { success ->
            if (success) {
                val updated = currentUser.copy(email = newEmail)
                updateUser(updated)
            } else {
                _errorMessage.value = "Không thể cập nhật email đăng nhập"
            }
        }
    }
    fun logout() {
        repository.logout()
        _currentUser.value = null
    }

}
