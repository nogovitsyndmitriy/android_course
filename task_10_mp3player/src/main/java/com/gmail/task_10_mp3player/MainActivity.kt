package com.gmail.task_10_mp3player

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.task_10_mp3player.adapter.MediaAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var test: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var trackList: MutableList<Int>
    private lateinit var mediaPlayer: MediaPlayer
    private val mediaAdapter by lazy { MediaAdapter(trackList, this, mediaPlayer) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        trackList = mutableListOf(R.raw.eli_m9so_muziki, R.raw.epidemia_krov_elfov, R.raw.epidemia_zvon_monet)
        mediaPlayer = MediaPlayer.create(this, R.raw.eli_m9so_muziki, AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build(), 0)
        recyclerView.apply {
            adapter = mediaAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        }
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
    }
}