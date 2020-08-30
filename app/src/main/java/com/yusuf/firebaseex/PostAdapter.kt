package com.yusuf.firebaseex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_post.view.*


class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var mData: MutableList<Post> = mutableListOf()


    fun setData(data: MutableList<Post>) {
        this.mData = data
        notifyDataSetChanged()
    }





    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Post) {
            itemView.textViewTitle.text = item.title
            itemView.textViewBody.text = item.body

            itemView.setOnClickListener {
                mListener?.invoke(item.id)
            }

            itemView.setOnLongClickListener {
                mLongListener?.invoke(item,it)
                return@setOnLongClickListener true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mData.count()
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    private var mListener : ((String?) -> Unit )? = null
    fun setOnItemClick(function: (String?) -> Unit) {
        this.mListener = function
    }

    private var mLongListener : ((Post?, View) -> Unit )? = null
    fun setOnItemLongClick(function: (Post?, View) -> Unit) {
        this.mLongListener = function
    }




}