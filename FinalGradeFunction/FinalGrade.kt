
//import kotlin math is crusial to run this function
import kotlin.math.round 

//tbs time before sleep
//sh sleeping hours
//Sl stress level
//Ps Psychological state
//pr Productivity
//en Energy at waking time
//Pn Need of power nap

fun FinalGrade(Tbs:Double , Sh:Double , SL:Double , Ps:Double , Pr:Double , En:Double , Pn:Double , hours:Long , min:Long ):Double {
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
    return round(finalValue/2.5)
    
    
}

fun main(){
    var a = FinalGrade(1.0,8.30,1.0,1.0,1.0,1.0,1.0,1,1)
    
    println(a)
}