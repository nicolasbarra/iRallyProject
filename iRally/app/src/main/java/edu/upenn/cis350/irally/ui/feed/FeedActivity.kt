package edu.upenn.cis350.irally.ui.feed

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import edu.upenn.cis350.irally.R
import edu.upenn.cis350.irally.ui.profile.ProfileActivity
import edu.upenn.cis350.irally.ui.search.SearchActivity

class FeedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        //TOOLBAR
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.toolbar)
    }

    fun goToProfile(v: View) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        // TODO, should these startActivities be ContextCompat.startActivity(intent);
    }

    fun goToFeed(v: View) {
    }

    fun goToSearch(v: View) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}

