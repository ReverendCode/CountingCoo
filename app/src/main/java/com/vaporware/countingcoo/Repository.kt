package com.vaporware.countingcoo

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kiwimob.firestore.livedata.livedata
import java.text.SimpleDateFormat
import java.util.*

//    Society grows great, when old men plant trees in whose shade they will never sit.
class Repository {
    private val collection = SimpleDateFormat("yyyy-MM-dd").format(Date())
    private val db = FirebaseFirestore.getInstance()
    private val count = db.collection(collection)
        .document("LodgePopulation")
    private val doorEntries = db.collection(collection)
        .document("entries")
    private val doorExits = db.collection(collection)
        .document("exits")

    fun setup() {
        val time = SimpleDateFormat("hh:mm:ss").format(Date())
        Log.d("setup","attempting to set up")
        count.set(hashMapOf("LastLogin" to time), SetOptions.merge())
            .addOnFailureListener {
                Log.d("setup","failure to set count",it)
            }
    }
    fun addLodgeEntry(entryway: String) {
        doorEntries.update(entryway, FieldValue.increment(1))
            .addOnFailureListener {
                Log.d("addLodgeEntry","Attempting to add new category", it)
                doorEntries.set(hashMapOf(entryway to 1.toLong()))
            }
    }
    fun addLodgeExit(entryway: String) {
        doorExits.update(entryway, FieldValue.increment(1))
            .addOnFailureListener {
                Log.d("addLodgeExit","Attempting to add new category", it)
                doorExits.set(hashMapOf(entryway to 1))
            }
    }
    fun addPopulation() {
        count.update("currentPopulation",FieldValue.increment(1))
            .addOnFailureListener {
                Log.d("update","Failure", it)
                count.set(hashMapOf("currentPopulation" to 1))
            }
    }
    fun subtractPopulation() {
        count.update("currentPopulation",FieldValue.increment(-1))
    }
    fun getLodgePopulation(): LiveData<LodgeCount>? {
        return count.livedata(LodgeCount::class.java)
    }
}

data class LodgeCount(val currentPopulation: Int = 0)