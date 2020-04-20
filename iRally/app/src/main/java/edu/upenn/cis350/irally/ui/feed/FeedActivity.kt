package edu.upenn.cis350.irally.ui.feed

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
import edu.upenn.cis350.irally.data.repository.LoginRepository
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.data.loadEventInfo
import edu.upenn.cis350.irally.ui.login.LoginActivity
import edu.upenn.cis350.irally.ui.profile.ProfileActivity
import edu.upenn.cis350.irally.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_feed.*
import org.json.JSONObject

class FeedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        val eventRequestBody = JSONObject()
        eventRequestBody.put("username", LoginRepository.user?.userId)

        val eventJsonObjectRequest = JsonObjectRequest(
            "http://10.0.2.2:9000/users/eventFeed/",
            eventRequestBody,
            Response.Listener { response ->
                Log.v("PROCESS", "got a response (register")
                Log.v("RESPONSE", response.toString())
                if (response.getString("status") == "Success") {
                    Log.v("Response Success", "Event received")
                    val eventJSONList = response.getJSONArray("eventsList")
                    if (eventJSONList.length() == 0) {
                        feed_event1.text = getString(R.string.no_events_yet_add_friends)
                    } else {
                        for (i in 0 until eventJSONList.length()) {
                            val eventText = eventJSONList.get(i).toString()

                            if (i == 0) {
                                feed_event1.text = eventText
                                feed_event1.setOnClickListener {
                                    loadEventInfo(it, eventText, this, applicationContext)
                                }
                            } else {
                                val myButton = Button(this)
                                myButton.text = eventText
                                myButton.setOnClickListener {
                                    loadEventInfo(it, eventText, this, applicationContext)
                                }
                                event_in_feed_layout.addView(myButton)
                            }
                        }
                    }
                } else {
                    Log.v("Response Success", "Events not found")
                    Log.v("ERROR", response.getString("errors"))
                    Toast.makeText(
                        applicationContext,
                        "Unable to load event feed: ${response.getString("errors")}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("Response Error", "Error occurred", error)
                Toast.makeText(
                    applicationContext,
                    "Unable to load event feed: ${error.message}",
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

        //TOOLBAR
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.toolbar)
    }

    fun goToProfile(v: View) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    fun goToFeed(v: View) {
    }

    fun goToSearch(v: View) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}

