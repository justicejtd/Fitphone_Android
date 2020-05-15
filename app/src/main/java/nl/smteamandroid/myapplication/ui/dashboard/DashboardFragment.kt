package nl.smteamandroid.myapplication.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import nl.smteamandroid.myapplication.R
import nl.smteamandroid.myapplication.database.AppDatabase


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var db : AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "database-name"
        )
            .allowMainThreadQueries()
            .build()
//        if(db.sleepHourEventDao().getById(1)!=null){
//            val sh = db.sleepHourEventDao().getById(1).hours
//
//        }

        val barchart: BarChart;
        barchart = root.findViewById(R.id.chart);
        val entries:ArrayList<BarEntry> = ArrayList();
        entries.add(BarEntry(0f, 6f))
        entries.add(BarEntry(1f, 8f))
        entries.add(BarEntry(2f, 9f))
        entries.add(BarEntry(3f, 7f))
        entries.add(BarEntry(4f, 6.5f))
        entries.add(BarEntry(5f, 8f))
        entries.add(BarEntry(6f, 7f))
        val bars = ArrayList<IBarDataSet>()
        val dataset = BarDataSet(entries, "")
        dataset.setColors(Color.BLUE)
        bars.add(dataset)
        val data = BarData(bars);
        data.setBarWidth(0.85f);
        data.setValueTextSize(18f);
        barchart.data = data;
        barchart.setData(data);
        barchart.setTouchEnabled(true);
        barchart.setDragEnabled(true);
        barchart.setScaleEnabled(true);
        barchart.getLegend().isEnabled = false;
        val BarEntryLabels = ArrayList<String>()
        BarEntryLabels.addAll(listOf("MON","TUE", "WED","THU","FRI","SAT","SUN"))
        barchart.getXAxis().setValueFormatter( IndexAxisValueFormatter(BarEntryLabels));
        barchart.getXAxis().textSize = 12f;

        val moods = listOf("Happy","Sad","Tired","Destroyed");
        val suggestionsViews = ArrayList<TextView>();
        val sug1 : TextView
        sug1 = root.findViewById(R.id.suggestionTV1);
        val sug2 : TextView
        sug2 = root.findViewById(R.id.suggestionTV2);
        val sug3 : TextView
        sug3 = root.findViewById(R.id.suggestionTV3);
        val sug4 : TextView
        sug4 = root.findViewById(R.id.suggestionTV4);
        suggestionsViews.addAll(listOf(sug1,sug2,sug3,sug4))
        val imageViewsArray = ArrayList<ImageView>();
        val pic1 : ImageView;
        pic1 = root.findViewById(R.id.imageSE1);
        val pic2 : ImageView;
        pic2 = root.findViewById(R.id.imageSE2)
        val pic3 : ImageView;
        pic3 = root.findViewById(R.id.imageSE3)
        val pic4 : ImageView;
        pic4 = root.findViewById(R.id.imageSE4)
        imageViewsArray.addAll(listOf(pic1,pic2,pic3,pic4))
        val mood = "Tired"
        val imagesHappyArray = ArrayList<Int>();
        imagesHappyArray.addAll(listOf(R.drawable.pic3,R.drawable.pic4,R.drawable.pic5,R.drawable.pic6))
        val imagesSadArray = ArrayList<Int>();
        imagesSadArray.addAll(listOf(R.drawable.pic3,R.drawable.pic4,R.drawable.pic5,R.drawable.pic6))
        val imagesTiredArray = ArrayList<Int>();
        imagesTiredArray.addAll(listOf(R.drawable.picbe1,R.drawable.picbe2,R.drawable.picbe3,R.drawable.picbe4))
        val imagesDestroyedArray = ArrayList<Int>();
        imagesDestroyedArray.addAll(listOf(R.drawable.pic1,R.drawable.pic2,R.drawable.pic5,R.drawable.pic6))
        val suggestionsHappy : ArrayList<String> = ArrayList();
        suggestionsHappy.addAll(listOf("ENJOY YOUR EXISTENCE","HELP OTHERS BE BETTER","PUSH YOURSELF MORE",""))
        val suggestionsSad : ArrayList<String> = ArrayList()
        suggestionsSad.addAll(listOf("GET OUTSIDE","TURN ON SOME TUNES","TAKE A WALK","STRETCH"))
        val suggestionsTired : ArrayList<String> = ArrayList();
        suggestionsTired.addAll(listOf("TAKE A NAP","DRINK SOME WATER","CLEAR YOUR MIND","TAKE A WALK IN THE NATURE"))
        val suggestionsDestroyed : ArrayList<String> = ArrayList();
        suggestionsDestroyed.addAll(listOf("LOOK FOR PROFESSIONAL HELP",""))
        if(mood=="Happy"){
            for(i in 0..3){
                imageViewsArray[i].setImageResource(imagesHappyArray[i])
                suggestionsViews[i].text = suggestionsHappy[i];
            }
        } else if(mood=="Sad"){
            for(i in 0..3){
                imageViewsArray[i].setImageResource(imagesSadArray[i])
                suggestionsViews[i].text = suggestionsSad[i];
            }
        }else if(mood=="Tired"){
        for(i in 0..3){
            imageViewsArray[i].setImageResource(imagesTiredArray[i])
            suggestionsViews[i].text = suggestionsTired[i];
        }
        }else{
            for(i in 0..3){
                imageViewsArray[i].setImageResource(imagesDestroyedArray[i])
                suggestionsViews[i].text = suggestionsDestroyed[i];
            }
        }

        return root
    }
}