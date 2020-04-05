package edu.upenn.cis350.irally.ui.event

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import edu.upenn.cis350.irally.R
import kotlinx.android.synthetic.main.activity_event_page.*
import org.json.JSONObject

class EventPageActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_event_page)

        val eventName = event_page_name

        val markAsGoing = event_page_button

        markAsGoing.setOnClickListener {
            val url = "http://10.0.2.2:9000/event/addAttendee"
            //TODO: uncomment this
            //                    val jsonObjectRequest = JsonObjectRequest(url, newUserJSON,
            //                        Response.Listener { response ->
            //                            if (response.status === 'Success') {
            //                                Toast.makeText(
            //                                    applicationContext,
            //                                    "Successfully marked as going.",
            //                                    Toast.LENGTH_LONG
            //                                ).show()
            //                            }
            //                            else {
            //                                Toast.makeText(
            //                                    applicationContext,
            //                                    "Something went wrong.",
            //                                    Toast.LENGTH_LONG
            //                                ).show()
            //                            }
            //                        },
            //                        Response.ErrorListener { error ->
            //                            Toast.makeText(
            //                                applicationContext,
            //                                ("Network connection error. Please try again." +
            //                                        "Error: %s").format(error.toString()),
            //                                Toast.LENGTH_LONG
            //                            ).show()
            //
            //                        }
            //                    )

            // Access the RequestQueue through your singleton class.
            //TODO: uncomment, change login??
            //     MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        }
    }
}