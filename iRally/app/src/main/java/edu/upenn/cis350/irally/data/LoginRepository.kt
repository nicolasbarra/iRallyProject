package edu.upenn.cis350.irally.data

import edu.upenn.cis350.irally.data.model.LoggedInUser

/**
 * Class that handles authentication w/ login credentials and retrieves user information,
 * and maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository() {

    companion object {
        // in-memory cache of the loggedInUser object
        var user: LoggedInUser? = null
            private set

        val isLoggedIn: Boolean
            get() = user != null

        init {
            // If user credentials will be cached in local storage, it is recommended it be encrypted
            // @see https://developer.android.com/training/articles/keystore
            user = null
        }

        fun logout() {
            user = null
            // TODO: revoke authentication
        }

        fun setLoggedInUser(loggedInUser: LoggedInUser) {
            this.user = loggedInUser
            // If user credentials will be cached in local storage, it is recommended it be encrypted
            // @see https://developer.android.com/training/articles/keystore
        }
    }
}
