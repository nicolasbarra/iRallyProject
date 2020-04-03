package edu.upenn.cis350.irally.ui.profile

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
        val linearLayout = event_button_layout


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
