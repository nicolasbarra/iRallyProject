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

            if (eventName.isNullOrEmpty() || eventDescription.isNullOrEmpty() ||
                    eventAddress.isNullOrEmpty()) {
                return "One or more fields left blank"
            } else if (eventName.length < 4 || eventDescription.length < 4) {
                return "Please make sure event name and description are at least 3 characters long"
            }
            else return "okgo"
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
