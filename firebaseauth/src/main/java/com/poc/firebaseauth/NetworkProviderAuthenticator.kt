package com.poc.firebaseauth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser


class NetworkProviderAuthenticator(private val applicationContext: Context) :
    FirebaseCredentialsProvider, GoogleLoginProvider {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var mGoogleSign: GoogleSignInClient? = null

    private val gson: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("413849803669-br4ptlt1tjstn6g10gnovmjhoac5sj15.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }

    override fun onSign(activity: AppCompatActivity, requestCode: Int) {
        mGoogleSign = GoogleSignIn.getClient(applicationContext, gson)
        val intent = mGoogleSign?.signInIntent
        intent?.let {
            activity.startActivityForResult(intent, requestCode)
        }

    }

    override fun getDataIntentSignIn(
        data: Intent,
        onSuccess: (GoogleLoginSingInDTO) -> Unit,
        onFailure: () -> Unit
    ) {

        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account: GoogleSignInAccount = task.getResult(
                ApiException::class.java
            )
            onSuccess.invoke(GoogleLoginSingInDTO(account.email, account.id))
        } catch (e: Exception) {
            onFailure.invoke()
        }
    }

    override fun onRegisterUser(
        email: String,
        password: String,
        onFailure: (RegisterUserFirebaseEnum) -> Unit,
        onSuccess: (String) -> Unit
    ) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sendEmailVerification {
                    onSuccess.invoke(it)
                }
            } else {
                val exception = task.exception;
                if (exception is FirebaseAuthException && exception.errorCode == RegisterUserFirebaseEnum.ERROR_EMAIL_ALREADY_IN_USE.name) {
                    onFailure.invoke(RegisterUserFirebaseEnum.ERROR_EMAIL_ALREADY_IN_USE)
                } else if (exception is FirebaseAuthException && exception.errorCode == RegisterUserFirebaseEnum.ERROR_WEAK_PASSWORD.name) {
                    onFailure.invoke(RegisterUserFirebaseEnum.ERROR_WEAK_PASSWORD)
                } else {
                    onFailure.invoke(RegisterUserFirebaseEnum.ERROR_UNDEFINED)
                }
            }
        }
    }

    override fun onLoginUser(email: String, password: String, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            task ->

            if(task.isSuccessful) {
                val firebaseCurrentUser = firebaseAuth.currentUser
                if(firebaseCurrentUser != null && firebaseCurrentUser.isEmailVerified) {
                   onSuccess.invoke(firebaseCurrentUser.uid)
                }
                if(firebaseCurrentUser!=null && !firebaseCurrentUser.isEmailVerified) {
                    onFailure.invoke("Não foi possivel logar, email não verificado!")
                }
            } else {
                onFailure.invoke("Falha ao logar, tente novamente!")
            }
        }
    }

    private fun sendEmailVerification(onResult: (String) -> Unit) {
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult.invoke("Conta criada com sucesso, verifique seu email!")
                }
            }
        }
    }
}