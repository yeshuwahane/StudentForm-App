package com.alien.assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.alien.assignment.databinding.ActivityShowStudentBinding
import com.alien.assignment.model.Student
import com.google.firebase.database.*

class ShowStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowStudentBinding
    private lateinit var refs: DatabaseReference
    private lateinit var studentList: MutableList<Student>
    private lateinit var adapter: StudentAdapter


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
                }

                Log.d("Student", "list : $studentList")

                adapter.setStudentData(studentList)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Student", "$error")
            }

        })


    }
}