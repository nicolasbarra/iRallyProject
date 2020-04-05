package edu.upenn.cis350.irally.data

import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import edu.upenn.cis350.irally.data.model.LoggedInUser
import edu.upenn.cis350.irally.ui.login.LoginActivity
import org.json.JSONObject
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            Log.v("PROCESS", "beginning network requests now")
            // TODO: handle loggedInUser authentication
            var result = false
            var errorMessage: String? = null

            val loginRequestBody = JSONObject()
            loginRequestBody.put("username", username).put("password", password)

            val loginJsonObjectRequest = JsonObjectRequest(
                "http://10.0.2.2:9000/users/login",
                loginRequestBody,
                Response.Listener { response ->
                    Log.v("PROCESS", "got a response")
                    Log.v("RESPONSE", response.toString())
                    if (response.getString("status") == "Success") {
                        if (response.getString("passwordStatus") == "Correct") {
                            // password is correct
                            Log.v("Response Success", "Password correct")
                            result = true
                        } else {
                            // password is incorrect
                            Log.v("Response Success", "Password incorrect")
                            result = false
                            errorMessage = "Password is incorrect."
                            Toast.makeText(
                                LoginActivity.context,
                                errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        result = false
                        errorMessage = response.getString("errors")
                        Toast.makeText(
                            LoginActivity.context,
                            errorMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Response Error", "Error occured", error)
                    result = false
                    errorMessage = error.message
                }
            )

            // Add the request to the RequestQueue.
            RequestQueueSingleton.getInstance(LoginActivity.context)
                .addToRequestQueue(loginJsonObjectRequest)

            Log.v("PROCESS", "result is $result")
            if (result) {
                var displayName: String? = null
                var email: String? = null
                var gender: String? = null
                var genderPronouns: String? = null
                val interests: MutableSet<String>? = null
                var profilePictureLink: String? = null

                val requestBody = JSONObject()
                requestBody.put("username", username)

                val jsonObjectRequest = JsonObjectRequest(
                    "http://10.0.2.2:9000/users/",
                    requestBody,
                    Response.Listener { response ->
                        if (response.has("status")) {
                            // error occured
                            throw Exception("Unable to retrieve profile.")
                        } else {
                            val personalInfo = response.getJSONObject("personalInfo")
                            displayName = personalInfo.getString("name")
                            email = personalInfo.getString("email")
                            gender = personalInfo.getString("gender")
                            genderPronouns = personalInfo.getString("genderPronouns")
                            profilePictureLink = personalInfo.getString("profilePictureLink")
                        }
                    },
                    Response.ErrorListener { error ->
                        Log.e("Response Error", "Error occured", error)
                        throw Exception("Unable to retrieve profile.")
                    }
                )

                // Add the request to the RequestQueue.
                RequestQueueSingleton.getInstance(LoginActivity.context)
                    .addToRequestQueue(jsonObjectRequest)

                if (!displayName.isNullOrEmpty() && !email.isNullOrEmpty()
                    && !gender.isNullOrEmpty() && !genderPronouns.isNullOrEmpty()
                ) {
                    val user = LoggedInUser(
                        username,
                        displayName!!,
                        email!!,
                        gender!!,
                        genderPronouns!!,
                        profilePictureLink
                    )
                    Result.Success(user)
                } else {
                    throw Exception("Profile information missing")
                }
            } else {
                throw Exception(errorMessage)
            }
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

