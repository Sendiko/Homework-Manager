package com.example.kotlinroom.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinroom.R
import com.example.kotlinroom.room.Const
import com.example.kotlinroom.room.Homework
import com.example.kotlinroom.room.HomeworkAdapter
import com.example.kotlinroom.room.HomeworkDB
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    // TODO : FOR ROOM DB
    private val db by lazy { HomeworkDB(this) }
    lateinit var homeworkAdapter: HomeworkAdapter

    // TODO : FOR FLOATING ACTION BUTTON
    private val rotateOpen : Animation by lazy{ AnimationUtils.loadAnimation(this, R.anim.rotate_open)}
    private val rotateClose : Animation by lazy{ AnimationUtils.loadAnimation(this, R.anim.rotate_close)}
    private val toBottom : Animation by lazy{ AnimationUtils.loadAnimation(this, R.anim.to_bottom)}
    private val fromBottom: Animation by lazy{ AnimationUtils.loadAnimation(this, R.anim.from_bottom)}
    private var clicked = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        setupListener()
        onAddButtonClicked()
    }

    // TODO FOR FLOATING ACTION BUTTON
    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setCilckable(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked : Boolean) {
        if(clicked){
            btn_add.visibility = View.GONE
            btn_sort2.visibility = View.GONE
        }else {
            btn_add.visibility = View.VISIBLE
            btn_sort2.visibility = View.VISIBLE
        }
    }

    private fun setAnimation(clicked : Boolean) {
        if (clicked){
            btn_add.startAnimation(toBottom)
            btn_sort2.startAnimation(toBottom)
            menu.startAnimation(rotateClose)
        }else {
            btn_add.startAnimation(fromBottom)
            btn_sort2.startAnimation(fromBottom)
            menu.startAnimation(rotateOpen)
        }
    }

    private fun setCilckable(clicked: Boolean){
        if(clicked){
            btn_add.isClickable = false
            btn_sort2.isClickable = false
        }else {
            btn_add.isClickable = true
            btn_sort2.isClickable = true
        }
    }

    // TODO BUAT ROOMDB
    fun loadHomework(){
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

    fun sortData(){
        CoroutineScope(Dispatchers.IO).launch {
            val idols = db.homeworkDao().sortHomework()
            withContext(Dispatchers.Main){
                homeworkAdapter.setData(idols)
            }
        }
    }

    // TODO SETUPLISTENER
    private fun setupListener() {
        menu.setOnClickListener{
            onAddButtonClicked()
        }
        btn_add.setOnClickListener{
            intentEdit(0, Const.TYPE_CREATE)
        }
        btn_sort2.setOnClickListener {
            sortData()
            Toast.makeText(this, "Sorted by name A-Z", Toast.LENGTH_SHORT).show()
        }
//        btn_add.setOnClickListener{
//            intentEdit(0, Const.TYPE_CREATE)
//        }
//        btn_sort.setOnClickListener {
//            sortData()
//            Toast.makeText(this, "Data Sorted A-Z", Toast.LENGTH_SHORT).show()
//        }
}

    // TODO INTENT
    fun intentEdit(intentId : Int, intentType : Int){
        startActivity(
            Intent(applicationContext, AddActivity::class.java)
                .putExtra("intent_id", intentId)
                .putExtra("intent_type", intentType)
        )
    }

    // TODO RECYCLERVIEW
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
        rv_idols.apply{
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = homeworkAdapter
        }
    }

    // TODO DELETE MESSAGE
    private fun deleteDialog(homework: Homework){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Konfirmasi")
            setMessage("Yakin ingin hapus ${homework.title} ?")
            setNegativeButton("Batal" ) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Hapus" ) { dialogInterface, _ ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch{
                    db.homeworkDao().deleteHomework(homework)
                    loadHomework()
                }
            }
        }
        alertDialog.show()
    }
}


