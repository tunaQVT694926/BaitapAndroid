package vn.edu.hust.studentman

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

  private val students = mutableListOf(
    StudentModel("Nguyễn Văn An", "SV001"),
    StudentModel("Trần Thị Bảo", "SV002"),
    StudentModel("Lê Hoàng Cường", "SV003"),
    StudentModel("Phạm Thị Dung", "SV004"),
    StudentModel("Đỗ Minh Đức", "SV005"),
    StudentModel("Vũ Thị Hoa", "SV006"),
    StudentModel("Hoàng Văn Hải", "SV007"),
    StudentModel("Bùi Thị Hạnh", "SV008"),
    StudentModel("Đinh Văn Hùng", "SV009"),
    StudentModel("Nguyễn Thị Linh", "SV010"),
    StudentModel("Phạm Văn Long", "SV011"),
    StudentModel("Trần Thị Mai", "SV012"),
    StudentModel("Lê Thị Ngọc", "SV013"),
    StudentModel("Vũ Văn Nam", "SV014"),
    StudentModel("Hoàng Thị Phương", "SV015"),
    StudentModel("Đỗ Văn Quân", "SV016"),
    StudentModel("Nguyễn Thị Thu", "SV017"),
    StudentModel("Trần Văn Tài", "SV018"),
    StudentModel("Phạm Thị Tuyết", "SV019"),
    StudentModel("Lê Văn Vũ", "SV020")
  )
  private lateinit var studentAdapter: StudentAdapter
  private var selectedStudentIndex: Int = -1
  private var deletedStudent: StudentModel? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    studentAdapter = StudentAdapter(this, students)

    val listView = findViewById<ListView>(R.id.list_view_students)
    listView.adapter = studentAdapter

    registerForContextMenu(listView)

    listView.setOnItemLongClickListener { _, _, position, _ ->
      selectedStudentIndex = position
      false
    }

    findViewById<View>(R.id.btn_add_new).setOnClickListener {
      val intent = Intent(this, AddStudentActivity::class.java)
      startActivityForResult(intent, REQUEST_ADD_STUDENT)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.main_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.menu_add) {
      val intent = Intent(this, AddStudentActivity::class.java)
      startActivityForResult(intent, REQUEST_ADD_STUDENT)
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onCreateContextMenu(
    menu: ContextMenu?,
    v: View?,
    menuInfo: ContextMenu.ContextMenuInfo?
  ) {
    super.onCreateContextMenu(menu, v, menuInfo)
    menuInflater.inflate(R.menu.context_menu, menu)
  }

  override fun onContextItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.menu_edit -> {
        val intent = Intent(this, EditStudentActivity::class.java)
        intent.putExtra("student", students[selectedStudentIndex])
        startActivityForResult(intent, REQUEST_EDIT_STUDENT)
        return true
      }
      R.id.menu_remove -> {
        // Hiển thị hộp thoại xác nhận xóa
        showDeleteConfirmationDialog()
        return true
      }
    }
    return super.onContextItemSelected(item)
  }

  private fun showDeleteConfirmationDialog() {
    val studentToDelete = students[selectedStudentIndex]

    val dialog = AlertDialog.Builder(this)
      .setTitle("Xác nhận xóa")
      .setMessage("Bạn có chắc chắn muốn xóa sinh viên ${studentToDelete.studentName}?")
      .setPositiveButton("Xóa") { dialog, _ ->
        // Xóa sinh viên
        deleteStudent(studentToDelete)
        dialog.dismiss()
      }
      .setNegativeButton("Hủy", null)
      .create()

    dialog.show()
  }

  private fun deleteStudent(studentToDelete: StudentModel) {
    // Lưu lại sinh viên vừa xóa để có thể undo
    deletedStudent = studentToDelete

    // Xóa sinh viên khỏi danh sách
    students.removeAt(selectedStudentIndex)
    updateAdapter()

    // Hiển thị snackbar thông báo và cho phép undo
    Snackbar.make(
      findViewById(R.id.list_view_students),
      "${studentToDelete.studentName} đã bị xóa",
      Snackbar.LENGTH_LONG
    ).setAction("UNDO") {
      restoreStudent(studentToDelete)
    }.show()
  }

  private fun restoreStudent(student: StudentModel) {
    // Khôi phục lại sinh viên đã bị xóa
    students.add(selectedStudentIndex, student)
    updateAdapter()
  }

  private fun updateAdapter() {
    studentAdapter.notifyDataSetChanged()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == RESULT_OK) {
      when (requestCode) {
        REQUEST_ADD_STUDENT -> {
          val newStudent = data?.getSerializableExtra("student") as? StudentModel
          newStudent?.let {
            students.add(it)
            updateAdapter()
          }
        }
        REQUEST_EDIT_STUDENT -> {
          val updatedStudent = data?.getSerializableExtra("student") as? StudentModel
          updatedStudent?.let {
            students[selectedStudentIndex] = it
            updateAdapter()
          }
        }
      }
    }
  }

  companion object {
    const val REQUEST_ADD_STUDENT = 1
    const val REQUEST_EDIT_STUDENT = 2
  }
}
