package edu.upenn.cis350.irally.data.model

data class Event(
    val eventId: String,
    val creator: LoggedInUser,
    val description: String,
    val address: String,
    val dateTime: String,
    val attendees: String?,
    val numberOfAttendees: Int,
    val interestsOfAttendees: MutableSet<String>
)