package edu.upenn.cis350.irally.ui.login

import edu.upenn.cis350.irally.data.model.LoggedInUser

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUser? = null,
    val error: String? = null
)
