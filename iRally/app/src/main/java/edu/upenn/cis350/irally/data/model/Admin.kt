package edu.upenn.cis350.irally.data.model

/**
 * Admin details
 */
data class Admin(
    val username: String,
    val adminInfo: String,
    val description: String,
    val politicalAffiliation: String,
    val goals: String,
    var interests: String,
    val profilePictureLink: String?,
    val eventsToHost: MutableSet<Event>?,
    val eventsHosted: MutableSet<String>?,
    var numEventsCreated: Int
)
