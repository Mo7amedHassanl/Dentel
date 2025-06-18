package com.m7md7sn.dentel.data.model

/**
 * Data model representing a user profile
 */
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null
)
