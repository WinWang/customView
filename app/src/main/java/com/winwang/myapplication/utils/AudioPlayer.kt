package com.winwang.myapplication.utils


import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri

/**
 * Created by WinWang on 2023/6/19
 * Description:音频播放工具类
 **/
object AudioPlayer : MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    private fun initPlayer() {
        // 创建MediaPlayer实例
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setOnPreparedListener(this@AudioPlayer)
                setOnCompletionListener(this@AudioPlayer)
            }
        }

    }

    // 准备播放器
    fun prepare(context: Context, audioUrl: String) {
        initPlayer()
        mediaPlayer?.apply {
            reset()
            setDataSource(context, Uri.parse(audioUrl))
            prepareAsync()
        }
    }


    fun playAssert(context: Context, assertFileName: String, loop: Boolean = false) {
        initPlayer()
        val assetFileDescriptor = context.assets.openFd(assertFileName)
        mediaPlayer?.run {
            reset()
            setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
            prepare()
            start()
            isLooping = loop
        }
    }


    // 播放音频
    fun play() {
        if (!isPlaying) {
            mediaPlayer?.start()
            isPlaying = true
        }
    }

    // 暂停音频
    fun pause() {
        if (isPlaying) {
            mediaPlayer?.pause()
            isPlaying = false
        }
    }

    // 停止音频
    fun stop() {
        mediaPlayer?.apply {
            stop()
            reset()
        }
        isPlaying = false
    }

    // 释放资源
    fun release() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        isPlaying = false
    }

    override fun onPrepared(mp: MediaPlayer) {
        // 准备完成后自动播放
        mp.start()
        isPlaying = true
    }

    override fun onCompletion(mp: MediaPlayer) {
        // 播放完成后重置播放器状态
        reset()
    }

    private fun reset() {
        mediaPlayer?.apply {
            reset()
            setOnPreparedListener(this@AudioPlayer)
            setOnCompletionListener(this@AudioPlayer)
        }
        isPlaying = false
    }
}
