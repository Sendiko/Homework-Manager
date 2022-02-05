package com.example.kotlinroom.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.InputDevice
import android.view.View
import com.example.kotlinroom.R
import com.example.kotlinroom.room.Const
import com.example.kotlinroom.room.Homework
import com.example.kotlinroom.room.HomeworkDB
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {

    val db by lazy { HomeworkDB(this) }
    private var homeworkId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setupView()
        setupListener()
        homeworkId = intent.getIntExtra("intent_id", 0)
        // TODO : Toast.makeText(this, idolsId.toString(), Toast.LENGTH_SHORT).show()
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        when (intent.getIntExtra("intent_type", 0)){
            Const.TYPE_CREATE -> {
                btn_updt.visibility = View.GONE
            }
            Const.TYPE_READ -> {
                btn_save.visibility = View.GONE
                et_name.inputType = InputDevice.KEYBOARD_TYPE_NONE
                btn_updt.visibility = View.GONE
                et_group.inputType = InputDevice.KEYBOARD_TYPE_NONE
                deskripsi.inputType = InputDevice.KEYBOARD_TYPE_NONE
                getIdol()
            }
            Const.TYPE_UPDATE -> {
                btn_save.visibility = View.GONE
                getIdol()
            }
        }
    }

    fun setupListener(){
        btn_save.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch{
                db.homeworkDao().addHomework(
                    Homework(0, et_name.text.toString(), et_group.text.toString(), deskripsi.text.toString())
                )
                finish()
            }
        }
        btn_updt.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch{
                db.homeworkDao().updateHomework(
                    Homework(homeworkId, et_name.text.toString(), et_group.text.toString(), deskripsi.text.toString() )
                )
                finish()
            }
        }
    }

    fun getIdol(){
        homeworkId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch{
            val homework = db.homeworkDao().getHomeworks(homeworkId)[0]
            et_name.setText(homework.title)
            et_group.setText(homework.mapel)
            deskripsi.setText(homework.desc)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}