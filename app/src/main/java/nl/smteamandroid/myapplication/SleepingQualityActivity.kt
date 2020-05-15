package nl.smteamandroid.myapplication

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MotionEvent

class SleepingQualityActivity : AppCompatActivity() {

    private var answer1 : Double = 0.0
    private var answer2 : Double = 0.0
    private var answer3 : Double = 0.0
    private var answer4 : Double = 0.0
    private var answer5 : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleeping_quality)
        askQuestions()
    }

    private fun askQuestions(){
        if(answer1 == 0.0){
            questionDialog("Stress level", "How stressed are you? Green is good", 1)
        }
        else if(answer1 != 0.0 && answer2 == 0.0){
            questionDialog("Productivity level", "Rank your productivity? Green is good", 2)
        }
        else if(answer1 != 0.0 && answer2 != 0.0 && answer3 == 0.0){
            questionDialog("Waking up", "How did u feel when u woke up? Green is good", 3)
        }
        else if(answer1 != 0.0 && answer2 != 0.0 && answer3 != 0.0 && answer4 == 0.0){
            questionDialog("Power nap", "Do/Did you need a power nap? Green is good", 4)
        }
        else if(answer1 != 0.0&& answer2 != 0.0 && answer3 != 0.0 && answer4 != 0.0 && answer5 == 0.0){
            questionDialog("Mind state", "How is your mind state? Green is good", 5)
        }
    }

    private fun questionDialog(title : String, message : String, answer : Int) {
        val builder = AlertDialog.Builder(this)
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
                answer1 = newValue
            2 ->
                answer2 = newValue
            3 ->
                answer3 = newValue
            4 ->
                answer4 = newValue
            5 ->
                answer5 = newValue

            else -> {
                throw Exception("Answer has no value to store the data")
            }
        }
        askQuestions()
    }

}
