package edu.upenn.cis350.irally.ui.event

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.data.EventRepository
import edu.upenn.cis350.irally.data.LoginRepository
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.ui.feed.FeedActivity
import edu.upenn.cis350.irally.ui.login.LoginActivity
import edu.upenn.cis350.irally.ui.profile.ProfileActivity
import edu.upenn.cis350.irally.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_event_page.*
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

        if (!EventRepository.eventSelected?.attendees.isNullOrEmpty()) {
            for (i in 0 until EventRepository.eventSelected!!.attendees.size) {
                if (i == 0) {
                    val attendeeText = EventRepository.eventSelected!!.attendees.elementAt(0)
                    attendee1.text = attendeeText
                    attendee1.setOnClickListener {
//                    loadEventInfo(it, eventText)
                        // TODO: load attendee, check if the person is the current users
                    }
                } else {
                    val myButton = Button(this)
                    val attendeeText = EventRepository.eventSelected!!.attendees.elementAt(i)
                    myButton.text = attendeeText
                    myButton.setOnClickListener {
//                    loadEventInfo(it, eventText)
                        // TODO: load attendee, check if the person is the current user
                    }
                    attendees_layout.addView(myButton)
                }
            }
        }

        val markAsGoing = event_page_button
        var attendingEventBool = false
        for (i in LoginRepository.user?.eventsToAttend!!) {
            if (i == EventRepository.eventSelected?.eventId) {
                attendingEventBool = true
            }
        }
        if (attendingEventBool) {
            markAsGoing.text = getString(R.string.going);
            markAsGoing.isEnabled = false;
        }
        markAsGoing.setOnClickListener {
            val eventRequestBody = JSONObject()
            val eventName = EventRepository.eventSelected?.eventId
            val username = LoginRepository.user?.userId
            eventRequestBody.put("username", LoginRepository.user?.userId)
            eventRequestBody.put("eventId", eventName);
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
                        markAsGoing.text = getString(R.string.going);
                        markAsGoing.isEnabled = false;
                        if (eventName != null) {
                            LoginRepository.user?.eventsToAttend?.add(eventName)
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
//                    loadEventInfo(it, eventText)
                                    // TODO: load attendee, check if the person is the current users
                                }
                            } else {
                                val myButton = Button(this)
                                val attendeeText =
                                    EventRepository.eventSelected!!.attendees.elementAt(i)
                                myButton.text = attendeeText
                                myButton.setOnClickListener {
//                    loadEventInfo(it, eventText)
                                    // TODO: load attendee, check if the person is the current user
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
