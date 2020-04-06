package edu.upenn.cis350.irally.ui.profile

import edu.upenn.cis350.irally.R

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import edu.upenn.cis350.irally.data.LoginRepository
import edu.upenn.cis350.irally.data.RequestQueueSingleton
import edu.upenn.cis350.irally.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_profile.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.lang.System.load

class ProfilePicture : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var imageButton: Button
    private lateinit var sendButton: Button
    private var imageData: ByteArray? = null
    private var imageFile: File? = null

    private val postURL: String = "http://10.0.2.2:9000/files/imageUploadUser"

    companion object {
        private const val IMAGE_PICK_CODE = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_picture)

        imageView = findViewById(R.id.imageView)

        imageButton = findViewById(R.id.imageButton)
        imageButton.setOnClickListener {
            launchGallery()
        }
        sendButton = findViewById(R.id.sendButton)
        sendButton.setOnClickListener {
            uploadImage()
        }
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }


    private fun uploadImage() {
        imageData ?: return
        val request = object : VolleyFileUploadRequest(
            Method.POST,
            postURL,
            Response.Listener {
                Log.v("response is: $it", "$it")
                LoginRepository.user?.profilePictureLink =
                    "https://profilepicturesirally.s3.amazonaws.com/1586203182311"
                ProfileActivity.hasUploaded = true
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            },
            Response.ErrorListener {
                Log.v("error is: $it", "$it")
            }
        ) {
            override fun getByteData(): MutableMap<String, FileDataPart> {
                var params = HashMap<String, FileDataPart>()
                params["image"] = FileDataPart("image", imageData!!, "jpeg")
                return params
            }
        }
        Volley.newRequestQueue(this).add(request)
    }

    @Throws(IOException::class)
    private fun createImageData(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.buffered()?.use {
            imageData = it.readBytes()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val uri = data?.data
            if (uri != null) {
                imageView.setImageURI(uri)
                createImageData(uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}
