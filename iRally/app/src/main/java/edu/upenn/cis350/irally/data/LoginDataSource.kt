package edu.upenn.cis350.irally.data

import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import edu.upenn.cis350.irally.data.model.LoggedInUser
import edu.upenn.cis350.irally.ui.login.LoginActivity
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            // TODO: handle loggedInUser authentication
            val queue = Volley.newRequestQueue(LoginActivity.context)
            val url = "http://10.0.2.2:9000/users/"

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                Response.Listener<String> { response ->
                    Log.v("Response", response)
                },
                Response.ErrorListener { error -> Log.e("Response Error", "Error occured", error) })

            // Add the request to the RequestQueue.
            queue.add(stringRequest)

            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            Result.Success(fakeUser)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

