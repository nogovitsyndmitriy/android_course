package com.gmail.task_10_mp3player.adapter

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.task_10_mp3player.AudioPlayService
import com.gmail.task_10_mp3player.MainActivity
import com.gmail.task_10_mp3player.R

class MediaAdapter(
    private var list: MutableList<Int>,
    private val mainActivity: MainActivity,
    private var mediaPlayer: MediaPlayer
) :
    RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MediaViewHolder(inflater, parent, list, mainActivity, this)
    }

    override fun getItemCount(): Int = list.size

    fun itemEdited(position: Int, song: Int) {
        list.removeAt(position)
        list.add(position, song)
        notifyItemChanged(position, song)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val song = list[position]
        holder.bind(song)
    }

    class MediaViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        private val list: MutableList<Int>,
        private val mainActivity: MainActivity,
        private val mediaAdapter: MediaAdapter,
    ) : RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.item_song,
            parent,
            false
        )
    ) {

        private val play: ImageButton = itemView.findViewById(R.id.status)
        private val name: TextView = itemView.findViewById(R.id.songName)

        fun bind(song: Int) {
            play.setImageResource(R.drawable.ic_play)
            name.text = mainActivity.resources.getResourceEntryName(song)
            play.setOnClickListener {
                if ((mediaAdapter.mediaPlayer.audioSessionId != list.indexOf(song))) {
                    mainActivity.stopService(Intent(mainActivity, AudioPlayService::class.java))
                    mediaAdapter.itemEdited(mediaAdapter.mediaPlayer.audioSessionId, list[mediaAdapter.mediaPlayer.audioSessionId])
                    mediaAdapter.mediaPlayer = MediaPlayer.create(mainActivity, song, AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build(), list.indexOf(song))
                    mainActivity.startService(Intent(mainActivity, AudioPlayService::class.java).putExtra("song", song).putExtra("songIndex", list.indexOf(song)))
                    play.setImageResource(R.drawable.ic_pause)
                } else {
                    if (mediaAdapter.mediaPlayer.isPlaying) {
                        mediaAdapter.mediaPlayer.pause()
                        play.setImageResource(R.drawable.ic_play)
                    } else {
                        mediaAdapter.mediaPlayer.start()
                        play.setImageResource(R.drawable.ic_pause)
                    }
                }
            }
        }
    }
}
