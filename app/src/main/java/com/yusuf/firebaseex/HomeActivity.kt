package com.yusuf.firebaseex

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var postAdapter:PostAdapter


    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("post")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val list : MutableList<Post> = mutableListOf()
                val children = dataSnapshot.children
                children.forEach {
                    val item = it.getValue(Post::class.java)!!
                    item.id = it.key
                    list.add(item)
                }
                postAdapter.setData(list)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }
        })

        postAdapter.setOnItemClick{
            CreateActivity.startActivity(this,it)
        }


        postAdapter.setOnItemLongClick{ item , v->
            val popup = PopupMenu(this, v)
            popup.inflate(R.menu.menu_post_popup)
            popup.gravity = Gravity.RIGHT
            popup.setOnMenuItemClickListener {
                when (it.itemId){
                    R.id.menuDelete -> {
                        myRef.child(item?.id!!).removeValue().continueWith {
                            coordinatorLayout.snack("Deleted"){ }
                        }
                    }
                    R.id.menuShare -> {
                        val sharingIntent = Intent(Intent.ACTION_SEND)
                        sharingIntent.type = "text/plain"
                        val shareBody = "${item?.title} / ${item?.body}"
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                        startActivity(Intent.createChooser(sharingIntent, "Share using"))
                    }
                    R.id.menuEdit -> {
                        CreateActivity.startActivity(this,item?.id)
                    }
                }
                return@setOnMenuItemClickListener true
            }
            popup.show()
        }
    }


    private fun init(){
        btnCreate.setOnClickListener(this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            postAdapter = PostAdapter()
            adapter = postAdapter
        }


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnCreate ->{
                CreateActivity.startActivity(this,null)
            }

        }
    }
}
