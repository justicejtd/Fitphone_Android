package nl.smteamandroid.myapplication

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_details.*
import nl.smteamandroid.myapplication.database.AppDatabase
import nl.smteamandroid.myapplication.database.AppEvent


class DetailsActivity : AppCompatActivity() {

    private lateinit var mAdapter: AppsAdapter
    private lateinit var db : AppDatabase
    private lateinit var categories: List<AppEvent>

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "database-name"
        )
            .allowMainThreadQueries()
            .build()

        val intent = intent
        this.categories = intent.getSerializableExtra("categories") as List<AppEvent>
        val selectedCategory = intent.getStringExtra("selectedCategory")
        //this.recyclerViewApps = this.recyclerViewApps
        if (selectedCategory != null){
            this.mAdapter = AppsAdapter(this.getApps(selectedCategory.toString()))
        }
        else{
            this.mAdapter = AppsAdapter(this.getApps(""))
        }

        this.recyclerViewApps.adapter = this.mAdapter
        this.recyclerViewApps.layoutManager = LinearLayoutManager(this.recyclerViewApps.context)
    }

    private fun getApps(selectedCategory: String): ArrayList<AppEvent>{
        var apps : ArrayList<AppEvent> = ArrayList()
        for (c in this.categories){
            if (c.categoryName == selectedCategory){
                apps.add(c)
            }
            else if (selectedCategory == ""){
                apps.add(c)
            }
        }
        return apps
    }

}
