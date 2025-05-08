package com.example.musicappmvvmjetpack.Model

data class User(
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val phoneNumber: String = "",
    val photoUrl: String = "",
    val favoriteMusicId: List<String> = emptyList(),
    val language: String = "vi"
)