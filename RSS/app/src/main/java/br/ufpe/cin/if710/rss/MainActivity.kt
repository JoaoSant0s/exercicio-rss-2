package br.ufpe.cin.if710.rss

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import android.content.Intent
import android.net.Uri
import br.ufpe.cin.if710.rss.R.string.rssfeed

class MainActivity : Activity() {

    //ao fazer envio da resolucao, use este link no seu codigo!
    private val RSS_FEED = "http://leopoldomt.com/if1001/g1brasil.xml"

    //OUTROS LINKS PARA TESTAR...
    //http://rss.cnn.com/rss/edition.rss
    //http://pox.globo.com/rss/g1/brasil/
    //http://pox.globo.com/rss/g1/ciencia-e-saude/
    //http://pox.globo.com/rss/g1/tecnologia/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    override fun onStart() {
        super.onStart()
        gertRessFeedCoroutine();
    }

    private fun gertRessFeedCoroutine() {
        doAsync {
            var feedXML = "";
            try {
                feedXML = getRssFeed(getString(rssfeed))
            } catch (e: IOException) {
                e.printStackTrace()
            }
            uiThread {
                val recyclerView = conteudoRSS;
                recyclerView.adapter = ListAdapter(ParserRSS.parse(feedXML),{ itemData : ItemRSS -> showItemToas(itemData) }, it);

                val layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false);
                recyclerView.layoutManager = layoutManager;
            }
        }
    }

    private fun showItemToas(item : ItemRSS) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
        startActivity(browserIntent)
    }

    @Throws(IOException::class)
    private fun getRssFeed(feed: String): String {
        var `in`: InputStream? = null
        var rssFeed : String
        try {
            val url = URL(feed)
            val conn = url.openConnection() as HttpURLConnection
            `in` = conn.inputStream
            val out = ByteArrayOutputStream()
            val buffer = ByteArray(1024)

            var count = `in`!!.read(buffer)

            while (count != -1) {
                out.write(buffer, 0, count)
                count = `in`.read(buffer)
            }

            val response = out.toByteArray()
            rssFeed = String(response, charset("UTF-8"))
        } finally {
            `in`?.close()
        }
        return rssFeed
    }
}
