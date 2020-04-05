package edu.upenn.cis350.irally.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.NetworkImageView
import com.squareup.picasso.Picasso
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.ui.event.CreateEventActivity
import edu.upenn.cis350.irally.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile_picture.view.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONArray

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val profilePicture = profile_picture
        val editProfilePicture = edit
        val profilePronouns = profile_pronouns
        val profileName = profile_name
        val editPicture = edit
        val deleteProfile = delete_profile

        deleteProfile.setOnClickListener{
            val url = "http://10.0.2.2:9000/users/delete"
            //todo: what is this json supposed to be?

           // val jsonObjectRequest:
            //TODO: uncomment this
            //                    val jsonObjectRequest = JsonObjectRequest(url, newUserJSON,
            //                        Response.Listener { response ->
            //                            if (response.status === 'Success') {
            //                                Toast.makeText(
            //                                    applicationContext,
            //                                    "Account deleted.",
            //                                    Toast.LENGTH_LONG
            //                                ).show()
            //                            }
            //                            else {
            //                                Toast.makeText(
            //                                    applicationContext,
            //                                    "Something went wrong.",
            //                                    Toast.LENGTH_LONG
            //                                ).show()
            //                            }
            //                        },
            //                        Response.ErrorListener { error ->
            //                            Toast.makeText(
            //                                applicationContext,
            //                                ("Network connection error. Please try again." +
            //                                        "Error: %s").format(error.toString()),
            //                                Toast.LENGTH_LONG
            //                            ).show()
            //
            //                        }
            //                    )

            // Access the RequestQueue through your singleton class.
            //     edu.upenn.cis350.irally.data.RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
            val intent = Intent(this, LoginActivity::class.java);
            startActivity(intent);
        }


        val createEvent = create_event_from_profile
        create_event_from_profile.setOnClickListener {
            // Handler code here.
            //CHANGE BACK TO LOGIN
            val intent = Intent(LoginActivity.context, CreateEventActivity::class.java);
            startActivity(intent);
        }

        edit.setOnClickListener {
            val intent = Intent(LoginActivity.context, ProfilePicture::class.java)
            startActivity(intent);

        }

        fun createEventButtons(events: JSONArray) {
            for (i in 0 until events.length()){
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
