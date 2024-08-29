package com.example.windowsaudioclientapk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.windowsaudioclientapk.ui.theme.WindowsAudioClientAPKTheme

class MainActivity : ComponentActivity() {
    private val audioReceiver = AudioReceiver() // Instantiate AudioReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            WindowsAudioClientAPKTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AudioStreamingScreen(
                        modifier = Modifier.padding(innerPadding),
                        audioReceiver = audioReceiver
                    )
                }
            }
        }
    }
}

@Composable
fun AudioStreamingScreen(
    modifier: Modifier = Modifier,
    audioReceiver: AudioReceiver
) {
    var isReceiving by remember { mutableStateOf(false) }
    var serverIp by remember { mutableStateOf("192.168.31.112") } // Default value
    var serverPort by remember { mutableStateOf("54424") } // Default value

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isReceiving) {
            TextField(
                value = serverIp,
                onValueChange = { serverIp = it },
                label = { Text("Server IP") },
                modifier = Modifier.padding(16.dp)
            )

            TextField(
                value = serverPort,
                onValueChange = { serverPort = it },
                label = { Text("Server Port") },
                modifier = Modifier.padding(16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { /* Handle the done action */ })
            )


            Button(onClick = {
                isReceiving = true
                val configData2 = """
                [screen_sharing]{keyenter}
                videoserverip = $serverIp{keyenter}
                videoserverport = 12345{keyenter}
                peripheralserverip = $serverIp{keyenter}
                peripheralserverport = 12346{keyenter}
                default_target_screen_width = 1920{keyenter}
                default_target_screen_height = 1080{keyenter}
                default_window_width = 800{keyenter}
                default_window_height = 600{keyenter}
                [audio_sharing]{keyenter}
                audioserverip = $serverIp{keyenter}
                audioserverport = 5005{keyenter}
                channels = 2{keyenter}
                rate = 44100{keyenter}
                chunk = 1024{keyenter}
                deviceindex = 0{keyenter}
                deviceautotarget = CABLE Output"""
                audioReceiver.registerWithServer(serverIp, serverPort.toInt(), "")
            }) {
                Text("Start Receiving Audio")
            }
        } else {
            Button(onClick = {
                isReceiving = false
                audioReceiver.stopReceiving()
            }) {
                Text("Stop Receiving Audio")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WindowsAudioClientAPKTheme {
        AudioStreamingScreen(audioReceiver = AudioReceiver())
    }
}
