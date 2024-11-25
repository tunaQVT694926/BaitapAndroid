package vn.edu.hust.studentman

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_student)

        val editName = findViewById<EditText>(R.id.edit_student_name)
        val editId = findViewById<EditText>(R.id.edit_student_id)
        val btnSave = findViewById<Button>(R.id.btn_save)

        val student = intent.getSerializableExtra("student") as StudentModel
        editName.setText(student.studentName)
        editId.setText(student.studentId)

        btnSave.setOnClickListener {
            val name = editName.text.toString()
            val id = editId.text.toString()

            if (name.isNotEmpty() && id.isNotEmpty()) {
                val updatedStudent = StudentModel(name, id)
                val intent = intent
                intent.putExtra("student", updatedStudent)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}
