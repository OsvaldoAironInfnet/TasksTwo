package com.poc.firebaseauth

interface FirebaseCredentialsProvider {
    fun onRegisterUser(email: String, password: String,onFailure: (RegisterUserFirebaseEnum) -> Unit,onSuccess: (String) -> Unit)
    fun onLoginUser(email: String, password: String,onSuccess: (String) -> Unit, onFailure: (String) -> Unit)
}