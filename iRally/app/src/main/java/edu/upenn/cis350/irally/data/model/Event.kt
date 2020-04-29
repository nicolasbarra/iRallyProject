package edu.upenn.cis350.irally.data.model

import edu.upenn.cis350.irally.data.repository.EventRepository

data class Event(
    val eventId: String,
    val creator: String,
    val description: String,
    val address: String,
    val dateTime: String,
    val attendees: MutableSet<String>,
    val numberOfAttendees: Int,
    val interestsOfAttendees: MutableSet<String>,
    val comments: MutableSet<Comment>
)

//todo: here's what im thinking for comments! this would go in EventPage Activity
//
//    for (i in 0 until EventRepository.eventSelected.comments.size) {
//        val parentComment = TextView(this)
//        parentComment.text =
//            EventRepository.eventSelected.comments.elementAt(i).userId + " says: " +
//                    EventRepository.eventSelected.comments.elementAt(i).message
//        comment_layout.addView(parentComment)
//
//        for (j in 0 until EventRepository.eventSelected.comments.elementAt(i).replies.size) {
//            val replyComment = TextView(this)
//            replyComment.text =
//                EventRepository.eventSelected.comments.elementAt(i).replies.elementAt(j).message + "'s reply: " +
//                        EventRepository.eventSelected!!.comments.elementAt(i).replies?.elementAt(j).message
//            comment_layout.addView(replyComment)
//        }
//
//        val replyBox = EditText(this)
//        val replySend = Button(this)
//        replySend.text = "Submit reply"
//        replySend.setOnClickListener {
//            //todo: JSON request?? you're gonna have to add this reply to the current comment's reply set
//
//        }
//        comment_layout.addView(replyBox)
//        comment_layout.addView(replySend)
//    }
//

