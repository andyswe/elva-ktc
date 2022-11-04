package io.github.andyswe.elva.data.reader

import com.github.andy.elva.model.Measurement
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.impl.client.HttpClients
import org.dom4j.Document
import org.dom4j.io.SAXReader
import java.net.URI


private val KTC_UNIT_IP = System.getenv().get("KTC_IP").toString();
private val KTC_PASSWORD = System.getenv().get("KTC_PASSWORD").toString();

/**
 * Created by andreas on 2017-09-05.
 */
class Reader {

    val reader = SAXReader()
    
    var  firstTime : Boolean
    init {
        firstTime = true;
    }

    fun obtain(): Measurement {

        if (KTC_UNIT_IP == null || KTC_PASSWORD == null) {
            System.err.println("Environment variables KTC_IP or KTC_PASSWORD are not set. Can not login to KTC. Exiting...");
            System.exit(-1);
        }

        val httpClient = HttpClients.custom()
            .build()
        val electrical_central_url = "http://$KTC_UNIT_IP/index.htm"
        try {
            val login = RequestBuilder.post()
                .setUri(URI(electrical_central_url))
                .addParameter("passwd", KTC_PASSWORD)
                .addParameter("part", "3")
                .build()
            val loginResponse = httpClient.execute(login)
            val loginStatus = loginResponse.statusLine.statusCode
            if (200 != loginStatus) {
                throw RuntimeException("Login failed to KTC, got status ${loginStatus} from ip  ${KTC_UNIT_IP}")
            }
            if (firstTime) {
                println("Successfully logged in to KTHC at ${KTC_UNIT_IP}");
            }
        } catch (e: Exception) {
            throw RuntimeException("Failed to communicate with KTC central at " + electrical_central_url, e)
        }

        var elDocument: Document? = null
        var vvDocument: Document? = null
        var counter = 0
        while (elDocument == null || vvDocument == null && counter < 10) {

            val document = obtainMb(httpClient)
            val mbName = document.selectSingleNode("/data/DBIpMB1_Txt").stringValue
            when (mbName) {
                "EL-01" -> elDocument = document
                "VV-01" -> vvDocument = document
            }
            counter++
        }
        val data = Data(elDocument, vvDocument!!)
        if (firstTime) {
            println("Successfully parsed a measurement from KTC at ${KTC_UNIT_IP}");
        }
        firstTime = false
        return Parser().parse(data)
    }

    private fun obtainMb(httpClient: HttpClient): Document {


        val pageRequest = RequestBuilder.post()
            .setUri(URI("http://$KTC_UNIT_IP/mb.xml"))
            .addParameter("counter", "2")
            .addHeader("Content-Type", "text/xml")
            .build()

        val page = httpClient.execute(pageRequest)
        return reader.read(page.entity.content)
    }

}
