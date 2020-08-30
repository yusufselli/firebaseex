package com.yusuf.firebaseex

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnCrashTest.setOnClickListener {
            throw  RuntimeException("Test Crash")
        }

        btnEvent.setOnClickListener {
            val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
            val params = Bundle()
            firebaseAnalytics.logEvent("eventButtonClick", params)
        }

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("ad")

        myRef.setValue(Post("Başlık", "Gövde :"))



        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(Post::class.java)!!
                Log.d(TAG, "Value is: $value")

                textViewName.text = "Başlık : ${value.title} Body : ${value.body} "
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
}
