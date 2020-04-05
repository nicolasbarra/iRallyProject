package edu.upenn.cis350.irally.data.model

/**
 * User details post authentication
 */
data class LoggedInUser(
    val userId: String,
    val displayName: String,
    val email: String,
    val gender: String,
    val genderPronouns: String,
    val profilePictureLink: String?,
    val interests: MutableSet<String>?
)
