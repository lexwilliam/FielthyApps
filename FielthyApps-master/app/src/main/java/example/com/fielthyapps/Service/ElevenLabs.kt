package example.com.fielthyapps.Service

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class ElevenLabs(
    private val context: android.content.Context
) {
    private var player: ExoPlayer? = null
    companion object {
        const val TAG = "ElevenLabs"
        const val TIMEOUT  = 6000L
        const val VOICE_ID = "21m00Tcm4TlvDq8ikWAM"
    }

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    useAlternativeNames = true
                    ignoreUnknownKeys = true
                    encodeDefaults = false
                }
            )
        }

        install(HttpTimeout) {
            requestTimeoutMillis = TIMEOUT
            connectTimeoutMillis = TIMEOUT
            socketTimeoutMillis = TIMEOUT
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("Logger Ktor =>", message)
                }
            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.d("HTTP status:", "${response.status.value}")
            }
        }
    }

    fun textToSpeech(text: String) {
        val byteArray = runBlocking {
            val url = "https://api.elevenlabs.io/v1/text-to-speech/$VOICE_ID?output_format=mp3_44100_32"
            Log.i(TAG, "ElevenLabs speak request: $text")
            val response = client.post(url) {
                header("Content-Type", "application/json")
                header("Accept", "audio/mpeg")
                header("xi-api-key", "sk_516ae87c3d58d002ab35a70bae7d299b55e84f4b37024a4e")
                setBody(ElevenLabsRequest(text, "eleven_multilingual_v2"))
            }
            response.body<ByteArray>()
        }
        playMp3(byteArray)
    }

    private fun playMp3(mp3SoundByteArray: ByteArray) {
        try {
            // create temp file that will hold byte array
            val tempMp3 = File.createTempFile("kurchina", "mp3", context.cacheDir)
            tempMp3.deleteOnExit()
            val fos = FileOutputStream(tempMp3)
            fos.write(mp3SoundByteArray)
            fos.close()
            val mediaItem = MediaItem.fromUri(Uri.fromFile(tempMp3))
            player = ExoPlayer.Builder(context).build()
            player!!.setMediaItem(mediaItem)
            player!!.prepare()
            player!!.play()

        } catch (ex: IOException) {
            val s = ex.toString()
            ex.printStackTrace()
        }
    }

    fun stopMp3() {
        if (player != null) {
            player!!.stop()
            player!!.release()
            player = null
        }
    }
}

@Serializable
data class ElevenLabsRequest(
    val text: String,
    @SerialName("model_id")
    val modelId: String,
)

