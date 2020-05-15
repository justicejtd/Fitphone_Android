package nl.smteamandroid.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.category_used_time_row.view.*
import nl.smteamandroid.myapplication.database.AppEvent
import java.io.Serializable


class CategoryTimeSpendAdapter(categories: List<AppEvent>) :
        RecyclerView.Adapter<CategoryTimeSpendAdapter.MyViewHolder>() {

        var categories: List<AppEvent> = categories
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
            val usage = this.categories[position].totalTimeUsed
            var categoryName = this.categories[position].categoryName

            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
            intent.putExtra("categories", categories as Serializable)
            intent.putExtra("selectedCategory", categoryName)


            var hours = (usage / (1000 * 60 * 60) % 24)

            holder.itemView.setOnClickListener {
                startActivity(holder.itemView.context, intent, null)
            }
            if (categoryName == null) {
                categoryName = "Other"

            }
            holder.itemView.textViewCategoryName.text = categoryName
            holder.itemView.textViewHours.text = (usage / (1000 * 60 * 60) % 24).toString().plus("H ").plus((usage / (1000 * 60) % 60)).plus("M")
            holder.itemView.progressBarCategory.progress = ((100 * hours) / 24).toInt()

        }

        override fun getItemCount(): Int {
            return categories.size
        }
}