package edu.upenn.cis350.irally.data.load

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.data.model.LoggedInUser
import edu.upenn.cis350.irally.data.repository.LoginRepository
import edu.upenn.cis350.irally.ui.login.LoginActivity
import edu.upenn.cis350.irally.ui.profile.ProfileActivity
import edu.upenn.cis350.irally.ui.profile.UserActivity
import org.json.JSONArray
import org.json.JSONObject

fun loadProfileInfo(
    view: View,
    attendeeText: String,
    packageContext: Context,
    applicationContext: Context
) {
    if (attendeeText == LoginRepository.user!!.userId) {
        val intent = Intent(packageContext, ProfileActivity::class.java)
        packageContext.startActivity(intent)
    } else {
        val requestBody = JSONObject()
        requestBody.put("username", attendeeText)

        val jsonObjectRequest = JsonObjectRequest(
            "http://10.0.2.2:9000/users/",
            requestBody,
            Response.Listener { response ->
                Log.v("PROCESS", "got a response")
                Log.v("RESPONSE", response.toString())
                if (response.getString("status") == "Success") {
                    val userJson = response.getJSONObject("user")
                    val personalInfo = userJson.getJSONObject("personalInfo")
                    val displayName = personalInfo.getString("name")
                    val email = personalInfo.getString("email")
                    val gender = personalInfo.getString("gender")
                    val genderPronouns = personalInfo.getString("genderPronouns")
                    val profilePictureLink = if (personalInfo.has("profilePictureLink")) {
                        personalInfo.getString("profilePictureLink")
                    } else {
                        ""
                    }
                    val interestsJSONArray: JSONArray =
                        personalInfo.getJSONArray("interests")
                    Log.v("interests", interestsJSONArray.toString())
                    val interests = mutableSetOf<String>()
                    for (i in 0 until interestsJSONArray.length()) {
                        interests.add(interestsJSONArray.get(i).toString())
                    }
                    Log.v("interests arry final", interests.toString())
                    val eventsToAttendStringsJson =
                        if (userJson.has("eventsToAttendStrings")) {
                            userJson.getJSONArray("eventsToAttendStrings")
                        } else {
                            JSONArray()
                        }
                    val eventsToAttendStrings = mutableSetOf<String>()
                    for (i in 0 until eventsToAttendStringsJson.length()) {
                        eventsToAttendStrings.add(
                            eventsToAttendStringsJson.get(i).toString()
                        )
                    }
                    val eventsJSONArray: JSONArray =
                        userJson.getJSONArray("eventsCreatedStrings")
                    val eventsString = mutableSetOf<String>()
                    for (i in 0 until eventsJSONArray.length()) {
                        eventsString.add(
                            eventsJSONArray.get(i).toString()
                        )
                    }
                    if (!displayName.isNullOrEmpty() && !email.isNullOrEmpty()
                        && !gender.isNullOrEmpty() && !genderPronouns.isNullOrEmpty()
                    ) {
                        val user = LoggedInUser(
                            attendeeText,
                            displayName,
                            email,
                            gender,
                            genderPronouns,
                            profilePictureLink,
                            interests,
                            eventsString,
                            null,
                            eventsToAttendStrings,
                            userJson.getInt("numEventsCreated"),
                            null,
                            null,
                            //todo: added adminsFollowed parameter, unclear
                            null
                        )
                        LoginRepository.userSelected = user
                        val intent = Intent(packageContext, UserActivity::class.java)
                        packageContext.startActivity(intent)
                    } else {
                        Log.v("Response Success", "User missing information")
                        Toast.makeText(
                            applicationContext,
                            "Unable to load user: User missing information",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Log.v("Response Success", "User not found")
                    Log.v("ERROR", response.getString("errors"))
                    Toast.makeText(
                        applicationContext,
                        "Unable to load user: ${response.getString("errors")}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            Response.ErrorListener { error ->
                Log.e("Response Error", "Error occurred", error)
                Toast.makeText(
                    applicationContext,
                    "Unable to load user: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        )

        RequestQueueSingleton.getInstance(
            LoginActivity.context
        )
            .addToRequestQueue(jsonObjectRequest)

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
    }
}
