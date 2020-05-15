package nl.smteamandroid.myapplication.ui.home

import android.app.AlertDialog
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import nl.smteamandroid.myapplication.CategoryTimeSpendAdapter
import com.example.phoneusage.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import nl.smteamandroid.myapplication.R
import nl.smteamandroid.myapplication.StoreAppEventRunnable
import nl.smteamandroid.myapplication.database.AppDatabase
import nl.smteamandroid.myapplication.database.AppEvent
import nl.smteamandroid.myapplication.database.UnlockEvent
import java.lang.Math.round
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class HomeFragment : Fragment() {

    private var categories: List<AppEvent> = ArrayList()
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mAdapter: CategoryTimeSpendAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var db: AppDatabase
    private lateinit var unlockCount: UnlockEvent
    private val executor: ExecutorService = Executors.newFixedThreadPool(12)
    private var stressLevel : Double = 0.0
    private var productivityLevel : Double = 0.0
    private var wakingUp : Double = 0.0
    private var powerNap : Double = 0.0
    private var mindState : Double = 0.0
    private var currentState : Int = 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        db = Room.databaseBuilder(
                requireContext(),
        AppDatabase::class.java, "database-name"
        )
        .allowMainThreadQueries()
            .build()
        val unlockCount: UnlockEvent? = db.unlockEventDao().getById(1)
        if (unlockCount == null){
            db.unlockEventDao().setUnlockCount(0, 1)
            this.unlockCount = db.unlockEventDao().getById(1)
        }

        this.mRecyclerView = root.findViewById(R.id.categoryListView)
        this.mRecyclerView.apply { setHasFixedSize(true) }
        return root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        askQuestions()
        showUnlockCount()
        progressBarPhoneUsage.progress = 10
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val firstTime = sharedPref.getBoolean("firstTime", true)
        if(firstTime){
            val thread = Thread {
                updateCategories()
                categories = getCategories()
                activity?.runOnUiThread{
                    progressBarCircle.visibility = View.GONE
                    this.mAdapter = CategoryTimeSpendAdapter(categories)
                    this.mRecyclerView.adapter = this.mAdapter
                    this.mRecyclerView.layoutManager = LinearLayoutManager(this.mRecyclerView.context)

                }
                with (sharedPref.edit()) {
                    putBoolean("firstTime", false)
                    apply()
                    showUnlockCount()
                    showTotalUsageTime()
                }
            }
            thread.start()
        }
        else{
            categories = getCategories()
            progressBarCircle.visibility = View.GONE
            this.mAdapter = CategoryTimeSpendAdapter(categories)
            this.mRecyclerView.adapter = this.mAdapter
            this.mRecyclerView.layoutManager = LinearLayoutManager(this.mRecyclerView.context)
            val thread = Thread{
                updateCategories()
                categories = getCategories()
                activity?.runOnUiThread{
                    this.mAdapter.notifyDataSetChanged();
                }
            }
            thread.start()

            showUnlockCount()
            this.showTotalUsageTime()
//            this.mAdapter = CategoryTimeSpendAdapter(categories)
//            this.mRecyclerView.adapter = this.mAdapter
//            this.mRecyclerView.layoutManager = LinearLayoutManager(this.mRecyclerView.context)

        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun updateCategories() {
        //db.appEventDao().deleteAll()
        val dailyEvent: Map<String, UsageStats> = getqueryAndAggregateUsageStats()
        dailyEvent.forEach {
            val packageName: String = it.component1()
            val usage: UsageStats = it.component2()
            val storeAppRunnable: StoreAppEventRunnable =
                StoreAppEventRunnable(packageName, usage, db, requireActivity().packageManager)
            executor.execute(storeAppRunnable)
        }
        executor.shutdown()
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
    }

    private fun getCategories():List<AppEvent>{
        return db.appEventDao().getCategories()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun getqueryAndAggregateUsageStats(): Map<String, UsageStats> {
        val usageStatsManager: UsageStatsManager =
            requireActivity().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val currentDayStart = calendar.timeInMillis
        return usageStatsManager.queryAndAggregateUsageStats(
            currentDayStart,
            System.currentTimeMillis()
        )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onStart() {
        super.onStart()
        this.startAnimation()
        showUnlockCount()
        if (!this.categories.isNullOrEmpty()){
            this.showTotalUsageTime()
        }
    }

    private fun showUnlockCount(){
        if(db.unlockEventDao().getById(1) != null){
            this.unlockCount = db.unlockEventDao().getById(1)
            this.textViewCount.text = getString(R.string.unlock_count).plus(" ").plus(unlockCount.unlockCount)
        }
    }

    private fun showTotalUsageTime() {
        var totalUsageTime: Long = 0
        //db.appEventDao().getTotalUsageTime()
        for (c in this.categories){
            totalUsageTime += c.totalTimeUsed
        }

        var hours = (totalUsageTime / (1000 * 60 * 60) % 24)
        var min = (totalUsageTime / (1000 * 60) % 60)
        this.textViewUsageHours.text = getString(R.string.usage_hours).plus(" ").plus(hours).plus("H ").plus(min).plus("M")
        this.progressBarPhoneUsage.progress = ((100 * hours) / 8).toInt()

        if (hours.toInt() >= 8){
            this.textViewUsageLevel.text = "High"
        }
        else if (hours.toInt() in 4..8){
            this.textViewUsageLevel.text = "Medium"
        }
        else{
            this.textViewUsageLevel.text = "Low"
        }
    }

    private fun startAnimation(){
       var totalUsageTime: Long = 0
        var path = "android.resource://nl.smteamandroid.myapplication/"
        var charType :Int
        //val intent = Intent(requireContext(), SleepingQualityActivity::class.java)
        //startActivity(intent)
        db.appEventDao().getTotalUsageTime()
        for (c in this.categories){
            totalUsageTime += c.totalTimeUsed
        }
        var hours = (totalUsageTime / (1000 * 60 * 60) % 24)
        var min = (totalUsageTime / (1000 * 60) % 60)

        if (stressLevel != 0.0){
            var finalGrade = FinalGrade(1.0, 5.0, stressLevel, mindState, productivityLevel, wakingUp, powerNap, hours, min)
            this.currentState = finalGrade
            when (finalGrade){
                1 -> {charType = R.raw.destroyed; this.textViewStatus.text = getString(R.string.destroyed) }
                2 -> {charType = R.raw.stressed; this.textViewStatus.text = getString(R.string.stressed) }
                3 -> {charType = R.raw.tired; this.textViewStatus.text = getString(R.string.tired) }
                4 -> {charType = R.raw.happy; this.textViewStatus.text = getString(R.string.happy) }
                else -> {
                    charType = R.raw.happy
                    this.textViewStatus.text = getString(R.string.happy)
                }
            }
        }
        else{
            when (this.currentState){
                1 -> {charType = R.raw.destroyed; this.textViewStatus.text = getString(R.string.destroyed) }
                2 -> {charType = R.raw.stressed; this.textViewStatus.text = getString(R.string.stressed) }
                3 -> {charType = R.raw.tired; this.textViewStatus.text = getString(R.string.tired) }
                4 -> {charType = R.raw.happy; this.textViewStatus.text = getString(R.string.happy) }
                else -> {
                    charType = R.raw.happy
                    this.textViewStatus.text = getString(R.string.happy)
                }
            }
        }
        path += charType

        videoViewCharacter.setVideoURI(Uri.parse(path))

        videoViewCharacter.setOnCompletionListener {
            videoViewCharacter.start()
        }
        videoViewCharacter.start()
    }
    //tbs time before sleep
    //sh sleeping hours
    //Sl stress level
    //Ps Psychological state
    //pr Productivity
    //en Energy at waking time
    //Pn Need of power nap

    fun FinalGrade(Tbs:Double , Sh:Double , SL:Double , Ps:Double , Pr:Double , En:Double , Pn:Double , hours:Long , min:Long ):Int {
        //adding the values and multiplying by the weights see sleeping algorithm document
        var sleepingValue : Double = Tbs*0.7 +  Sh * 0.8 + 10/SL+ 8/Ps + 8/Pr + 6/En +  6/Pn
        //changing the grade to a scale 1-10
        sleepingValue = sleepingValue/5.3
        //write the time var as HH.MM
        var time = hours + min/100
        //as descriped in research report 2 hours is average usage time =>
        //2  hours of usage time = 5 on usage quality grade => 10/Hours
        time = 10/time
        //average of both values
        var finalValue = (time + sleepingValue)/2
        //convert to scale from 1-4 (character animations)
        var test: Double = (finalValue / 2.5)
        return test.roundToInt()

    }

    override fun onResume() {
        super.onResume()
        startAnimation()
    }

    private fun askQuestions(){
        if(stressLevel == 0.0){
            questionDialog("Stress level", "How stressed are you? Green is good", 1)
        }
        else if(stressLevel != 0.0 && productivityLevel == 0.0){
            questionDialog("Productivity level", "Rank your productivity? Green is good", 2)
        }
        else if(stressLevel != 0.0 && productivityLevel != 0.0 && wakingUp == 0.0){
            questionDialog("Waking up", "How did u feel when u woke up? Green is good", 3)
        }
        else if(stressLevel != 0.0 && productivityLevel != 0.0 && wakingUp != 0.0 && powerNap == 0.0){
            questionDialog("Power nap", "Do/Did you need a power nap? Green is good", 4)
        }
        else if(stressLevel != 0.0&& productivityLevel != 0.0 && wakingUp != 0.0 && powerNap != 0.0 && mindState == 0.0){
            questionDialog("Mind state", "How is your mind state? Green is good", 5)

        }
    }

    private fun questionDialog(title : String, message : String, answer : Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton("Green") { dialog, which ->
            changeValue(answer, 1.0)
        }

        builder.setNeutralButton("Orange") { dialog, which ->
            changeValue(answer, 2.0)
        }

        builder.setNegativeButton("Red" ) { dialog, which ->
            changeValue(answer, 3.0)
        }

        builder.show()
    }

    private fun changeValue(answer : Int, newValue : Double){
        when (answer) {
            1 ->
                stressLevel = newValue
            2 ->
                productivityLevel = newValue
            3 ->
                wakingUp = newValue
            4 ->
                powerNap = newValue
            5 ->
                mindState = newValue

            else -> {
                throw Exception("Answer has no value to store the data")
            }
        }
        if (mindState != 0.0){
            this.startAnimation()
        }
        askQuestions()
    }

}