package edu.upenn.cis350.irally.ui.profile


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.data.repository.LoginRepository
import edu.upenn.cis350.irally.ui.feed.FeedActivity
import edu.upenn.cis350.irally.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val userInfo = LoginRepository.userSelected!!
        user_profile_name.text = userInfo.displayName
        user_profile_pronouns.text = userInfo.genderPronouns
        user_profile_interests.text =
            userInfo.interests.toString().filter { e -> e != '[' && e != ']' }
        if (!userInfo.profilePictureLink.isNullOrEmpty()) {
            Picasso.with(this).load(userInfo.profilePictureLink)
                .into(profile_picture)
        }

        friend.setOnClickListener {
            // TODO: set friend button onclick listener
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
