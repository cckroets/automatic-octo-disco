/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.typography

class MainActivity : AppCompatActivity() {

    private val viewModel: TimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp(timerViewModel = viewModel)
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp(timerViewModel: TimerViewModel) {
    val secondsRemaining by timerViewModel.remainingSeconds.observeAsState(0)
    val isPlaying by timerViewModel.isPlaying.observeAsState(initial = false)

    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                TimeUnitPicker(
                    value = secondsRemaining / 60,
                    onIncrease = { timerViewModel.addSeconds(60) },
                    onDecrease = { timerViewModel.minusSeconds(60) }
                )

                TimeUnitPicker(
                    value = secondsRemaining % 60,
                    onIncrease = { timerViewModel.addSeconds(1) },
                    onDecrease = { timerViewModel.minusSeconds(1) }
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(24.dp)
            ) {
                Button(onClick = { timerViewModel.toggleTimer() }) {
                    Text(
                        text = (if (isPlaying) "Pause" else "Start"),
                        Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TimeUnitPicker(
    value: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.wrapContentWidth().defaultMinSize(100.dp)
    ) {
        Button(onClick = onIncrease) {
            Text(text = "+")
        }

        Text(
            modifier = Modifier.padding(24.dp),
            style = typography.h4,
            text = value.toString().padStart(2, '0')
        )

        Button(onClick = onDecrease) {
            Text(text = "-")
        }
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp(TimerViewModel())
    }
}
