package edu.upenn.cis350.irally.data

import edu.upenn.cis350.irally.data.model.Admin
import edu.upenn.cis350.irally.data.model.Event
import edu.upenn.cis350.irally.data.model.LoggedInUser

class AdminRepository {

    companion object {
        // in-memory cache of the loggedInUser object
        var admins: MutableSet<Admin> = mutableSetOf()
        var adminsFollowedByUser: MutableSet<String> = mutableSetOf()
        var adminSelected: Admin? = null
    }
}