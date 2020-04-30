package edu.upenn.cis350.irally.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.squareup.picasso.Picasso
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.data.repository.AdminRepository
import edu.upenn.cis350.irally.data.repository.LoginRepository
import edu.upenn.cis350.irally.ui.feed.FeedActivity
import edu.upenn.cis350.irally.ui.login.LoginActivity
import edu.upenn.cis350.irally.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_user.*
import org.json.JSONObject

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val adminInfo = AdminRepository.adminSelected!!
        Log.v("AdminInfo ${adminInfo.username}", adminInfo.toString())
        admin_name.text = adminInfo.displayName
        admin_political_affiliation.text = adminInfo.politicalAffiliation
        admin_profile_description.text = adminInfo.description
        admin_goals.text =
            adminInfo.goals.toString().filter { e -> e != '[' && e != ']' }
        admin_interests.text =
            adminInfo.interests.toString().filter { e -> e != '[' && e != ']' }
        if (!adminInfo.profilePictureLink.isNullOrEmpty()) {
            Picasso.with(this).load(adminInfo.profilePictureLink)
                .into(profile_picture)
        }

        for (i in LoginRepository.user?.adminsFollowedNames!!) {
            if (i == adminInfo.username) {
                follow.text = getString(R.string.following)
                follow.isEnabled = false;
            }
        }

        follow.setOnClickListener {
            val requestBody = JSONObject()
            Log.v("HIT", "this is run!!!");
            requestBody.put("username", LoginRepository.user?.userId)
            requestBody.put("admin", adminInfo.username)

            val jsonObjectRequest = JsonObjectRequest(
                "http://10.0.2.2:9000/users/followAdmin",
                requestBody,
                Response.Listener { response ->
                    Log.v("PROCESS", "got a response")
                    Log.v("RESPONSE", response.toString())
                    if (response.getString("status") == "Success") {
                        LoginRepository.user?.adminsFollowedNames?.add(adminInfo.username)
                        follow.text = "Already following"
                        follow.isEnabled = false;
                        Log.v("Response Success", "Followed Admin")
                        Toast.makeText(
                            applicationContext,
                            "Followed ${adminInfo.displayName} !",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Log.v("Response Success", "Admin not found")
                        Log.v("ERROR", response.getString("errors"))
                        Toast.makeText(
                            applicationContext,
                            "Unable to follow admin: ${response.getString("errors")}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Response Error", "Error occurred", error)
                    Toast.makeText(
                        applicationContext,
                        "Unable to follow admin: ${error.message}",
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
