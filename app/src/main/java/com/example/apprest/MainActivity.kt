package com.example.apprest

import android.net.http.HttpResponseCache
import android.os.AsyncTask
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Begin network code execution
        AsyncTask.execute{
            //GET Request
            val githubEndpoint = URL("https://api.github.com/")
            val myConnection: HttpsURLConnection = githubEndpoint.openConnection() as HttpsURLConnection
            myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1")
            myConnection.setRequestProperty("Accept",
                "application/vnd.github.v3+json")
            myConnection.setRequestProperty("Contact-Me",
                "hathibelagal@example.com")
            if (myConnection.getResponseCode() == 200) {
                val responseBody: InputStream = myConnection.inputStream
                val responseBodyReader = InputStreamReader(responseBody, "UTF-8")
                val jsonReader = JsonReader(responseBodyReader)
                jsonReader.beginObject() // Start processing the JSON object
                while (jsonReader.hasNext()) { // Loop through all keys
                    val key = jsonReader.nextName() // Fetch the next key
                    if (key == "code_search_url") { // Check if desired key
                        // Fetch the value as a String
                        val value = jsonReader.nextString()

                        // Do something with the value
                        Log.d("Value code_search_url: ", value)
                        break // Break out of the loop
                    } else {
                        jsonReader.skipValue() // Skip values of other keys
                    }
                }
                jsonReader.close()
                myConnection.disconnect()
            } else {
                throw Exception("Error: No GET response")
            }
            //POST Request
            val httpbinEndpoint = URL("https://httpbin.org/post")
            val myConnectionPOST = httpbinEndpoint.openConnection() as HttpsURLConnection
            myConnectionPOST.requestMethod = "POST"
            // Create the data
            val myData = "message=Hello"
            // Enable writing
            myConnectionPOST.doOutput = true
            // Write the data
            myConnectionPOST.outputStream.write(myData.toByteArray())
            val myCache = HttpResponseCache.install(
                cacheDir, 100000L
            )
            if (myCache.getHitCount() > 0) {
                Log.d("Cache status:", "The cache is working")
            }
            if (myConnectionPOST.getResponseCode() == 200) {
                val responseBody: InputStream = myConnectionPOST.inputStream
                val responseBodyReader = InputStreamReader(responseBody, "UTF-8")
                val jsonReader = JsonReader(responseBodyReader)
                jsonReader.beginObject() // Start processing the JSON object
                while (jsonReader.hasNext()) { // Loop through all keys
                    val key = jsonReader.nextName() // Fetch the next key
                    val value = jsonReader.nextString()
                    // Do something with the value
                    Log.d("Value: ", value)
                }
                jsonReader.close()
                myConnectionPOST.disconnect()
            } else {
                throw Exception("Error: No POST response")
            }
        }
    }
}