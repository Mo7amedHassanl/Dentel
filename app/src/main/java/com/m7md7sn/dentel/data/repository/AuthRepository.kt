package com.m7md7sn.dentel.data.repository

import com.google.firebase.auth.FirebaseUser
import com.m7md7sn.dentel.utils.Result

interface AuthRepository {
    val currentUser: FirebaseUser?

    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun signup(email: String, password: String): Result<FirebaseUser>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    suspend fun reloadUser(): Result<Unit>
    suspend fun sendEmailVerification(): Result<Unit>
    suspend fun logout()
} 