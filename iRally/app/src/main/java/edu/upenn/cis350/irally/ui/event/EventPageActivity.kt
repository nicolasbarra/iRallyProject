package edu.upenn.cis350.irally.ui.event

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.R.*
import edu.upenn.cis350.irally.data.repository.EventRepository
import edu.upenn.cis350.irally.data.repository.LoginRepository
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.data.load.loadProfileInfo
import edu.upenn.cis350.irally.data.model.Comment
import edu.upenn.cis350.irally.ui.feed.FeedActivity
import edu.upenn.cis350.irally.ui.login.LoginActivity
import edu.upenn.cis350.irally.ui.profile.ProfileActivity
import edu.upenn.cis350.irally.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_event_page.*
import kotlinx.android.synthetic.main.activity_user.*
import org.json.JSONObject

class EventPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_page)

        //toolbar
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM;
        supportActionBar?.setCustomView(R.layout.toolbar);

        event_page_name.text =
            EventRepository.eventSelected?.eventId ?: "Error: Unable to set event."

        event_page_creator.text =
            EventRepository.eventSelected?.creator ?: "Error: creator cannot be found"

        event_page_description.text =
            EventRepository.eventSelected?.description ?: "Error: description cannot be found"

        event_page_address.text =
            EventRepository.eventSelected?.address ?: "Error: address cannot be found"

        event_page_date.text =
            EventRepository.eventSelected?.dateTime ?: "Error date and time cannot be found"

        previous_comments_title.text = "See what people are saying about " + event_page_name.text

        if (!EventRepository.eventSelected?.attendees.isNullOrEmpty()) {
            for (i in 0 until EventRepository.eventSelected!!.attendees.size) {
                if (i == 0) {
                    val attendeeText = EventRepository.eventSelected!!.attendees.elementAt(0)
                    attendee1.text = attendeeText
                    attendee1.setOnClickListener {
                        loadProfileInfo(
                            it,
                            attendeeText,
                            this,
                            applicationContext
                        )
                    }
                } else {
                    val myButton = Button(this)
                    val attendeeText = EventRepository.eventSelected!!.attendees.elementAt(i)
                    myButton.text = attendeeText
                    myButton.setOnClickListener {
                        loadProfileInfo(
                            it,
                            attendeeText,
                            this,
                            applicationContext
                        )
                    }
                    attendees_layout.addView(myButton)
                }
            }
        }

        if (!EventRepository.eventSelected!!.comments.isNullOrEmpty()) {
            for (i in 0 until EventRepository.eventSelected!!.comments.size) {
                val currComment = EventRepository.eventSelected!!.comments.elementAt(i)
                val myLayout = LinearLayout(this)
                myLayout.orientation = LinearLayout.VERTICAL
                val parentComment = TextView(this)
                parentComment.text =
                    currComment.userId + " says: " +
                            currComment.message
                myLayout.addView(parentComment)
                if (!currComment.replies.isNullOrEmpty()) {
                    for (j in 0 until EventRepository.eventSelected!!.comments.elementAt(i).replies!!.size) {
                        val replyComment = TextView(this)
                        replyComment.text =
                            "\t" + "\t" + "\t" + "\t" + "\t" +"\t" + currComment.replies?.elementAt(j)?.userId + "'s reply: " +
                                    currComment.replies?.elementAt(j)?.message + "\n"
                        myLayout.addView(replyComment)
                    }
                }
                val replyBox = EditText(this)
                val replySend = Button(this)
                replySend.text = "Submit reply"
                replySend.setOnClickListener {
                    if (replyBox.text.isEmpty()) {
                        Toast.makeText(
                            applicationContext,
                            "Type a reply first.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        val replyRequestBody1 = JSONObject()
                        replyRequestBody1.put("username", LoginRepository.user?.userId)
                        replyRequestBody1.put("originalComment", currComment.message)
                        replyRequestBody1.put(
                            "eventId",
                            EventRepository.eventSelected?.eventId
                        )
                        replyRequestBody1.put("isReply", "True")
                        replyRequestBody1.put("poster", currComment.userId)
                        replyRequestBody1.put("comment", replyBox.text)

                        val jsonObjectRequestReply1 = JsonObjectRequest(
                            "http://10.0.2.2:9000/events/addComment",
                            replyRequestBody1,
                            Response.Listener { response ->
                                Log.v("PROCESS", "got a response")
                                Log.v("RESPONSE", response.toString())
                                if (response.getString("status") == "Success") {
                                    Log.v("Response Success", "Wrote reply")
                                    val newReply1 = Comment(
                                        EventRepository.eventSelected?.eventId!!,
                                        LoginRepository.user?.userId!!,
                                        replyBox.text.toString(),
                                        null
                                    )
                                    if (currComment.replies == null) {
                                        currComment.replies = mutableSetOf(newReply1)
                                    } else {
                                        currComment.replies!!.add(newReply1)
                                    }
                                    val replyCommentNew1 = TextView(this)
                                    replyCommentNew1.text = "\t" + "\t" + "\t" +"\t" + "\t" + "\t" + newReply1.userId + "'s reply: " + newReply1.message + "\n"
                                    myLayout.addView(replyCommentNew1)
                                    Toast.makeText(
                                        applicationContext,
                                        "Reply posted!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Log.v("Response Success", "User or Event not found")
                                    Log.v("ERROR", response.getString("errors"))
                                    Toast.makeText(
                                        applicationContext,
                                        "Unable to add reply: ${response.getString("errors")}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            },
                            Response.ErrorListener { error ->
                                Log.e("Response Error", "Error occurred", error)
                                Toast.makeText(
                                    applicationContext,
                                    "Unable to add reply: ${error.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        )
                        RequestQueueSingleton.getInstance(LoginActivity.context)
                            .addToRequestQueue(jsonObjectRequestReply1)
                    }
                }
                myLayout.addView(replyBox)
                myLayout.addView(replySend)
                comment_layout.addView(myLayout)
            }
        }

        val markAsGoing = event_page_button
        var attendingEventBool = false
        for (i in LoginRepository.user?.eventsToAttend!!) {
            if (i == EventRepository.eventSelected?.eventId) {
                attendingEventBool = true
            }
        }
        for (i in EventRepository.eventSelected?.attendees!!) {
            Log.v("The attendees are", i)
            Log.v("The logged in user is", LoginRepository.user!!.userId)
            if (i == LoginRepository.user!!.userId) {
                Log.v("it was true", "evaluateed if condition")
                attendingEventBool = true
            }
        }
        if (attendingEventBool) {
            markAsGoing.text = getString(string.going);
            markAsGoing.isEnabled = false;
        }
        markAsGoing.setOnClickListener {
            val eventRequestBody = JSONObject()
            val eventName = EventRepository.eventSelected?.eventId
            val username = LoginRepository.user?.userId
            eventRequestBody.put("username", LoginRepository.user?.userId)
            eventRequestBody.put("eventId", eventName)
            val eventJsonObjectRequest = JsonObjectRequest(
                "http://10.0.2.2:9000/events/addAttendee",
                eventRequestBody,
                Response.Listener { response ->
                    if (response.getString("status") == "Failure") {
                        val errors = response.getString("errors")
                        Log.v("Response Success", "Unable to join due to error")
                        Log.v("ERROR", errors)
                        Toast.makeText(
                            applicationContext,
                            "Unable to join event: $errors",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Log.v("PROCESS", "got a response (register")
                        Log.v("RESPONSE", response.toString())
                        markAsGoing.text = getString(string.going);
                        markAsGoing.isEnabled = false;
                        if (eventName != null) {
                            LoginRepository.user?.eventsToAttend?.add(
                                eventName + " on " + EventRepository.eventSelected?.dateTime
                            )
                        }
                        if (username != null) {
                            EventRepository.eventSelected?.attendees?.add(username)
                        }
                        for (i in 0 until EventRepository.eventSelected!!.attendees.size) {
                            if (i == 0) {
                                val attendeeText =
                                    EventRepository.eventSelected!!.attendees.elementAt(0)
                                attendee1.text = attendeeText
                                attendee1.setOnClickListener {
                                    loadProfileInfo(
                                        it,
                                        attendeeText,
                                        this,
                                        applicationContext
                                    )
                                }
                            } else {
                                val myButton = Button(this)
                                val attendeeText =
                                    EventRepository.eventSelected!!.attendees.elementAt(i)
                                myButton.text = attendeeText
                                myButton.setOnClickListener {
                                    loadProfileInfo(
                                        it,
                                        attendeeText,
                                        this,
                                        applicationContext
                                    )
                                }
                                attendees_layout.addView(myButton)
                            }
                        }
                        Toast.makeText(
                            applicationContext,
                            "Added you successfully!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Response Error", "Error occurred", error)
                    Toast.makeText(
                        applicationContext,
                        "Unable to add as attendee: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )

            RequestQueueSingleton.getInstance(LoginActivity.context)
                .addToRequestQueue(eventJsonObjectRequest)
        }


        comment_button.setOnClickListener {
            if (comment.text.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Type a comment first.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val requestBody = JSONObject()
                requestBody.put("username", LoginRepository.user?.userId)
                requestBody.put("comment", comment.text)
                requestBody.put("eventId", EventRepository.eventSelected?.eventId)
                requestBody.put("isReply", "False")

                val jsonObjectRequest = JsonObjectRequest(
                    "http://10.0.2.2:9000/events/addComment",
                    requestBody,
                    Response.Listener { response ->
                        Log.v("PROCESS", "got a response")
                        Log.v("RESPONSE", response.toString())
                        if (response.getString("status") == "Success") {
                            Log.v("Response Success", "Wrote comment")
                            val myLayout1 = LinearLayout(this)
                            myLayout1.orientation = LinearLayout.VERTICAL
                            val newComment = Comment(
                                EventRepository.eventSelected?.eventId!!,
                                LoginRepository.user?.userId!!,
                                comment.text.toString(),
                                null
                            )
                            EventRepository.eventSelected?.comments?.add(newComment)
                            val parentComment = TextView(this)
                            parentComment.text =
                                newComment.userId + " says: " +
                                        newComment.message
                            myLayout1.addView(parentComment)
                            val replyBox = EditText(this)
                            val replySend = Button(this)
                            replySend.text = "Submit reply"
                            replySend.setOnClickListener {
                                if (replyBox.text.isEmpty()) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Type a reply first.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    val replyRequestBody = JSONObject()
                                    replyRequestBody.put("username", LoginRepository.user?.userId)
                                    replyRequestBody.put("originalComment", newComment.message)
                                    replyRequestBody.put(
                                        "eventId",
                                        EventRepository.eventSelected?.eventId
                                    )
                                    replyRequestBody.put("isReply", "True")
                                    replyRequestBody.put("poster", newComment.userId)
                                    replyRequestBody.put("comment", replyBox.text)

                                    val jsonObjectRequestReply = JsonObjectRequest(
                                        "http://10.0.2.2:9000/events/addComment",
                                        replyRequestBody,
                                        Response.Listener { response ->
                                            Log.v("PROCESS", "got a response")
                                            Log.v("RESPONSE", response.toString())
                                            if (response.getString("status") == "Success") {
                                                Log.v("Response Success", "Wrote reply")
                                                val newReply = Comment(
                                                    EventRepository.eventSelected?.eventId!!,
                                                    LoginRepository.user?.userId!!,
                                                    replyBox.text.toString(),
                                                    null
                                                )
                                                if (newComment.replies == null) {
                                                    newComment.replies = mutableSetOf(newReply)
                                                } else {
                                                    newComment.replies!!.add(newReply)
                                                }
                                                val replyCommentNew = TextView(this)
                                                replyCommentNew.text =
                                                    newReply.userId + "'s reply: " + newReply.message
                                                myLayout1.addView(replyCommentNew)
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Reply posted!",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            } else {
                                                Log.v("Response Success", "User or Event not found")
                                                Log.v("ERROR", response.getString("errors"))
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Unable to add reply: ${response.getString("errors")}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }

                                        },
                                        Response.ErrorListener { error ->
                                            Log.e("Response Error", "Error occurred", error)
                                            Toast.makeText(
                                                applicationContext,
                                                "Unable to add reply: ${error.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    )
                                    RequestQueueSingleton.getInstance(LoginActivity.context)
                                        .addToRequestQueue(jsonObjectRequestReply)
                                }
                            }
                            myLayout1.addView(replyBox)
                            myLayout1.addView(replySend)
                            comment_layout.addView(myLayout1)

                            Toast.makeText(
                                applicationContext,
                                "Comment posted!",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Log.v("Response Success", "User or Event not found")
                            Log.v("ERROR", response.getString("errors"))
                            Toast.makeText(
                                applicationContext,
                                "Unable to add comment: ${response.getString("errors")}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    Response.ErrorListener { error ->
                        Log.e("Response Error", "Error occurred", error)
                        Toast.makeText(
                            applicationContext,
                            "Unable to add comment: ${error.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )

                RequestQueueSingleton.getInstance(LoginActivity.context)
                    .addToRequestQueue(jsonObjectRequest)
            }
        }


    }

    //toolbar stuff
    fun goToProfile(v: View) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    fun goToFeed(v: View) {
        val intent = Intent(this, FeedActivity::class.java)
        startActivity(intent)
    }

    fun goToSearch(v: View) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}
