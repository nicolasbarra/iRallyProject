package edu.upenn.cis350.irally.ui.profile


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
import com.squareup.picasso.Picasso
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.data.loadEventInfo
import edu.upenn.cis350.irally.data.model.LoggedInUser
import edu.upenn.cis350.irally.data.repository.EventRepository
import edu.upenn.cis350.irally.data.repository.LoginRepository
import edu.upenn.cis350.irally.ui.feed.FeedActivity
import edu.upenn.cis350.irally.ui.login.LoginActivity
import edu.upenn.cis350.irally.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.activity_user.profile_picture
import org.json.JSONArray
import org.json.JSONObject

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val userInfo = LoginRepository.userSelected!!
        user_profile_name.text = userInfo.displayName
        user_profile_pronouns.text = userInfo.genderPronouns
        user_profile_interests.text =
            userInfo.interests.toString().filter { e -> e != '[' && e != ']' }
        if (!userInfo.profilePictureLink.isNullOrEmpty()) {
            Picasso.with(this).load(userInfo.profilePictureLink)
                .into(profile_picture)
        }

        for (i in LoginRepository.user?.friends!!) {
            if (i == userInfo.userId) {
                friend.text = getString(R.string.already_friends)
                friend.isEnabled = false;
            }
        }

        if (!userInfo.eventsCreated.isNullOrEmpty()) {
            for (i in 0 until userInfo.eventsCreated.size) {
                val eventText = userInfo.eventsCreated.elementAt(i)
                if (i == 0) {
                    user_event_created1.text = eventText
                    user_event_created1.setOnClickListener {
                        loadEventInfo(it, eventText, this, applicationContext)
                    }
                } else {
                    val myButton = Button(this)
                    myButton.text = eventText
                    myButton.setOnClickListener {
                        loadEventInfo(it, eventText, this, applicationContext)
                    }
                    user_events_created_layout.addView(myButton)
                }
            }
        }

        if (!userInfo.eventsToAttend.isNullOrEmpty()) {
            for (i in 0 until userInfo.eventsToAttend.size) {
                val eventText = userInfo.eventsToAttend.elementAt(i)
                if (i == 0) {
                    user_events_attending1.text = eventText
                    user_events_attending1.setOnClickListener {
                        loadEventInfo(it, eventText, this, applicationContext)
                    }
                } else {
                    val button = Button(this)
                    button.text = eventText
                    button.setOnClickListener {
                        loadEventInfo(it, eventText, this, applicationContext)
                    }
                    user_events_attending_layout.addView(button)
                }
            }
        }

        friend.setOnClickListener {
            val requestBody = JSONObject()
            requestBody.put("currUsername", LoginRepository.user?.userId)
            requestBody.put("friendUsername", userInfo.userId)

            val jsonObjectRequest = JsonObjectRequest(
                "http://10.0.2.2:9000/users/addFriend",
                requestBody,
                Response.Listener { response ->
                    Log.v("PROCESS", "got a response")
                    Log.v("RESPONSE", response.toString())
                    if (response.getString("status") == "Success") {
                        LoginRepository.user?.friends?.add(userInfo.userId)
                        friend.text = getString(R.string.already_friends)
                        friend.isEnabled = false;
                        Log.v("Response Success", "Added Friend")
                        Toast.makeText(
                            applicationContext,
                            "Added ${userInfo.displayName} as a friend!",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Log.v("Response Success", "User not found")
                        Log.v("ERROR", response.getString("errors"))
                        Toast.makeText(
                            applicationContext,
                            "Unable to friend user: ${response.getString("errors")}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Response Error", "Error occurred", error)
                    Toast.makeText(
                        applicationContext,
                        "Unable to friend user: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )

            RequestQueueSingleton.getInstance(LoginActivity.context)
                .addToRequestQueue(jsonObjectRequest)

        }

        //toolbar
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
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}
