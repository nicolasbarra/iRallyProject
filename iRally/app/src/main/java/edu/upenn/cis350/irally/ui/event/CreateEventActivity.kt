package edu.upenn.cis350.irally.ui.event

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.data.repository.EventRepository
import edu.upenn.cis350.irally.data.repository.LoginRepository
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.data.model.Comment
import edu.upenn.cis350.irally.data.model.Event
import edu.upenn.cis350.irally.ui.login.LoginActivity
import edu.upenn.cis350.irally.ui.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_create_event.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class CreateEventActivity : AppCompatActivity() {

    companion object {
        var hourOfDay: Int? = null
        var minute: Int? = null
        var year: Int? = null
        var day: Int? = null
        var month: Int? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_create_event)
        val eventNameTxt = event_name
        //make this a real address
        val eventAddressTxt = event_address


        fun isDataValid(): String {
            //read in the inputs
            val eventName = eventNameTxt.text.toString().toLowerCase(Locale.ROOT)
            val eventDescription = event_description.text.toString()
            //make this a real address
            val eventAddress = eventAddressTxt.text.toString()

            return if (eventName.isEmpty() || eventDescription.isEmpty() ||
                eventAddress.isEmpty()
            ) {
                "One or more fields left blank"
            } else if (eventName.length < 4 || eventDescription.length < 4) {
                "Please make sure event name and description are at least 3 characters long"
            } else if (hourOfDay == null || minute == null || year == null || day == null || month == null) {
                "Please make sure to select a date and time."
            } else {
                "okgo"
            }
        }

        submit_event.setOnClickListener {
            val validityString = isDataValid()
            if (validityString == "okgo") {
                val eventRequestBody = JSONObject()
                val eventTitle: String = event_name.text.toString()
                val eventDescr: String = event_description.text.toString()
                val eventAddr: String = event_address.text.toString()
                val eventDateTime = "$month-$day-$year at $hourOfDay:$minute"
                eventRequestBody.put("username", LoginRepository.user?.userId)
                    .put("eventName", eventTitle).put("description", eventDescr)
                    .put("address", eventAddr).put("dateTime", eventDateTime)

                val eventJsonObjectRequest = JsonObjectRequest(
                    "http://10.0.2.2:9000/events/create",
                    eventRequestBody,
                    Response.Listener { response ->
                        Log.v("PROCESS", "got a response (register")
                        Log.v("RESPONSE", response.toString())
                        if (response.getString("status") == "Failure") {
                            val errors = response.getString("errors")
                            Log.v("Response Success", "Event not created due to error")
                            Log.v("ERROR", errors)
                            Toast.makeText(
                                applicationContext,
                                "Unable to create event: $errors",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            val newEventJSON = response.getJSONObject("event")
                            Log.v("Response Success", "Event create: $newEventJSON")
                            Toast.makeText(
                                applicationContext,
                                "New event, ${newEventJSON.get("eventId")}, successfully create.",
                                Toast.LENGTH_LONG
                            ).show()
                            val interestsOfAttendeesJSONArray: JSONArray =
                                newEventJSON.getJSONArray("interestsOfAttendees")
                            Log.v("interestscreate", interestsOfAttendeesJSONArray.toString())
                            val interestsOfAttendees = mutableSetOf<String>()
                            for (i in 0 until interestsOfAttendeesJSONArray.length()) {
                                interestsOfAttendees.add(
                                    interestsOfAttendeesJSONArray.get(i).toString()
                                )
                            }
                            val comments = mutableSetOf<Comment>()
                            val newEvent = Event(
                                newEventJSON.getString("eventId"),
                                LoginRepository.user!!.userId,
                                newEventJSON.getString("description"),
                                newEventJSON.getString("address"),
                                newEventJSON.getString("dateTime"),
                                mutableSetOf(),
                                0,
                                interestsOfAttendees,
                                comments
                            )
                            EventRepository.events.add(newEvent)
                            EventRepository.eventsCreatedByUser.add(newEvent.eventId + " on " + newEvent.dateTime)
                            if (LoginRepository.user != null) {
                                val prevVal = LoginRepository.user!!.numEventsCreated
                                Log.v(
                                    "code has erached here2",
                                    "or not, prevVal is ${LoginRepository.user!!.numEventsCreated}"
                                )
                                LoginRepository.user!!.numEventsCreated = prevVal + 1
                                Log.v(
                                    "code has erached here3",
                                    "or not, newVal is ${LoginRepository.user!!.numEventsCreated}"
                                )
                            }
                            Log.v("about to launch activity", "now")
                            val intent = Intent(this, ProfileActivity::class.java);
                            startActivity(intent);
                        }
                    },
                    Response.ErrorListener { error ->
                        Log.e("Response Error", "Error occurred", error)
                        Toast.makeText(
                            applicationContext,
                            "Unable to create event: ${error.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )

                RequestQueueSingleton.getInstance(LoginActivity.context)
                    .addToRequestQueue(eventJsonObjectRequest)


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

            } else {
                Toast.makeText(
                    applicationContext,
                    validityString,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    fun showTimePickerDialog(v: View) {
        TimePickerFragment().show(supportFragmentManager, "timePicker")
    }

    fun showDatePickerDialog(v: View) {
        DatePickerFragment().show(supportFragmentManager, "datePicker")
    }
}
