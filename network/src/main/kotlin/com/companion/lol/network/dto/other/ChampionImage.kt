package com.companion.lol.network.dto.other

import android.annotation.SuppressLint
import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
@Suppress("unused")
@SuppressLint("UnsafeOptInUsageError")
class ChampionImage(val full: String, val sprite: String)
