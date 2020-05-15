package nl.smteamandroid.myapplication.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_ranking_screen.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import nl.smteamandroid.myapplication.CustomAdapter
import nl.smteamandroid.myapplication.MainActivity
import nl.smteamandroid.myapplication.R
import nl.smteamandroid.myapplication.database.AppDatabase
import nl.smteamandroid.myapplication.database.UserEvent
import nl.smteamandroid.myapplication.models.User

class NotificationsFragment : Fragment() {

    var users = ArrayList<User>()
    lateinit var db : AppDatabase

    private lateinit var notificationsViewModel: NotificationsViewModel

        override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)

            notificationsViewModel.text.observe(this, Observer {
            textView.text = it

        })

             db = Room.databaseBuilder(
                requireContext(),
                AppDatabase::class.java, "database-name"
            )
                .allowMainThreadQueries()
                .build()

        var userlist = db.userEventDao().getAll()

        for(userEvent in userlist){
            var user = User(userEvent.username!!, userEvent.rank!!)
            users.add(user)

        }

        return root
    }
}