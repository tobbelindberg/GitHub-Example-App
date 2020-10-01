package com.github.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.R
import com.github.base.GitHubApplication

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as GitHubApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
