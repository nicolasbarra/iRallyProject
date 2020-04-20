package edu.upenn.cis350.irally.data.model

/**
 * Admin details
 */
data class Admin(
    val username: String,
    val displayName: String,
    val description: String,
    val politicalAffiliation: String,
    val goals: MutableSet<String>,
    var interests: MutableSet<String>,
    val profilePictureLink: String?,
    val eventsToHost: MutableSet<Event>?,
    val eventsHosted: MutableSet<String>?,
    var numEventsCreated: Int
)
