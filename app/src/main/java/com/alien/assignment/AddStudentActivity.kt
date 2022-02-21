package com.alien.assignment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alien.assignment.databinding.ActivityMainBinding
import com.alien.assignment.model.Student
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException


class AddStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private var selectedGender = ""
    private var filePath: Uri? = null
    val PICK_IMAGE_REQUEST = 21

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Dropdown menu(Gender selection)
        val genders = resources.getStringArray(R.array.Gender)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_itemview, genders)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.autoCompleteTextView.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            selectedGender = genders[position]
        })


        //profile picture selection
        binding.profileImage.setOnClickListener {
            selectImage()
        }


        //firebase
        binding.btnProceed.setOnClickListener {

            val studentName = binding.etStudentName.text.toString()
            val emailId = binding.etEmailId.text.toString()
            val birthDate = binding.etBirthday.text.toString()
            val gender = selectedGender

            if (studentName.isEmpty() || emailId.isEmpty() || birthDate.isEmpty() || gender.isNullOrBlank()) {
                Toast.makeText(this, "Please fill the all information", Toast.LENGTH_SHORT).show()
            } else {

                database = FirebaseDatabase.getInstance()
                    .getReference("Students") // storing the data using path as reference to the table of firebase database
                val students = Student(studentName, emailId, birthDate, gender)
                database.child(studentName).setValue(students).addOnSuccessListener {
                    Toast.makeText(this, "Data saved Successful", Toast.LENGTH_SHORT).show()

                    // After data successfully saved clear all the editText Fields
                    binding.etStudentName.text.clear()
                    binding.etEmailId.text.clear()
                    binding.etBirthday.text.clear()
                    binding.autoCompleteTextView.text.clear()

                    val intent = Intent(this, ShowStudentActivity::class.java)
                    startActivity(intent)


                }.addOnFailureListener {
                    Toast.makeText(this, "failed!! Check your connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun uploadImage() {
        /**
         * TODO upload image in firebase
         * */
    }

    //function for selecting the image from file storage
    private fun selectImage() {

        // Intent for getting the image type data using startActivityForResult method
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )
    }


    //getting image path setting the image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {

            // Get the path Uri of data
            filePath = data?.data
            try {

                // Setting image on image view using Bitmap
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                binding.profileImage.setImageBitmap(bitmap)

            } catch (e: IOException) {
                // Log the exception
                e.printStackTrace()
            }
        }

    }
}
