package edu.upenn.cis350.irally.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userId: String,
    val displayName: String,
    val email: String,
    val gender: String,
    val genderPronouns: String,
    val profilePictureLink: String?
)
