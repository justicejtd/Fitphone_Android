package nl.smteamandroid.myapplication

import android.os.AsyncTask
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.lang.Exception

class GetCategoryASync : AsyncTask<String, Void, String?>() {
    val GOOGLE_URL = "https://play.google.com/store/apps/details?id="

    override fun doInBackground(vararg params: String?): String? {
        try{
            var doc : Document = Jsoup.connect(GOOGLE_URL + params.get(0).toString()).get()
            var link : Element = doc.select("[itemprop=genre]").first()
            return link.html()
        }
        catch (e : Exception){
            return null
        }
    }

}