import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.data.model.Admin
import edu.upenn.cis350.irally.data.repository.AdminRepository
import edu.upenn.cis350.irally.ui.login.LoginActivity
import edu.upenn.cis350.irally.ui.profile.AdminActivity
import org.json.JSONArray
import org.json.JSONObject


fun loadAdminInfo(
    view: View,
    adminText: String,
    packageContext: Context,
    applicationContext: Context
) {
    val requestBody = JSONObject()
    requestBody.put("username", adminText)

    val jsonObjectRequest = JsonObjectRequest(
        "http://10.0.2.2:9000/admins/profileAdmin",
        requestBody,
        Response.Listener { response ->
            Log.v("PROCESS", "got a response")
            Log.v("RESPONSE", response.toString())
            if (response.getString("status") == "Success") {
                val adminJson = response.getJSONObject("admin")
                val numEventsCreated = adminJson.getInt("numEventsCreated")
                val personalInfo = adminJson.getJSONObject("adminInfo")
                val displayName = personalInfo.getString("adminName")
                val description = personalInfo.getString("description")
                val politicalAffiliation = personalInfo.getString("politicalAffiliation");
                val goalsJSON = personalInfo.getJSONArray("goals")
                val goalsStrings = mutableSetOf<String>()
                for (i in 0 until goalsJSON.length()) {
                    goalsStrings.add(
                        goalsJSON.get(i).toString()
                    )
                }
                val profilePictureLink = if (personalInfo.has("profilePictureLink")) {
                    personalInfo.getString("profilePictureLink")
                } else {
                    ""
                }
                val interestsJSON = personalInfo.getJSONArray("interests")
                val interestsStrings = mutableSetOf<String>()
                for (i in 0 until interestsJSON.length()) {
                    interestsStrings.add(
                        interestsJSON.get(i).toString()
                    )
                }
                val eventsToHostJson =
                    if (adminJson.has("eventsToHost")) {
                        adminJson.getJSONArray("eventsToHost")
                    } else {
                        JSONArray()
                    }
                val eventsString = mutableSetOf<String>()
                for (i in 0 until eventsToHostJson.length()) {
                    eventsString.add(
                        eventsToHostJson.get(i).toString()
                    )
                }
                if (!displayName.isNullOrEmpty()) {
                    val admin = Admin(
                        adminText,
                        displayName,
                        description,
                        politicalAffiliation,
                        goalsStrings,
                        interestsStrings,
                        profilePictureLink,
                        null,
                        eventsString,
                        numEventsCreated
                    )
                    AdminRepository.adminSelected = admin
                    Log.v("admin", admin.toString());
                    val intent = Intent(packageContext, AdminActivity::class.java)
                    packageContext.startActivity(intent)
                } else {
                    Log.v("Response Success", "User missing information")
                    Toast.makeText(
                        applicationContext,
                        "Unable to load user: User missing information",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Log.v("Response Success", "User not found")
                Log.v("ERROR", response.getString("errors"))
                Toast.makeText(
                    applicationContext,
                    "Unable to load user: ${response.getString("errors")}",
                    Toast.LENGTH_LONG
                ).show()
            }
        },
        Response.ErrorListener { error ->
            Log.e("Response Error", "Error occurred", error)
            Toast.makeText(
                applicationContext,
                "Unable to load user: ${error.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    )

    RequestQueueSingleton.getInstance(LoginActivity.context)
        .addToRequestQueue(jsonObjectRequest)
}