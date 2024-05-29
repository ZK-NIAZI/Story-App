package com.example.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.storyapp.databinding.ActivityStoryBinding
import java.util.Locale

class StoryActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts:TextToSpeech?=null
    private var binding: ActivityStoryBinding?=null
    private var position=0
    private lateinit var storyList:ArrayList<Story>
    private lateinit var speakableText:String
    private var isplaying=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        position=intent.getIntExtra("position",0)
        storyList=Constants.getStoryList()
        tts=TextToSpeech(this,this)
        setStoryView()
        setspeakableText()

        binding?.showstory?.setOnClickListener{
            val v=if (binding?.tvStory?.visibility== View.GONE)
                View.VISIBLE

            else
                View.GONE

            binding?.tvStory?.visibility=v




        }
        binding?.showmoral?.setOnClickListener{
            val v=if (binding?.tvMoral?.visibility== View.GONE) View.VISIBLE else View.GONE
            binding?.tvMoral?.visibility=v
        }
        binding?.btnNext?.setOnClickListener{
            if (position < storyList.size-1)
            {
               onchangestory(1)
            }
            else
            {
                Toast.makeText(this,"No More Stories...",Toast.LENGTH_SHORT).show()
            }
        }
        binding?.btnPrevious?.setOnClickListener{
            if (position > 0)
            {
               onchangestory(-1)
            }
            else
            {
                Toast.makeText(this,"No More Stories...",Toast.LENGTH_SHORT).show()
            }
        }
        binding?.btnPlay?.setOnClickListener{playStory()}
    }
    private fun playStory(){
        if (!isplaying)
        {
            isplaying=true
            speakOut(speakableText)
            binding?.btnPlay?.setImageResource(R.drawable.pause)
        }
        else
        {
            tts?.stop()
            isplaying=false
            binding?.btnPlay?.setImageResource(R.drawable.play)
        }
    }
    private fun onchangestory(offset:Int){
        tts?.stop()
        position+=offset
        isplaying=false
        setStoryView()
        binding?.btnPlay?.setImageResource(R.drawable.play)
        setspeakableText()

    }
    private fun setStoryView()
    {
        val story =storyList[position]
        binding?.storyImage?.setImageResource(story.image2)
        binding?.tvStoryTitle?.setText(story.title)
        binding?.tvStory?.setText(story.story)
        binding?.tvMoral?.setText(story.moral)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onInit(status: Int) {
        if (status==TextToSpeech.SUCCESS){
            val result=tts!!.setLanguage(Locale.ENGLISH)
            if (result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Log.e("TTS","The Specified Language is not Supported!")
            }
        }
        else
        {
            Log.e("TTS","Initialisation Failed")
        }
    }
    private fun speakOut(text :String)
    {

        tts?.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

    private fun setspeakableText()
    {

        speakableText=getString(storyList[position].story)+"Moral Of The Story."+
                getString(storyList[position].moral)
    }
    override fun onDestroy() {
        super.onDestroy()
        if (tts!=null)
        {
            tts?.stop()
            tts?.shutdown()
        }
        binding=null
    }
}