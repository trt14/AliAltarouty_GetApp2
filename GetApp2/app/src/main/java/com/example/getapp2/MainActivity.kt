package com.example.getapp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    var List = arrayListOf<String>()
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RVAdapter(List)

        requestApi()

    }
    private fun requestApi() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val okHttpClient = OkHttpClient()
                val request = Request.Builder()
                    .url("https://dojo-recipes.herokuapp.com/people/")
                    .build()
                val response =
                    withContext(Dispatchers.Default) {
                        okHttpClient.newCall(request).execute()
                    }
                if (response != null) {
                    if (response.code == 200) {
                        val jsonArray = JSONArray(response.body!!.string())
                        Log.d("Something", jsonArray.toString())

                        for(index in 0 until jsonArray.length()){

                            val nameObj = jsonArray.getJSONObject(index)
                            val name = nameObj.getString("name")
                            List.add(name)
                        }
                        withContext(Main){
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
            }
        }
    }
}


