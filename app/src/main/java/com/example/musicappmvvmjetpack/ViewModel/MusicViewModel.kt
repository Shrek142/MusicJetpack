package com.example.musicappmvvmjetpack.ViewModel

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicappmvvmjetpack.Model.Music

class MusicViewModel(private val context: Context) : ViewModel() {

    private val _music = MutableLiveData<List<Music>>()
    val musics: LiveData<List<Music>> = _music

    private var mediaPlayer: MediaPlayer? = null

    var isPlay by mutableStateOf(false)

    var isRandom by mutableStateOf(false)
    //danh sách bài hát yêu thích
    var favoriteSongs = mutableStateListOf<Music>()
        private set

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long> = _currentTime

    private val _totalDuration = MutableLiveData<Long>()
    val totalDuration: LiveData<Long> = _totalDuration

    private val _currentMusic = MutableLiveData<Music?>()
    val currentMusic: LiveData<Music?> = _currentMusic


    private var timer: CountDownTimer? = null

    var currentMusicId: Int? = null

    init {
        _music.value = Music.getMusic()
    }

    fun getMusicById(id: String): Music? {
        return _music.value?.find { it.id == id.toInt() }
    }

    fun getCurrentMusicId(): Int {
        // Giả định lưu ID của bài hát đang phát
        return currentMusicId ?: -1
    }

    fun playNextRandom(){
        val currentIndex = getCurrentMusicId()
        val availableMusic = musics.value?.filter { it.id != currentIndex }

        if (!availableMusic.isNullOrEmpty()){
            val randomMusic = availableMusic.random()
            playMusic(randomMusic.id)
        }else{
            0
        }
    }

    fun nextMusic() {
        val currentIndex = musics.value?.indexOfFirst { it.id == getCurrentMusicId() } ?: -1  //lưu id của bài nhạc hiện tại trong danh sách musics
        val nextIndex = if (currentIndex >= 0 && currentIndex < (musics.value?.size ?: 0) - 1) {
            currentIndex + 1
        } else {
            0 // Quay lại bài đầu tiên nếu đang ở bài cuối
        }
        playMusic(musics.value?.get(nextIndex)?.id ?: 1)
    }

    fun previousMusic() {
        val currentIndex = musics.value?.indexOfFirst { it.id == getCurrentMusicId() } ?: -1
        val previousIndex = if (currentIndex > 0) {
            currentIndex - 1
        } else {
            (musics.value?.size ?: 1) - 1 // Quay lại bài cuối nếu đang ở bài đầu
        }
        playMusic(musics.value?.get(previousIndex)?.id ?: 1)
    }

    fun playMusic(id: Int) {
        mediaPlayer?.release() // Giải phóng tài nguyên nếu có âm thanh đang phát
        val music = getMusicById(id.toString())
        music?.let {
            mediaPlayer = MediaPlayer.create(context, it.song)
            _totalDuration.value = mediaPlayer?.duration?.toLong()  // lưu tổng thời gian

            mediaPlayer?.start() // Phát nhạc

            isPlay = true

            currentMusicId = id // Cập nhật ID bài hát hiện tại
            _currentMusic.value = it
            startTimer(mediaPlayer?.duration?.toLong() ?: 0)

            mediaPlayer?.setOnCompletionListener {
                stopTimer()
                _currentTime.value = 0L
                nextMusic()
            }
        }
    }

    private fun startTimer(duration: Long) {
        _currentTime.value = duration
        timer?.cancel()
        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished
            }

            override fun onFinish() {
                _currentTime.value = 0L
            }
        }.start()
    }

    private fun stopTimer() {
        timer?.cancel()
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
        isPlay = false
        stopTimer()
    }

    fun resumeMusic() {
        mediaPlayer?.start()
        isPlay = true
        val remainingTime = _currentTime.value ?: 0L
        startTimer(remainingTime)
    }

    // Hàm thêm bài hát vào danh sách yêu thích
    fun addFavorite(song: Music) {
        if (!favoriteSongs.contains(song)) {
            favoriteSongs.add(song)
        }
    }

    // Hàm xóa bài hát khỏi danh sách yêu thích
    fun removeFavorite(song: Music) {
        favoriteSongs.remove(song)
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        stopTimer()
    }
}