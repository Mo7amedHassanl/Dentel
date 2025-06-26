package com.m7md7sn.dentel.data.repository

import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.m7md7sn.dentel.utils.Result

interface AuthRepository {
    val currentUser: FirebaseUser?

    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun signup(email: String, password: String): Result<FirebaseUser>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    suspend fun reloadUser(): Result<Unit>
    suspend fun sendEmailVerification(): Result<Unit>
    suspend fun updateUserProfile(displayName: String): Result<Unit>
    suspend fun logout()
    suspend fun updatePassword(newPassword: String): Result<Unit>
    suspend fun updateEmail(newEmail: String): Result<Unit>
    suspend fun reauthenticate(currentEmail: String, currentPassword: String): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
    fun getGoogleSignInIntent(): Intent
    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser>
}
