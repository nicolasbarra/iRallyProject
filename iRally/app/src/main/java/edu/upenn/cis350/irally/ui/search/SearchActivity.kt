package edu.upenn.cis350.irally.ui.search

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
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.data.loadEventInfo
import edu.upenn.cis350.irally.data.loadProfileInfo
import edu.upenn.cis350.irally.ui.feed.FeedActivity
import edu.upenn.cis350.irally.ui.login.LoginActivity
import edu.upenn.cis350.irally.ui.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_search.*
import org.json.JSONObject

class SearchActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        search_button.setOnClickListener {
            val searchRequestBody = JSONObject()
            searchRequestBody.put("query", search_text.text);

            val eventJsonObjectRequest = JsonObjectRequest(
                "http://10.0.2.2:9000/search",
                searchRequestBody,
                Response.Listener { response ->
                    Log.v("PROCESS", "got a response (register")
                    Log.v("RESPONSE", response.toString())
                    if (response.getString("status") == "Success") {
                        Log.v("Response Success", "Event received")
                        val usersList = response.getJSONArray("usersList")
                        val eventsList = response.getJSONArray("eventsList")
                        val adminsList = response.getJSONArray("adminsList")
                        if (usersList.length() != 0) {
                            for (i in 0 until usersList.length()) {
                                val userText = usersList.get(i).toString()
                                if (i == 0) {
                                    user_search_result1.text = userText
                                    user_search_result1.setOnClickListener {
                                        loadProfileInfo(it, userText, this, applicationContext)
                                    }
                                } else {
                                    val myButton = Button(this)
                                    myButton.text = userText
                                    myButton.setOnClickListener {
                                        loadProfileInfo(it, userText, this, applicationContext)
                                    }
                                    user_search_result_layout.addView(myButton)
                                }
                            }
                        }

                        if (eventsList.length() != 0) {
                            for (i in 0 until eventsList.length()) {
                                val eventText = eventsList.get(i).toString()
                                if (i == 0) {
                                    event_search_result1.text = eventText
                                    event_search_result1.setOnClickListener {
                                        loadEventInfo(it, eventText, this, applicationContext)
                                    }
                                } else {
                                    val myButton = Button(this)
                                    myButton.text = eventText
                                    myButton.setOnClickListener {
                                        loadEventInfo(it, eventText, this, applicationContext)
                                    }
                                    event_search_result_layout.addView(myButton)
                                }
                            }
                        }

//                        if (adminsList.length() != 0) {
//                            for (i in 0 until adminsList.length()) {
//                                val adminText = adminsList.get(i).toString()
//                                if (i == 0) {
//                                    admin_search_result1.text = adminText
//                                    admin_search_result1.setOnClickListener {
//                                        loadEventInfo(it, adminText, this, applicationContext)
//                                    }
//                                } else {
//                                    val myButton = Button(this)
//                                    myButton.text = adminText
//                                    myButton.setOnClickListener {
//                                        loadEventInfo(it, adminText, this, applicationContext)
//                                    }
//                                    admin_search_result_layout.addView(myButton)
//                                }
//                            }
//                        }

                    } else {
                        Log.v("Response Success", "queries not found")
                        Log.v("ERROR", response.getString("errors"))
                        Toast.makeText(
                            applicationContext,
                            "Unable to search the database: ${response.getString("errors")}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Response Error", "Error occurred", error)
                    Toast.makeText(
                        applicationContext,
                        "Unable to load search results: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )

            RequestQueueSingleton.getInstance(LoginActivity.context)
                .addToRequestQueue(eventJsonObjectRequest)
        }

        map_button.setOnClickListener {
            //todo: set to new activity?? with 5 closest events
        }


        //TOOLBAR
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.toolbar)

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
    }
}
