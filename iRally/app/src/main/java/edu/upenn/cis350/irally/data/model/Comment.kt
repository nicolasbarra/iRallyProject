package edu.upenn.cis350.irally.data.model

data class Comment(
    val eventId: String,
    val replies: MutableSet<Comment>?
)