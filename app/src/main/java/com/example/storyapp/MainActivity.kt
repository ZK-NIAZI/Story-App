package com.example.storyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val data=Constants.getStoryList()
        setAdabterRecyclerView(data)
    }
    private fun setAdabterRecyclerView(data:ArrayList<Story>)
    {
        val recyclerView=findViewById<RecyclerView>(R.id.rvStoryList)
        recyclerView.layoutManager=LinearLayoutManager(this)
        val adabter=StoryAdabter(data)
        recyclerView.adapter=adabter

        adabter.setOnClickListner(object :StoryAdabter.OnClickListener{
            override fun onClick(position: Int) {
                val intent= Intent(this@MainActivity,StoryActivity::class.java)
                intent.putExtra("position",position)
                startActivity(intent)
            }

        })
    }
}