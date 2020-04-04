package edu.upenn.cis350.irally.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.ui.login.LoginActivity
import edu.upenn.cis350.irally.ui.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject


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
            //CHANGE BACK TO LOGIN
            val intent = Intent(LoginActivity.context, ProfileActivity::class.java);
            startActivity(intent);
        }


        //getting the stuff from xml
        val username_txt = username
        val password_txt = password
        val confirm_password_txt = confirm_password
        val email_txt = email
        val name_txt = name
        val interests_txt = interests
        val pronouns_txt = pronouns


        //functions to check validity of inputs
        //check length
        fun isValidLength(input: String, len: Int): Boolean {
            return input.length >= len
        }

        fun isEmailValid(email: String): Boolean {
            return !email.isNullOrEmpty() && email.length > 2 &&
                    Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isDataValid(): String {

            //read in the inputs
            val username = username_txt.text.toString().toLowerCase()
            val password = password_txt.text.toString()
            val confirm_password = confirm_password_txt.text.toString()
            val email = email_txt.text.toString()
            val name = name_txt.text.toString()
            val interests = interests_txt.text.toString()
            val pronouns = pronouns_txt.text.toString()

            if (username.isNullOrEmpty() || password.isNullOrEmpty() ||
                confirm_password.isNullOrEmpty() || interests.isNullOrEmpty()
                || email.isNullOrEmpty() || name.isNullOrEmpty() || pronouns.isNullOrEmpty()
            ) {
                return "One or more fields left blank."
            }
            if (!isValidLength(username, 3)) {
                return "Please create a username that is at least 3 characters long."
            }
            if (!isValidLength(password, 5)) {
                return "Please create a password that is at least 5 characters long."
            }
            if (confirm_password != password) {
                return "Please make sure your password is typed correctly."
            }
            if (!isEmailValid(email)) {
                return "Please enter an email address."
            } else return ""
        }



        //Regis
        // ter back to login
        val submit = register
        submit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View): Unit {
                //ADD CASE WHERE USER ALREADY EXISTS
                if (isDataValid() == "") {

                    val username = username_txt.text.toString().toLowerCase()
                    val password = password_txt.text.toString()
                    val confirm_password = confirm_password_txt.text.toString()
                    val email = email_txt.text.toString()
                    val name = name_txt.text.toString()
                    val interests = interests_txt.text.toString()
                    val pronouns = pronouns_txt.text.toString()
                    var gender: String? = spinner.selectedItem.toString()


                    val newUserJSON = JSONObject(
                        "{\"username\":username,\"password\":password,\n" +
                                "\"personalInfo\":{\"name\":name,\"email\":email,\"gender\" +\n" +
                                ":gender,\"genderPronouns\":pronouns,\"interests\":interests}}"
                    )

                    val url = "http://10.0.2.2:9000/users/"

                    val jsonObjectRequest = JsonObjectRequest(url, newUserJSON,
                        Response.Listener { response ->
                            if (response.status === 'Success') {
                                Toast.makeText(
                                    applicationContext,
                                    "Successfully registered. Return to login page to login.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            else {
                                Toast.makeText(
                                    applicationContext,
                                    "Username already taken. Please choose new username.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        Response.ErrorListener { error ->
                            Toast.makeText(
                                applicationContext,
                                ("Network connection error. Please try again. " +
                                        "Error: %s").format(error.toString()),
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    )

// Access the RequestQueue through your singleton class.
                    //TODO: this is a loginActivity
                    MySingleton.getInstance(LoginActivity.context).addToRequestQueue(jsonObjectRequest)

                    //DO SOMETHING WITH THE JSON
                } else {
                    Toast.makeText(
                        applicationContext,
                        isDataValid(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}
