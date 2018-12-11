package com.github.leonardoz.kanbapp

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.leonardoz.kanbapp.data.dao.BoardsDao
import com.github.leonardoz.kanbapp.data.entity.Board
import com.github.leonardoz.kanbapp.di.AppComponent
import com.github.leonardoz.kanbapp.di.DaggerAppComponent
import com.github.leonardoz.kanbapp.di.RoomModule
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_boards.*
import javax.inject.Inject

class BoardsActivity : AppCompatActivity() {

    @Inject
    lateinit var boardsDao: BoardsDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boards)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            // TO- DO
        }
        (application as KanbappApplication)
            .appComponent.injectActivity(this)
        boardsDao.getAllBoards().observe(this, Observer { Log.d("Wololo", it.toString()) })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_boards, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
