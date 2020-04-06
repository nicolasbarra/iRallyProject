package edu.upenn.cis350.irally.ui.event

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.data.LoginRepository
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.activity_profile.*
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
            if (isDataValid() == "okgo") {
                val eventRequestBody = JSONObject()
                val interestToAdd = add_interest_type.text
                eventRequestBody.put("username", LoginRepository.user?.userId)
                eventRequestBody.put("interest", interestToAdd)

                val insertJsonObjectRequest = JsonObjectRequest(
                    "http://10.0.2.2:9000/users/addInterest",
                    eventRequestBody,
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
