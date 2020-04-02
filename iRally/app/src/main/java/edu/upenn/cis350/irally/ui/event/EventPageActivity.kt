package edu.upenn.cis350.irally.ui.event

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.upenn.cis350.irally.R
import kotlinx.android.synthetic.main.activity_event_page.*

class EventPageActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_event_page)

        val eventName = event_page_name


    }
}