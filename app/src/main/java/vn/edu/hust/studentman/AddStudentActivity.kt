package vn.edu.hust.studentman

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_student)

        val editName = findViewById<EditText>(R.id.edit_student_name)
        val editId = findViewById<EditText>(R.id.edit_student_id)
        val btnSave = findViewById<Button>(R.id.btn_save)

        btnSave.setOnClickListener {
            val name = editName.text.toString()
            val id = editId.text.toString()

            if (name.isNotEmpty() && id.isNotEmpty()) {
                val newStudent = StudentModel(name, id)
                val intent = intent
                intent.putExtra("student", newStudent)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}
