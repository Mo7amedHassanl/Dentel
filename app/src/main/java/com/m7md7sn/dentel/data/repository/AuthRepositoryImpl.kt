package com.m7md7sn.dentel.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuthException
import com.m7md7sn.dentel.utils.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Result<FirebaseUser> = withContext(Dispatchers.IO) {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                Result.Success(it)
            } ?: Result.Error("Login failed: User is null")
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is FirebaseAuthException -> {
                    when (e.errorCode) {
                        "ERROR_INVALID_EMAIL" -> "Invalid email address."
                        "ERROR_WRONG_PASSWORD" -> "Wrong password. Please try again."
                        "ERROR_USER_NOT_FOUND" -> "User not found. Please sign up."
                        "ERROR_USER_DISABLED" -> "This account has been disabled."
                        "ERROR_TOO_MANY_REQUESTS" -> "Too many requests. Please try again later."
                        else -> e.localizedMessage ?: "An unexpected error occurred."
                    }
                }
                else -> e.localizedMessage ?: "An unexpected error occurred."
            }
            Result.Error(errorMessage, e)
        }
    }

    override suspend fun signup(email: String, password: String): Result<FirebaseUser> = withContext(Dispatchers.IO) {
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let {
                Result.Success(it)
            } ?: Result.Error("Signup failed: User is null")
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is FirebaseAuthException -> {
                    when (e.errorCode) {
                        "ERROR_WEAK_PASSWORD" -> "Password is too weak. Please use a stronger password."
                        "ERROR_INVALID_EMAIL" -> "Invalid email address."
                        "ERROR_EMAIL_ALREADY_IN_USE" -> "This email is already in use. Please sign in or use a different email."
                        else -> e.localizedMessage ?: "An unexpected error occurred."
                    }
                }
                else -> e.localizedMessage ?: "An unexpected error occurred."
            }
            Result.Error(errorMessage, e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is FirebaseAuthException -> {
                    when (e.errorCode) {
                        "ERROR_INVALID_EMAIL" -> "Invalid email address."
                        "ERROR_USER_NOT_FOUND" -> "No user found with this email."
                        else -> e.localizedMessage ?: "An unexpected error occurred."
                    }
                }
                else -> e.localizedMessage ?: "An unexpected error occurred."
            }
            Result.Error(errorMessage, e)
        }
    }

    override suspend fun reloadUser(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            currentUser?.reload()?.await()
            Result.Success(Unit)
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is FirebaseAuthException -> e.localizedMessage ?: "Failed to reload user. Please try again."
                else -> e.localizedMessage ?: "An unexpected error occurred."
            }
            Result.Error(errorMessage, e)
        }
    }

    override suspend fun sendEmailVerification(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            currentUser?.sendEmailVerification()?.await()
            Result.Success(Unit)
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is FirebaseAuthException -> e.localizedMessage ?: "Failed to send email verification. Please try again."
                else -> e.localizedMessage ?: "An unexpected error occurred."
            }
            Result.Error(errorMessage, e)
        }
    }

    override suspend fun logout() = withContext(Dispatchers.IO) {
        firebaseAuth.signOut()
    }

    override suspend fun updateUserProfile(displayName: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val user = firebaseAuth.currentUser
            if (user != null) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()
                user.updateProfile(profileUpdates).await()
                Result.Success(Unit)
            } else {
                Result.Error("User not logged in.")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Failed to update profile.", e)
        }
    }
} 