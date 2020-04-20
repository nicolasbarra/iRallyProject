package edu.upenn.cis350.irally.data.repository

import edu.upenn.cis350.irally.data.model.LoggedInUser
import edu.upenn.cis350.irally.ui.profile.ProfileActivity

/**
 * Class that handles authentication w/ login credentials and retrieves user information,
 * and maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository() {

    companion object {
        // in-memory cache of the loggedInUser object
        var user: LoggedInUser? = null
            private set

        fun logout() {
            user = null
            ProfileActivity.hasUploaded = false
            EventRepository.eventsCreatedByUser.clear()
        }

        fun setLoggedInUser(loggedInUser: LoggedInUser) {
            user = loggedInUser
            // If user credentials will be cached in local storage, it is recommended it be encrypted
            // @see https://developer.android.com/training/articles/keystore
        }

        var userSelected: LoggedInUser? = null
    }
}
