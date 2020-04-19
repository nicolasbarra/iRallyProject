package edu.upenn.cis350.irally.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.squareup.picasso.Picasso
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.data.EventRepository
import edu.upenn.cis350.irally.data.LocationManagerActivity
import edu.upenn.cis350.irally.data.LoginRepository
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.data.model.Event
import edu.upenn.cis350.irally.ui.event.CreateEventActivity
import edu.upenn.cis350.irally.ui.event.EventPageActivity
import edu.upenn.cis350.irally.ui.feed.FeedActivity
import edu.upenn.cis350.irally.ui.login.LoginActivity
import edu.upenn.cis350.irally.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_profile.*
import org.json.JSONArray
import org.json.JSONObject

class ProfileActivity : AppCompatActivity() {

    companion object {
        var hasUploaded = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //TOOLBAR
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.toolbar)

        if (hasUploaded || !LoginRepository.user?.profilePictureLink.isNullOrEmpty()) {
            Picasso.with(this).load(LoginRepository.user?.profilePictureLink)
                .into(profile_picture)
        }

        fun loadEventInfo(view: View, eventText: String) {
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
                            if (eventJSON.has("eventsToAttendStrings")) {
                                eventJSON.getJSONArray("eventsToAttendStrings")
                            } else {
                                JSONArray()
                            }
                        val attendeesStrings = mutableSetOf<String>()
                        for (k in 0 until attendeesStringsJSONArray.length()) {
                            attendeesStrings.add(
                                attendeesStringsJSONArray.get(k).toString()
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
                            interestsOfAttendees
                        )
                        EventRepository.eventSelected = newEvent
                        val intent = Intent(this, EventPageActivity::class.java)
                        startActivity(intent)
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

            RequestQueueSingleton.getInstance(LoginActivity.context)
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

        if (EventRepository.eventsCreatedByUser.isNotEmpty()) {
            if (EventRepository.eventsCreatedByUser.size == 1) {
                val eventText = EventRepository.eventsCreatedByUser.elementAt(0)
                event_created1.text = eventText
                event_created1.setOnClickListener {
                    loadEventInfo(it, eventText)
                }
            } else {
                Log.v("it is not null", "not null")
                for (i in 0 until EventRepository.eventsCreatedByUser.size) {
                    if (i == 0) {
                        val eventText = EventRepository.eventsCreatedByUser.elementAt(i)
                        event_created1.text = eventText
                        event_created1.setOnClickListener {
                            loadEventInfo(it, eventText)
                        }
                    } else {
                        val myButton = Button(this)
                        val eventText = EventRepository.eventsCreatedByUser.elementAt(i)
                        myButton.text = eventText
                        myButton.setOnClickListener {
                            loadEventInfo(it, eventText)
                        }
                        events_created_layout.addView(myButton)
                    }
                }
            }
        }

        // TODO: Figure out WHAT THE FUCK THIS SHIT BELOW IS
        //TODO: need to create a route to get event from event name!
        if (LoginRepository.user?.eventsToAttend!!.isNotEmpty()) {
            if (LoginRepository.user?.eventsToAttend!!.size == 1) {
                val eventText = LoginRepository.user?.eventsToAttend!!.elementAt(0)
                event_attending1.text = eventText
                event_attending1.setOnClickListener {
                    loadEventInfo(it, eventText)
                }
            } else {
                Log.v("it is not null", "not null")
                for (i in 0 until LoginRepository.user?.eventsToAttend!!.size) {
                    //todo: add the route call here
                    if (i == 0) {
                        val eventText = LoginRepository.user?.eventsToAttend!!.elementAt(i)
                        event_attending1.text = eventText
                        event_attending1.setOnClickListener {
                            loadEventInfo(it, eventText)
                        }
                    } else {
                        val button = Button(this)
                        val eventText = LoginRepository.user?.eventsToAttend!!.elementAt(i)
                        button.text = eventText
                        button.setOnClickListener {
                            loadEventInfo(it, eventText)
                        }
                        events_attending_layout.addView(button)
                    }
                }
            }
        }
        // TODO: figure out what the fuck the shit above is

        logout.setOnClickListener {
            LoginRepository.logout()
            finish()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        add_interest_send.setOnClickListener {
            val interestRequestBody = JSONObject()
            val interestToAdd = add_interest_type.text
            interestRequestBody.put("username", LoginRepository.user?.userId)
            interestRequestBody.put("interest", interestToAdd)

            val insertJsonObjectRequest = JsonObjectRequest(
                "http://10.0.2.2:9000/users/addInterest",
                interestRequestBody,
                Response.Listener { response ->
                    Log.v("PROCESS", "got a response (register")
                    Log.v("RESPONSE", response.toString())
                    if (response.getString("status") == "Failure") {
                        val errors = response.getString("errors")
                        Log.v("Response Success", "Interest not added due to error")
                        Log.v("ERROR", errors)
                        Toast.makeText(
                            applicationContext,
                            "Unable to add interest: $errors",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        val newInterestsJSONArray = response.getJSONArray("newInterests")
                        Log.v("Response Success", "Interest added: $newInterestsJSONArray")
                        Toast.makeText(
                            applicationContext,
                            "New interest, $interestToAdd, successfully added.",
                            Toast.LENGTH_LONG
                        ).show()
                        val newInterests = mutableSetOf<String>()
                        for (i in 0 until newInterestsJSONArray.length()) {
                            newInterests.add(newInterestsJSONArray.get(i).toString())
                        }
                        profile_description.text =
                            newInterests.toString().filter { e -> e != '[' && e != ']' }
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Response Error", "Error occurred", error)
                    Toast.makeText(
                        applicationContext,
                        "Unable to add interest: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )

            RequestQueueSingleton.getInstance(LoginActivity.context)
                .addToRequestQueue(insertJsonObjectRequest)

            //                        Response.ErrorListener { error ->
            //                            Toast.makeText(
            //                                applicationContext,
            //                                ("Network connection error. Please try again." +
            //                                        "Error: %s").format(error.toString()),
            //                                Toast.LENGTH_LONG
            //                            ).show()
        }

        remove_interest.setOnClickListener {
            val interestRequestBody = JSONObject()
            val interestToRemove = add_interest_type.text
            interestRequestBody.put("username", LoginRepository.user?.userId)
            interestRequestBody.put("interest", interestToRemove)

            val removeJsonObjectRequest = JsonObjectRequest(
                "http://10.0.2.2:9000/users/removeInterest",
                interestRequestBody,
                Response.Listener { response ->
                    Log.v("PROCESS", "got a response (register")
                    Log.v("RESPONSE", response.toString())
                    if (response.getString("status") == "Failure") {
                        val errors = response.getString("errors")
                        Log.v("Response Success", "Interest not removed due to error")
                        Log.v("ERROR", errors)
                        Toast.makeText(
                            applicationContext,
                            "Unable to remove interest: $errors",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        val newInterestsJSONArray = response.getJSONArray("newInterests")
                        Log.v("Response Success", "Interest removed: $newInterestsJSONArray")
                        Toast.makeText(
                            applicationContext,
                            "New interest, $interestToRemove, successfully removed.",
                            Toast.LENGTH_LONG
                        ).show()
                        val newInterests = mutableSetOf<String>()
                        for (i in 0 until newInterestsJSONArray.length()) {
                            newInterests.add(newInterestsJSONArray.get(i).toString())
                        }
                        profile_description.text =
                            newInterests.toString().filter { e -> e != '[' && e != ']' }
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Response Error", "Error occurred", error)
                    Toast.makeText(
                        applicationContext,
                        "Unable to remove interest: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )

            RequestQueueSingleton.getInstance(LoginActivity.context)
                .addToRequestQueue(removeJsonObjectRequest)

            //                        Response.ErrorListener { error ->
            //                            Toast.makeText(
            //                                applicationContext,
            //                                ("Network connection error. Please try again." +
            //                                        "Error: %s").format(error.toString()),
            //                                Toast.LENGTH_LONG
            //                            ).show()
        }

        profile_pronouns.text = LoginRepository.user?.genderPronouns
        val profileName = profile_name
        profileName.text = LoginRepository.user?.displayName
        profile_description.text =
            LoginRepository.user?.interests.toString().filter { e -> e != '[' && e != ']' }


        delete_profile.setOnClickListener {
            val deleteRequestBody = JSONObject()
            deleteRequestBody.put("username", LoginRepository.user?.userId)

            val deleteJsonObjectRequest = JsonObjectRequest(
                "http://10.0.2.2:9000/users/delete",
                deleteRequestBody,
                Response.Listener { response ->
                    Log.v("PROCESS", "got a response (register")
                    Log.v("RESPONSE", response.toString())
                    if (response.getString("status") == "Success") {
                        val deletedUser = response.get("username")
                        Log.v("Response Success", "User deleted: $deletedUser")
                        Toast.makeText(
                            applicationContext,
                            "$deletedUser successfully deleted.",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        val errors = response.getString("errors")
                        Log.v("Response Success", "User not deleted due to error")
                        Log.v("ERROR", errors)
                        Toast.makeText(
                            applicationContext,
                            "Unable to delete user: $errors",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Response Error", "Error occurred", error)
                    Toast.makeText(
                        applicationContext,
                        "Unable to delete user: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )

            RequestQueueSingleton.getInstance(LoginActivity.context)
                .addToRequestQueue(deleteJsonObjectRequest)

            //                        Response.ErrorListener { error ->
            //                            Toast.makeText(
            //                                applicationContext,
            //                                ("Network connection error. Please try again." +
            //                                        "Error: %s").format(error.toString()),
            //                                Toast.LENGTH_LONG
            //                            ).show()
        }


        create_event.setOnClickListener {
            // Handler code here.
            //CHANGE BACK TO LOGIN
            val intent = Intent(this, CreateEventActivity::class.java)
            startActivity(intent)
        }

        edit.setOnClickListener {
            val intent = Intent(this, ProfilePicture::class.java)
            startActivity(intent)

        }

        fun createEventButtons(events: JSONArray) {
            for (i in 0 until events.length()) {
                val button = Button(this)
                button.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                //val name = events.getJSONObject(i).eventName


            }
        }
    }

    //toolbar stuff
    fun goToProfile(v: View) {
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
