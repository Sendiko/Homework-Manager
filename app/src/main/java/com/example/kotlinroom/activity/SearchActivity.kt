package com.example.kotlinroom.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinroom.R
import com.example.kotlinroom.room.Const
import com.example.kotlinroom.room.Homework
import com.example.kotlinroom.room.HomeworkAdapter
import com.example.kotlinroom.room.HomeworkDB
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchActivity : AppCompatActivity() {
    private val db by lazy { HomeworkDB(this) }
    lateinit var homeworkAdapter: HomeworkAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        setupRecyclerView()
    }



    private fun loadHomework(){
        CoroutineScope(Dispatchers.IO).launch {
            val homework = db.homeworkDao().getHomework()
//            Log.d("MainActivity", "dbresponse: $idols")
            withContext(Dispatchers.Main){
                homeworkAdapter.setData(homework)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadHomework()
    }

    private fun deleteDialog(homework: Homework){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Konfirmasi")
            setMessage("Yakin ${homework.title} sudah selesai ?")
            setNegativeButton("Batal" ) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Sudah" ) { dialogInterface, _ ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch{
                    db.homeworkDao().deleteHomework(homework)
                    loadHomework()
                }
            }
        }
        alertDialog.show()
    }

    fun intentEdit(intentId : Int, intentType : Int){
        startActivity(
            Intent(applicationContext, AddActivity::class.java)
                .putExtra("intent_id", intentId)
                .putExtra("intent_type", intentType)
        )
    }

    private fun setupRecyclerView() {
        homeworkAdapter = HomeworkAdapter(arrayListOf(), object :  HomeworkAdapter.OnAdapterListener{
            override fun onViewListener(homework: Homework) {
                intentEdit(homework.id, Const.TYPE_READ)
            }

            override fun onUpdateListener(homework: Homework) {
                intentEdit(homework.id, Const.TYPE_UPDATE)
            }

            override fun onDeleteListener(homework: Homework) {
                deleteDialog(homework)
            }
        })
        rv_homeworks.apply{
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = homeworkAdapter
        }
    }
}