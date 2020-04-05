package edu.upenn.cis350.irally.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.ui.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.util.*


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        //GENDER SPINNER
        val spinner = findViewById<Spinner>(R.id.gender_spinner)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.gender_array, android.R.layout.simple_spinner_item
        )
        //Specify the layout to use when the list of choices appears
        //Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        // Apply the adapter to the spinner
        spinner.adapter = adapter


        //BACK TO LOGIN PAGE
        val login = login
        login.setOnClickListener {
            // Handler code here.
            //TODO: GET RID OF IT
            val intent = Intent(this, ProfileActivity::class.java);
            startActivity(intent);
        }


        //getting the stuff from xml
        val usernameTxt = username
        val passwordTxt = password
        val confirmPasswordTxt = confirm_password
        val emailTxt = email
        val nameTxt = name
        val interestsTxt = interests
        val pronounsTxt = pronouns


        //functions to check validity of inputs
        //check length
        fun isValidLength(input: String, len: Int): Boolean {
            return input.length >= len
        }

        fun isEmailValid(email: String): Boolean {
            return email.isNotEmpty() && email.length > 2 &&
                    Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isDataValid(): String {

            //read in the inputs
            val username = usernameTxt.text.toString().toLowerCase(Locale.ROOT)
            val password = passwordTxt.text.toString()
            val confirmPassword = confirmPasswordTxt.text.toString()
            val email = emailTxt.text.toString()
            val name = nameTxt.text.toString()
            val interests = interestsTxt.text.toString()
            val pronouns = pronounsTxt.text.toString()
            val gender: String? = spinner.selectedItem.toString()

            if (username.isEmpty() || password.isEmpty() ||
                confirmPassword.isEmpty() || interests.isEmpty()
                || email.isEmpty() || name.isEmpty() || pronouns.isEmpty()
            ) {
                return "One or more fields left blank."
            }
            if (!isValidLength(username, 3)) {
                return "Please create a username that is at least 3 characters long."
            }
            if (!isValidLength(password, 5)) {
                return "Please create a password that is at least 5 characters long."
            }
            if (confirmPassword != password) {
                return "Please make sure your password is typed correctly."
            }
            if (!isEmailValid(email)) {
                return "Please enter an email address."
            } else return "{\"username\":" + username + ",\"password\":" + password+ ",\n" +
                    "\"personalInfo\":{\"name\":" + name +",\"email\":" + email + ",\"gender\" +\n" +
                    ":" + gender + ",\"genderPronouns\":" + pronouns + ",\"interests\":" + interests + "}}"
        }



        //Regis
        // ter back to login
        val submit = register
        submit.setOnClickListener {
            //ADD CASE WHERE USER ALREADY EXISTS
            if (isDataValid().contains("{")) {

                val newUserJSON = JSONObject(isDataValid())

                val url = "http://10.0.2.2:9000/users/create"
                //TODO: uncomment this
                //                    val jsonObjectRequest = JsonObjectRequest(url, newUserJSON,
                //                        Response.Listener { response ->
                //                            if (response.status === 'Success') {
                //                                Toast.makeText(
                //                                    applicationContext,
                //                                    "Successfully registered. Return to login page to login.",
                //                                    Toast.LENGTH_LONG
                //                                ).show()
                //                            }
                //                            else {
                //                                Toast.makeText(
                //                                    applicationContext,
                //                                    "Username already taken. Please choose new username.",
                //                                    Toast.LENGTH_LONG
                //                                ).show()
                //                            }
                //                        },
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

                // Access the RequestQueue through your singleton class.
                //TODO: uncomment, change login??
                //     RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

                //DO SOMETHING WITH THE JSON
            } else {
                Toast.makeText(
                    applicationContext,
                    isDataValid(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
