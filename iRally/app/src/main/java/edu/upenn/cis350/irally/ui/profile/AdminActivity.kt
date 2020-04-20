package edu.upenn.cis350.irally.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.data.loadEventInfo
import edu.upenn.cis350.irally.data.repository.AdminRepository
import edu.upenn.cis350.irally.data.repository.LoginRepository
import edu.upenn.cis350.irally.ui.feed.FeedActivity
import edu.upenn.cis350.irally.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_user.*

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val adminInfo = AdminRepository.adminSelected!!
        Log.v("AdminInfo ${adminInfo.username}", adminInfo.toString())
        admin_name.text = adminInfo.displayName
        admin_political_affiliation.text = adminInfo.politicalAffiliation
        admin_profile_description.text = adminInfo.description
        admin_goals.text =
            adminInfo.goals.toString().filter { e -> e != '[' && e != ']' }
        admin_interests.text =
            adminInfo.interests.toString().filter { e -> e != '[' && e != ']' }
        if (!adminInfo.profilePictureLink.isNullOrEmpty()) {
            Picasso.with(this).load(adminInfo.profilePictureLink)
                .into(profile_picture)
        }


//        friend.setOnClickListener {
//            // TODO: set friend button onclick listener
//        }

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
