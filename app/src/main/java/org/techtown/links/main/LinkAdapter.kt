package org.techtown.links.main

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.techtown.links.R
import org.techtown.links.data.LinkData

class LinkAdapter(val linkList: ArrayList<LinkData>) :
    RecyclerView.Adapter<LinkAdapter.Viewholder>() {
    class Viewholder(linkView: View) : RecyclerView.ViewHolder(linkView) {
        val title: TextView = linkView.findViewById(R.id.item_title)
        val link: TextView = linkView.findViewById(R.id.item_link)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.link_item, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.title.text = linkList[position].title
        holder.link.text = linkList[position].link
        holder.itemView.setOnClickListener {
            if (linkList[position].link.split('.')[0] == "https://www") {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkList[position].link))
                holder.itemView.context.startActivity(intent)
            } else {
                Toast.makeText(holder.itemView.context, "올바른 url이 아닙니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun getItemCount(): Int {
        return linkList.size
    }
}