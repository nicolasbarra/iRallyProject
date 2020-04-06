package edu.upenn.cis350.irally.ui.event

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import java.util.*


class CreateEventActivity : AppCompatActivity() {

    companion object {
        var hourOfDay: Int? = null
        var minute: Int? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_create_event)
        val eventNameTxt = event_name
        val eventDescriptionTxt = event_description
        //make this a real address
        val eventAddressTxt = event_address


        fun isDataValid(): String {

            //read in the inputs
            val eventName = eventNameTxt.text.toString().toLowerCase(Locale.ROOT)
            val eventDescription = eventDescriptionTxt.text.toString()
            //make this a real address
            val eventAddress = eventAddressTxt.text.toString()

            return if (eventName.isEmpty() || eventDescription.isEmpty() ||
                eventAddress.isEmpty()
            ) {
                "One or more fields left blank"
            } else if (eventName.length < 4 || eventDescription.length < 4) {
                "Please make sure event name and description are at least 3 characters long"
            } else "okgo"
        }

        submit_event.setOnClickListener {
            Log.v("inCREATeT TIME hour", hourOfDay.toString())
            Log.v("increateTIME minute", minute.toString())
            if (isDataValid() == "okgo") {
                Log.v("inCREATeT TIME hour", hourOfDay.toString())
                Log.v("increateTIME minute", minute.toString())


                val eventName = eventNameTxt.text.toString().toLowerCase(Locale.ROOT)
                val eventDescription = eventDescriptionTxt.text.toString()
                //make this a real address
                val eventAddress = eventAddressTxt.text.toString()

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
        DatePickerFragment().show(supportFragmentManager, "datePicker")
    }
}
