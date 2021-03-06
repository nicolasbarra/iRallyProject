package edu.upenn.cis350.irally.data.load

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.data.model.Comment
import edu.upenn.cis350.irally.data.model.Event
import edu.upenn.cis350.irally.data.repository.EventRepository
import edu.upenn.cis350.irally.ui.event.EventPageActivity
import edu.upenn.cis350.irally.ui.login.LoginActivity
import org.json.JSONArray
import org.json.JSONObject

fun loadEventInfo(
    view: View,
    eventText: String,
    packageContext: Context,
    applicationContext: Context
) {
    val eventId = eventText.substring(0, eventText.lastIndexOf(" on "))

    val eventRequestBody = JSONObject()
    eventRequestBody.put("eventId", eventId)

    val eventJsonObjectRequest = JsonObjectRequest(
        "http://10.0.2.2:9000/events/",
        eventRequestBody,
        Response.Listener { response ->
            Log.v("PROCESS", "got a response (register")
            Log.v("RESPONSE", response.toString())
            if (response.getString("status") == "Success") {
                Log.v("Response Success", "Event received")
                val eventJSON = response.getJSONObject("event")
                val interestsOfAttendeesJSONArray: JSONArray =
                    eventJSON.getJSONArray("interestsOfAttendees")
                val interestsOfAttendees = mutableSetOf<String>()
                for (j in 0 until interestsOfAttendeesJSONArray.length()) {
                    interestsOfAttendees.add(
                        interestsOfAttendeesJSONArray.get(j).toString()
                    )
                }
                val attendeesStringsJSONArray =
                    if (eventJSON.has("attendeesStrings")) {
                        eventJSON.getJSONArray("attendeesStrings")
                    } else {
                        JSONArray()
                    }
                val attendeesStrings = mutableSetOf<String>()
                for (k in 0 until attendeesStringsJSONArray.length()) {
                    attendeesStrings.add(
                        attendeesStringsJSONArray.get(k).toString()
                    )
                }
                val commentsStringsJSONArray =
                    if (eventJSON.has("commentsStrings")) {
                        eventJSON.getJSONArray("commentsStrings")
                    } else {
                        JSONArray()
                    }
                Log.v("commentsStringsJSONArray is", commentsStringsJSONArray.toString())
                val commentsComments = mutableSetOf<Comment>()
                for (k in 0 until commentsStringsJSONArray.length()) {
                    val commentUserRepliesCombo =
                        commentsStringsJSONArray.get(k).toString().split("&&")
                    for (comUse in commentUserRepliesCombo) {
                        comUse.trim()
                    }
                    val commentingUser = commentUserRepliesCombo[0]
                    val commentText = commentUserRepliesCombo[1]
                    Log.v("commentUserRepliesCombo[2] is", commentUserRepliesCombo[2].toString())
                    if (commentUserRepliesCombo[2].isNotEmpty()) {
                        val repliesUsers = commentUserRepliesCombo[2].split("~")
                        val replyComments = mutableSetOf<Comment>()
                        Log.v("repliesUsers is", repliesUsers.toString())
                        for (replyUser in repliesUsers) {
                            if (replyUser.isNotEmpty()) {
                                val replyUserArr = replyUser.split("::")
                                val replyingUser = replyUserArr[0]
                                Log.v("replyUserArr is", replyUserArr.toString())
                                val replyingComment = replyUserArr[1]
                                replyComments.add(
                                    Comment(
                                        eventText,
                                        replyingUser,
                                        replyingComment,
                                        null
                                    )
                                )
                            }
                        }
                        commentsComments.add(
                            Comment(
                                eventText,
                                commentingUser,
                                commentText,
                                replyComments
                            )
                        )
                    }
                    commentsComments.add(
                        Comment(
                            eventText,
                            commentingUser,
                            commentText,
                            null
                        )
                    )
                }
                val newEvent = Event(
                    eventJSON.getString("eventId"),
                    eventJSON.getString("creatorId"),
                    eventJSON.getString("description"),
                    eventJSON.getString("address"),
                    eventJSON.getString("dateTime"),
                    attendeesStrings,
                    eventJSON.getInt("numberOfAttendees"),
                    interestsOfAttendees,
                    commentsComments
                )
                EventRepository.eventSelected = newEvent
                val intent = Intent(packageContext, EventPageActivity::class.java)
                packageContext.startActivity(intent)
            } else {
                Log.v("Response Success", "Event not found")
                Log.v("ERROR", response.getString("errors"))
                Toast.makeText(
                    applicationContext,
                    "Unable to load event: ${response.getString("errors")}",
                    Toast.LENGTH_LONG
                ).show()
            }
        },
        Response.ErrorListener { error ->
            Log.e("Response Error", "Error occurred", error)
            Toast.makeText(
                applicationContext,
                "Unable to load event: ${error.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    )

    RequestQueueSingleton.getInstance(
        LoginActivity.context
    )
        .addToRequestQueue(eventJsonObjectRequest)

    //
    //                        Response.ErrorListener { error ->
    //                            Toast.makeText(
    //                                applicationContext,
    //                                ("Network connection error. Please try again. " +
    //                                        "Error: %s").format(error.toString()),
    //                                Toast.LENGTH_LONG
    //                            ).show()
    //
    //                        }
    //                    )
}