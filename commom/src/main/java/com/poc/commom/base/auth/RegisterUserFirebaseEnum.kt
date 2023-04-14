package com.poc.commom.base.auth

enum class RegisterUserFirebaseEnum(s: String) {
    ERROR_EMAIL_ALREADY_IN_USE("ERROR_EMAIL_ALREADY_IN_USE"),
    ERROR_WEAK_PASSWORD("ERROR_WEAK_PASSWORD"),
    ERROR_UNDEFINED("ERROR_UNDEFINED")
}