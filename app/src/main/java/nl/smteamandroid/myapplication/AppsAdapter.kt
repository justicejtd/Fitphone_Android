package nl.smteamandroid.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.category_used_time_row.view.*
import nl.smteamandroid.myapplication.database.AppEvent


    class AppsAdapter(categories: List<AppEvent>) :
        RecyclerView.Adapter<AppsAdapter.MyViewHolder>() {

        private var categories: List<AppEvent> = categories
        class MyViewHolder(val linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout){

        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MyViewHolder {
            val linearLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.category_used_time_row, parent, false) as LinearLayout
            return MyViewHolder(linearLayout)
        }


        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val appUsage = this.categories[position].totalTimeUsed
            var appName = this.categories[position].appName
            var hours = (appUsage / (1000 * 60 * 60) % 5)
            if (appName == null) {
                appName = "Other"
            }
            holder.itemView.textViewCategoryName.text = appName
            holder.itemView.textViewHours.text = (appUsage / (1000 * 60 * 60) % 24).toString().plus("H ").plus((appUsage / (1000 * 60) % 60)).plus("M")
            holder.itemView.progressBarCategory.progress = ((100 * hours) / 24).toInt()
        }

        override fun getItemCount(): Int {
            return categories.size
        }


    }
