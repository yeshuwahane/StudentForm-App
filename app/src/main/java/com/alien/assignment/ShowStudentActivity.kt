package com.alien.assignment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.alien.assignment.databinding.ActivityShowStudentBinding
import com.alien.assignment.model.Student
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ShowStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowStudentBinding
    private lateinit var refs: DatabaseReference
    private lateinit var studentList: MutableList<Student>
    private lateinit var adapter: StudentAdapter
    private lateinit var bitMap: Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter = StudentAdapter(this)
        binding.rvStudent.adapter = adapter
        binding.rvStudent.layoutManager = LinearLayoutManager(this)


        studentList = arrayListOf()



        //retrieving data from firebase
        refs = FirebaseDatabase.getInstance().getReference("Students") // reference for getting the table we need from firebase

        //retrieving image from firebase


        refs.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                /**
                 * snapshot value is in HashMap
                 * adding child data to a List<Student> for RecyclerView Adapter
                 * */
                snapshot.children.forEach {
                    Log.d("Student", "$it")
                    val stu = it.getValue(Student::class.java)
                    studentList.add(stu!!)

                    //retrieving image from firebase
                    val storageRef = FirebaseStorage.getInstance().reference.child("Students/${stu.studentName}")
                    val localFile = File.createTempFile("${stu.studentName}",".img")
                    storageRef.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        bitMap = bitmap



                    }
                }

                Log.d("Student", "list : $bitMap")

                adapter.setStudentData(studentList)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Student", "$error")
            }

        })


    }
}