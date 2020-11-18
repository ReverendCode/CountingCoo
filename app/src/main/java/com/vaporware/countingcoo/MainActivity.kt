package com.vaporware.countingcoo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var repo: Repository? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepSpinner()
        repo = Repository().also {
            it.setup()
        }
        val foo = Repository().getLodgePopulation()
        foo?.observe(this, Observer {
            val thingy = it.currentPopulation
            textLodgeCount.text = thingy.toString()
        })
    }
    private fun prepSpinner() {
        val spinner = findViewById<Spinner>(R.id.roomSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.doors,
            android.R.layout.simple_spinner_dropdown_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = it
        }
    }
    fun handleAddPopulation(view: View) {
        Toast.makeText(this,"Plus One!",Toast.LENGTH_SHORT).show()
        repo?.addPopulation()
        repo?.addLodgeEntry(roomSpinner.selectedItem.toString())
    }
    fun handleSubtractPopulation(view: View) {
        Toast.makeText(this,"Minus One!",Toast.LENGTH_SHORT).show()
        repo?.subtractPopulation()
        repo?.addLodgeExit(roomSpinner.selectedItem.toString())
    }
}