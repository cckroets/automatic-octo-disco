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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Timer
import java.util.concurrent.TimeUnit
import kotlin.concurrent.fixedRateTimer

class TimerViewModel : ViewModel() {

    private var _isPlaying = MutableLiveData(false)
    private var _remainingSeconds = MutableLiveData(0)
    private var timer: Timer? = null

    fun toggleTimer() {
        _isPlaying.value = !_isPlaying.value!!
        if (_isPlaying.value!!) {
            timer = fixedRateTimer(period = TimeUnit.SECONDS.toMillis(1)) {
                val current = _remainingSeconds.value!!
                if (current <= 0) {
                    cancel()
                    _isPlaying.postValue(false)
                    this@TimerViewModel.timer = null
                } else {
                    _remainingSeconds.postValue(current - 1)
                }
            }
        } else {
            timer?.cancel()
            timer = null
        }
    }

    fun addSeconds(seconds: Int) {
        _remainingSeconds.value = _remainingSeconds.value!! + seconds
    }

    fun minusSeconds(seconds: Int) {
        _remainingSeconds.value = (_remainingSeconds.value!! - seconds).coerceAtLeast(0)
    }

    val isPlaying: LiveData<Boolean> = _isPlaying
    val remainingSeconds: LiveData<Int> = _remainingSeconds
}
