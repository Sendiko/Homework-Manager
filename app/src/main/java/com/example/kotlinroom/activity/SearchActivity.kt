package com.example.kotlinroom.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinroom.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        textView.setText("Bitch Get Out the Way")
    }
}