package com.alien.assignment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alien.assignment.databinding.StudentItemLayoutBinding
import com.alien.assignment.model.Student

class StudentAdapter(val context: Context) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {


    var data: List<Student> = arrayListOf()

    fun setStudentData(data: List<Student>) {
        this.data = data
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            StudentItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvStudentName.text = data[position].studentName
        holder.binding.tvEmail.text = data[position].emailId
        holder.binding.tvBirthdate.text = data[position].birthDate
        holder.binding.tvGender.text = data[position].gender
        holder.binding.ivProfileImage.setImageBitmap(data[position].bitmap)

    }


    class ViewHolder(val binding: StudentItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}