package nl.smteamandroid.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nl.smteamandroid.myapplication.database.UserEvent
import nl.smteamandroid.myapplication.models.User

class CustomAdapter(val userList:ArrayList<User>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.txtName?.text = userList[position].name
        var rank = userList[position].rank
        holder?.txtRank?.text = userList[position].rank.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val txtRank = itemView.findViewById<TextView>(R.id.txtRank)
    }

}