package com.yusuf.firebaseex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_create.*

private const val POST_ID = "POST_ID"

class CreateActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("post")
    private var postID: String? = null
    private var optionsMenu : Menu ? = null
    private var post :Post? = null
    private var isEditable = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        postID = intent.getStringExtra(POST_ID)

        confUI()

    }

    private fun confUI(){
        optionsMenu?.findItem(R.id.menuSave)?.isVisible =  false
        optionsMenu?.findItem(R.id.menuCancel)?.isVisible =  false
        optionsMenu?.findItem(R.id.menuEdit)?.isVisible =  false
        optionsMenu?.findItem(R.id.menuCreate)?.isVisible = false

        if(postID.isNullOrBlank()){
            isEditable = true
            optionsMenu?.findItem(R.id.menuCreate)?.isVisible = true
            supportActionBar?.title = "Add Note"
        }else {
            if(isEditable){
                optionsMenu?.findItem(R.id.menuCancel)?.isVisible = true
                optionsMenu?.findItem(R.id.menuSave)?.isVisible = true
            }else{
                optionsMenu?.findItem(R.id.menuEdit)?.isVisible = true
            }
            supportActionBar?.title = "Detail Note"
            getPost()
        }



        textViewTitle.isEnabled = isEditable
        textViewBody.isEnabled = isEditable

    }

    private fun getPost(){
        if (post != null) return

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                post = dataSnapshot.child(postID!!).getValue(Post::class.java)!!
                textViewTitle.setText(post?.title)
                textViewBody.setText(post?.body)
            }

            override fun onCancelled(error: DatabaseError) {
                shortToast(error.message)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_create, menu)
        optionsMenu = menu
        confUI()
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuCreate -> {
                val mTitle = textViewTitle.text.toString()
                val mBody = textViewBody.text.toString()
                when {
                    mTitle.isEmpty() -> {
                        shortToast("Title boş olamaz")
                    }
                    mBody.isEmpty() -> {
                        shortToast("Body boş olamaz")
                    }
                    else -> {
                        myRef.push().setValue(Post(null,mTitle, mBody)).continueWith {
                            finish()
                        }
                    }
                }
            }
            R.id.menuSave ->{
                val mTitle = textViewTitle.text.toString()
                val mBody = textViewBody.text.toString()
                when {
                    mTitle.isEmpty() -> {
                        shortToast("Title boş olamaz")
                    }
                    mBody.isEmpty() -> {
                        shortToast("Body boş olamaz")
                    }
                    else -> {

                        myRef.child(postID!!).setValue(Post(null,mTitle, mBody)).continueWith {
                            finish()
                        }
                    }
                }
            }
            R.id.menuEdit->{
                isEditable = true
                confUI()
            }
            R.id.menuCancel->{
                isEditable = false
                confUI()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {
        fun startActivity(context: Context, id: String?) {
            val intent = Intent(context, CreateActivity::class.java)
            intent.putExtra(POST_ID, id)
            context.startActivity(intent)
        }
    }
}