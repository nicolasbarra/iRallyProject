package edu.upenn.cis350.irally.ui.event

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject


class CreateEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_create_event)
        val eventNameTxt = event_name
        val eventDescriptionTxt = event_description
        //make this a real address
        val eventAddressTxt = event_address


        fun isDataValid(): String {

            //read in the inputs
            val eventName = eventNameTxt.text.toString().toLowerCase()
            val eventDescription = eventDescriptionTxt.text.toString()
            //make this a real address
            val eventAddress = eventAddressTxt.text.toString()

            return if (eventName.isNullOrEmpty() || eventDescription.isNullOrEmpty() ||
                eventAddress.isNullOrEmpty()
            ) {
                "One or more fields left blank"
            } else if (eventName.length < 4 || eventDescription.length < 4) {
                "Please make sure event name and description are at least 3 characters long"
            } else "okgo"
        }
        //Regis
        // ter back to login
        val submitEvent = submit_event
        submitEvent.setOnClickListener {
            if (isDataValid() == "okgo") {

                val eventName = eventNameTxt.text.toString().toLowerCase()
                val eventDescription = eventDescriptionTxt.text.toString()
                //make this a real address
                val eventAddress = eventAddressTxt.text.toString()

                //TODO: CREATE EVENT JSON
                val newEventJSONObject = JSONObject(
                    "{eventId:TODO,eventName:" + eventName +
                            "create:USER???,description:" + eventDescription + ",address:" + eventAddress
                            + "dateTime:DATETIME???,attendees????:???,numberOfAttendees:" + 0 + "interestsOfAttendees:" +
                            "interestsOfAttendees????}")

                val url = "http://10.0.2.2:9000/event/create"
//TODO: uncomment this
//                    val jsonObjectRequest = JsonObjectRequest(url, newEventJSON,
//                        Response.Listener { response ->
//                            if (response.status === 'Success') {
//                                Toast.makeText(
//                                    applicationContext,
//                                    "Event created. Return to profile.",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
//                            else {
//                                Toast.makeText(
//                                    applicationContext,
//                                    "Something went wrong. Please try again.",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
//                        },
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

// Access the RequestQueue through your singleton class.
                //TODO: uncomment, change login??
                //     MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)


//                val eventJSON = JSONObject()
//
//                {"eventId": idk,"eventName":eventName,"creator":USER??,"description":eventDescription,
//                    "address":eventAddress,"dateTime":??,"numberOfAttendees":0,"interestsOfAttendees":userinterests??}
            } else {
                Toast.makeText(
                    applicationContext,
                    isDataValid(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }


    fun showTimePickerDialog(v: View) {
        TimePickerFragment().show(supportFragmentManager, "timePicker")

    }

    fun showDatePickerDialog(v: View) {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")

    }

}
