package com.companion.lol.network.dto

import android.annotation.SuppressLint
import androidx.annotation.Keep
import com.companion.lol.network.dto.other.ChampionDetailsData
import com.companion.lol.network.serializer.ChampionDetailsDtoSerializer
import kotlinx.serialization.Serializable

@Serializable(ChampionDetailsDtoSerializer::class)
@Keep
@SuppressLint("UnsafeOptInUsageError")
class ChampionDetailsDto(val info: ChampionDetailsData)
