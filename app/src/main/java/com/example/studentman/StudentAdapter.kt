package com.example.studentman

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AlertDialog
import android.widget.EditText


class StudentAdapter(
    private val students: MutableList<StudentModel>,
    private val context: Context
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
        val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
        val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
        val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_student_item, parent, false)
        return StudentViewHolder(itemView)
    }

    override fun getItemCount(): Int = students.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.textStudentName.text = student.studentName
        holder.textStudentId.text = student.studentId

        holder.imageRemove.setOnClickListener {
            val removedStudent = students.removeAt(position)
            notifyItemRemoved(position)
            Snackbar.make(holder.itemView, "Removed: ${removedStudent.studentName}", Snackbar.LENGTH_LONG)
                .setAction("Undo") {
                    students.add(position, removedStudent)
                    notifyItemInserted(position)
                }.show()
        }

        holder.imageEdit.setOnClickListener {
            // Hiển thị dialog chỉnh sửa
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_edit_student, null)
            val editName = dialogView.findViewById<android.widget.EditText>(R.id.edit_student_name)
            val editId = dialogView.findViewById<android.widget.EditText>(R.id.edit_student_id)

            // Điền thông tin hiện tại
            editName.setText(student.studentName)
            editId.setText(student.studentId)

            AlertDialog.Builder(context)
                .setTitle("Edit Student")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    val newName = editName.text.toString()
                    val newId = editId.text.toString()

                    if (newName.isNotEmpty() && newId.isNotEmpty()) {
                        students[position] = StudentModel(newName, newId)
                        notifyItemChanged(position) // Cập nhật RecyclerView
                    }
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }

    }

    fun addStudent(newStudent: StudentModel) {
        students.add(newStudent)
        notifyItemInserted(students.size - 1)
    }

    fun updateStudents(newStudents: List<StudentModel>) {
        students.clear()
        students.addAll(newStudents)
        notifyDataSetChanged()
    }
}