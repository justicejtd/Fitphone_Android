//tbs time before sleep
//sh sleeping hours
//Sl stress level
//Ps Psychological state
//pr Productivity
//en Energy at waking time
//Pn Need of power nap
import kotlin.math.round 
fun sleepingQuality(Tbs:Double , Sh:Double , SL:Double , Ps:Double , Pr:Double , En:Double , Pn:Double ):Double {
	var finalValue : Double = Tbs*0.7 +  Sh * 0.8 + 10/SL+ 8/Ps + 8/Pr + 6/En +  6/Pn
    return finalValue/5.3
    //this value is on a scale from 0-10 it should be added to the grade of usage time 
    // divide the total by two
    // divide the resault by 2.5
    // and use round() function to get a grade from 1-4
    // to use round you need to import kotlin.math.round 
}

fun main(){
    var a = sleepingQuality(6.0,8.0,1.0,2.0,2.0,1.0,3.0)
    
    println(round(a))
}