package edu.upenn.cis350.irally.data.model

data class Comment(
    val eventId: String,
    val userId: String,
    val message: String,
    val replies: MutableSet<Comment>?
)