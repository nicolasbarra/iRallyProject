package edu.upenn.cis350.irally.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.upenn.cis350.irally.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        map_button.setOnClickListener {
            //todo: set to new activity?? with 5 closest events
        }
    }
}
