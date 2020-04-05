package edu.upenn.cis350.irally.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import edu.upenn.cis350.irally.data.LoginRepository

import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.data.model.LoggedInUser
import org.json.JSONObject

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        try {
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
                            getUser(username)
                        } else {
                            // password is incorrect
                            Log.v("Response Success", "Password incorrect")
                            _loginResult.value = LoginResult(error = "Password is incorrect.")
                        }
                    } else {
                        _loginResult.value = LoginResult(error = response.getString("errors"))
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Response Error", "Error occurred", error)
                    _loginResult.value = LoginResult(error = "Error logging in: ${error.message}")
                }
            )

            // Add the request to the RequestQueue.
            RequestQueueSingleton.getInstance(LoginActivity.context)
                .addToRequestQueue(loginJsonObjectRequest)
        } catch (e: Throwable) {
            _loginResult.value = LoginResult(error = "Error logging in: ${e.message}")
        }
    }

    // TODO: don't do this here, do this once the user has logged in using viewmodel
    // https://developer.android.com/topic/libraries/architecture/viewmodel
    private fun getUser(username: String) {
        try {
            Log.v("PROCESS", "in getUser")
            val interests: MutableSet<String>? = null

            val requestBody = JSONObject()
            requestBody.put("username", username)

            val jsonObjectRequest = JsonObjectRequest(
                "http://10.0.2.2:9000/users/",
                requestBody,
                Response.Listener { response ->
                    if (response.has("status")) {
                        // error occured
                        _loginResult.value = LoginResult(error = "Unable to retrieve profile.")
                    } else {
                        val personalInfo = response.getJSONObject("personalInfo")
                        val displayName = personalInfo.getString("name")
                        val email = personalInfo.getString("email")
                        val gender = personalInfo.getString("gender")
                        val genderPronouns = personalInfo.getString("genderPronouns")
                        val profilePictureLink = personalInfo.getString("profilePictureLink")
                        if (!displayName.isNullOrEmpty() && !email.isNullOrEmpty()
                            && !gender.isNullOrEmpty() && !genderPronouns.isNullOrEmpty()
                        ) {
                            val user = LoggedInUser(
                                username,
                                displayName,
                                email,
                                gender,
                                genderPronouns,
                                profilePictureLink
                            )
                            _loginResult.value =
                                LoginResult(success = user)
                            LoginRepository.setLoggedInUser(user)
                        } else {
                            _loginResult.value = LoginResult(error = "Profile information missing")
                        }
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Response Error", "Error occurred", error)
                    _loginResult.value = LoginResult(error = "Unable to retrieve profile.")
                }
            )

            // Add the request to the RequestQueue.
            RequestQueueSingleton.getInstance(LoginActivity.context)
                .addToRequestQueue(jsonObjectRequest)
        } catch (e: Throwable) {
            _loginResult.value = LoginResult(error = "Error logging in: ${e.message}")
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
