package com.example.ui.audio

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AudioState(
    val isPlaying: Boolean = false,
    val currentSurahNumber: Int = 1,
    val currentAyahNumber: Int = 1,
    val surahName: String = "Al-Fatihah",
    val reciterName: String = "Mishary Rashid Alafasy",
    val speed: Float = 1.0f,
    val isRepeatEnabled: Boolean = false,
    val sleepTimerMinutes: Int = 0
)

class AudioPlayerManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    private val _audioState = MutableStateFlow(AudioState())
    val audioState: StateFlow<AudioState> = _audioState.asStateFlow()

    val availableReciters = listOf(
        "Mishary Rashid Alafasy",
        "Abdul Rahman Al-Sudais",
        "Saad Al-Ghamdi",
        "Abu Bakr Al-Shatri",
        "Maher Al-Muaiqly"
    )

    fun playAyah(surahNumber: Int, ayahNumber: Int, surahName: String) {
        mediaPlayer?.stop()
        mediaPlayer?.release()

        // Formatted sample online audio URL for EveryAyah stream
        val formattedSurah = String.format("%03d", surahNumber)
        val formattedAyah = String.format("%03d", ayahNumber)
        val audioUrl = "https://everyayah.com/data/Alafasy_128kbps/$formattedSurah$formattedAyah.mp3"

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(context, Uri.parse(audioUrl))
                prepareAsync()
                setOnPreparedListener { mp ->
                    mp.start()
                    _audioState.value = _audioState.value.copy(
                        isPlaying = true,
                        currentSurahNumber = surahNumber,
                        currentAyahNumber = ayahNumber,
                        surahName = surahName
                    )
                }
                setOnCompletionListener {
                    if (_audioState.value.isRepeatEnabled) {
                        playAyah(surahNumber, ayahNumber, surahName)
                    } else {
                        _audioState.value = _audioState.value.copy(isPlaying = false)
                    }
                }
                setOnErrorListener { _, _, _ ->
                    _audioState.value = _audioState.value.copy(isPlaying = false)
                    true
                }
            }
        } catch (e: Exception) {
            _audioState.value = _audioState.value.copy(isPlaying = false)
        }
    }

    fun togglePlayPause() {
        mediaPlayer?.let { mp ->
            if (mp.isPlaying) {
                mp.pause()
                _audioState.value = _audioState.value.copy(isPlaying = false)
            } else {
                mp.start()
                _audioState.value = _audioState.value.copy(isPlaying = true)
            }
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        _audioState.value = _audioState.value.copy(isPlaying = false)
    }

    fun toggleRepeat() {
        _audioState.value = _audioState.value.copy(
            isRepeatEnabled = !_audioState.value.isRepeatEnabled
        )
    }

    fun setReciter(name: String) {
        _audioState.value = _audioState.value.copy(reciterName = name)
    }

    fun setPlaybackSpeed(speed: Float) {
        _audioState.value = _audioState.value.copy(speed = speed)
        mediaPlayer?.let { mp ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                try {
                    mp.playbackParams = mp.playbackParams.setSpeed(speed)
                } catch (_: Exception) {}
            }
        }
    }
}
