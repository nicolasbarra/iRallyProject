package edu.upenn.cis350.irally.ui.location

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.data.load.loadEventInfo
import edu.upenn.cis350.irally.ui.feed.FeedActivity
import edu.upenn.cis350.irally.ui.login.LoginActivity
import edu.upenn.cis350.irally.ui.profile.ProfileActivity
import edu.upenn.cis350.irally.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_location.*
import org.json.JSONObject

class LocationActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            val positionObject = JSONObject()
            if (location != null) {
                positionObject.put("lat", location.latitude)
                positionObject.put("lng", location.longitude)

                val eventRequestBody = JSONObject()
                eventRequestBody.put("userPosition", positionObject)

                val eventJsonObjectRequest = JsonObjectRequest(
                    "http://10.0.2.2:9000/events/getClosestEvents/",
                    eventRequestBody,
                    Response.Listener { response ->
                        Log.v("PROCESS", "got a response (register")
                        Log.v("RESPONSE", response.toString())
                        if (response.getString("status") == "Success") {
                            Log.v("Response Success", "Event received")
                            val eventJSONList = response.getJSONArray("closestEvents")
                            if (eventJSONList.length() == 0) {
                                near_event1.text = getString(R.string.no_events_found_nearby)
                            } else {
                                for (i in 0 until eventJSONList.length()) {
                                    val eventText = eventJSONList.get(i).toString()
                                    if (i == 0) {
                                        near_event1.text = eventText
                                        near_event1.setOnClickListener {
                                            loadEventInfo(
                                                it,
                                                eventText,
                                                this,
                                                applicationContext
                                            )
                                        }
                                    } else {
                                        val myButton = Button(this)
                                        myButton.text = eventText
                                        myButton.setOnClickListener {
                                            loadEventInfo(
                                                it,
                                                eventText,
                                                this,
                                                applicationContext
                                            )
                                        }
                                        event_near_layout.addView(myButton)
                                    }
                                }
                            }
                        } else {
                            Log.v("Response Success", "Events not found")
                            Log.v("ERROR", response.getString("errors"))
                            Toast.makeText(
                                applicationContext,
                                "Unable to load nearby events: ${response.getString("errors")}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    Response.ErrorListener { error ->
                        Log.e("Response Error", "Error occurred", error)
                        Toast.makeText(
                            applicationContext,
                            "Unable to load nearby events: ${error.message}",
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
            } else {
                Log.e("Location Error", "Location is null")
                Toast.makeText(
                    applicationContext,
                    "Unable to find your current location",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.toolbar)
    }

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
