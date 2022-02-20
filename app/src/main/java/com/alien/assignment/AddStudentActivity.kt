package com.alien.assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import com.alien.assignment.databinding.ActivityMainBinding
import android.widget.Toast

import android.widget.AdapterView.OnItemClickListener
import com.alien.assignment.model.Student
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private var selectedGender = ""

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



        //firebase
        binding.btnProceed.setOnClickListener {

            val studentName = binding.etStudentName.text.toString()
            val emailId = binding.etEmailId.text.toString()
            val birthDate = binding.etBirthday.text.toString()
            val gender = selectedGender


            if (studentName.isEmpty() || emailId.isEmpty() || birthDate.isEmpty() || gender.isEmpty()) {
                Toast.makeText(this, "Please enter student name", Toast.LENGTH_SHORT).show()
            } else {

                database = FirebaseDatabase.getInstance().getReference("Students")
                val students = Student(studentName, emailId, birthDate, gender)
                database.child(studentName).setValue(students).addOnSuccessListener {
                    Toast.makeText(this, "Data saved Successful", Toast.LENGTH_SHORT).show()

                    //scope Function for intent
                    Intent(this, ShowStudentActivity::class.java).apply {
                        startActivity(this)
                    }


                }.addOnFailureListener {
                    Toast.makeText(this, "failed!! Check your connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


    }


}
