package edu.upenn.cis350.irally.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.NetworkImageView
import com.squareup.picasso.Picasso
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.data.LoginRepository
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.ui.event.CreateEventActivity
import edu.upenn.cis350.irally.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile_picture.view.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONArray
import org.json.JSONObject

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        logout.setOnClickListener {
            LoginRepository.logout()
            finish()
            val intent = Intent(this, LoginActivity::class.java);
            startActivity(intent);
        }

        add_interest_send.setOnClickListener {
            val interestRequestBody = JSONObject()
            val interestToAdd = add_interest_type.text
            interestRequestBody.put("username", LoginRepository.user?.userId)
            interestRequestBody.put("interest", interestToAdd)

            val insertJsonObjectRequest = JsonObjectRequest(
                "http://10.0.2.2:9000/users/addInterest",
                interestRequestBody,
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

            //                        Response.ErrorListener { error ->
            //                            Toast.makeText(
            //                                applicationContext,
            //                                ("Network connection error. Please try again." +
            //                                        "Error: %s").format(error.toString()),
            //                                Toast.LENGTH_LONG
            //                            ).show()
        }

        remove_interest.setOnClickListener {
            val interestRequestBody = JSONObject()
            val interestToRemove = add_interest_type.text
            interestRequestBody.put("username", LoginRepository.user?.userId)
            interestRequestBody.put("interest", interestToRemove)

            val removeJsonObjectRequest = JsonObjectRequest(
                "http://10.0.2.2:9000/users/removeInterest",
                interestRequestBody,
                Response.Listener { response ->
                    Log.v("PROCESS", "got a response (register")
                    Log.v("RESPONSE", response.toString())
                    if (response.getString("status") == "Failure") {
                        val errors = response.getString("errors")
                        Log.v("Response Success", "Interest not removed due to error")
                        Log.v("ERROR", errors)
                        Toast.makeText(
                            applicationContext,
                            "Unable to remove interest: $errors",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        val newInterestsJSONArray = response.getJSONArray("newInterests")
                        Log.v("Response Success", "Interest removed: $newInterestsJSONArray")
                        Toast.makeText(
                            applicationContext,
                            "New interest, $interestToRemove, successfully removed.",
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
                        "Unable to remove interest: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )

            RequestQueueSingleton.getInstance(LoginActivity.context)
                .addToRequestQueue(removeJsonObjectRequest)

            //                        Response.ErrorListener { error ->
            //                            Toast.makeText(
            //                                applicationContext,
            //                                ("Network connection error. Please try again." +
            //                                        "Error: %s").format(error.toString()),
            //                                Toast.LENGTH_LONG
            //                            ).show()
        }


        val profilePicture = profile_picture
        val editProfilePicture = edit
        profile_pronouns.text = LoginRepository.user?.genderPronouns
        val profileName = profile_name
        profileName.text = LoginRepository.user?.displayName
        profile_description.text =
            LoginRepository.user?.interests.toString().filter { e -> e != '[' && e != ']' }
        val editPicture = edit


        delete_profile.setOnClickListener {
            val deleteRequestBody = JSONObject()
            deleteRequestBody.put("username", LoginRepository.user?.userId)

            val deleteJsonObjectRequest = JsonObjectRequest(
                "http://10.0.2.2:9000/users/delete",
                deleteRequestBody,
                Response.Listener { response ->
                    Log.v("PROCESS", "got a response (register")
                    Log.v("RESPONSE", response.toString())
                    if (response.getString("status") == "Success") {
                        val deletedUser = response.get("username")
                        Log.v("Response Success", "User deleted: $deletedUser")
                        Toast.makeText(
                            applicationContext,
                            "$deletedUser successfully deleted.",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                        val intent = Intent(this, LoginActivity::class.java);
                        startActivity(intent);
                    } else {
                        val errors = response.getString("errors")
                        Log.v("Response Success", "User not deleted due to error")
                        Log.v("ERROR", errors)
                        Toast.makeText(
                            applicationContext,
                            "Unable to delete user: $errors",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Response Error", "Error occurred", error)
                    Toast.makeText(
                        applicationContext,
                        "Unable to delete user: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )

            RequestQueueSingleton.getInstance(LoginActivity.context)
                .addToRequestQueue(deleteJsonObjectRequest)

            //                        Response.ErrorListener { error ->
            //                            Toast.makeText(
            //                                applicationContext,
            //                                ("Network connection error. Please try again." +
            //                                        "Error: %s").format(error.toString()),
            //                                Toast.LENGTH_LONG
            //                            ).show()
        }


        val createEvent = create_event
        create_event.setOnClickListener {
            // Handler code here.
            //CHANGE BACK TO LOGIN
            val intent = Intent(this, CreateEventActivity::class.java);
            startActivity(intent);
        }

        edit.setOnClickListener {
            val intent = Intent(this, ProfilePicture::class.java)
            startActivity(intent);

        }

        fun createEventButtons(events: JSONArray) {
            for (i in 0 until events.length()) {
                val button = Button(this)
                button.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                //val name = events.getJSONObject(i).eventName


            }
        }
    }
}
