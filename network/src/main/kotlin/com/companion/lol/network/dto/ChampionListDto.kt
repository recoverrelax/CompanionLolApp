package com.companion.lol.network.dto

import androidx.annotation.Keep
import com.companion.lol.network.serializer.ChampionListDtoSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ChampionListDtoSerializer::class)
@Keep
class ChampionListDto(val champions: List<ChampionDto>)
