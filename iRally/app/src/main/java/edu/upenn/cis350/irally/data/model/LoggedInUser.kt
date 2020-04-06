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
    var profilePictureLink: String?,
    val interests: MutableSet<String>?,
    val eventsAttended: MutableSet<Event>?,
    val eventsToAttend: MutableSet<Event>?,
    var numEventsCreated: Int,
    val adminsFollowed: MutableSet<Event>?,
    val friends: MutableSet<LoggedInUser>?
)
