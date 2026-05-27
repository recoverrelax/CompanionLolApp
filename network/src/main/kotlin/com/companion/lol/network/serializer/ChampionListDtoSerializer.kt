package com.companion.lol.network.serializer

import android.annotation.SuppressLint
import androidx.annotation.Keep
import com.companion.lol.network.dto.ChampionDto
import com.companion.lol.network.dto.ChampionListDto
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object ChampionListDtoSerializer : KSerializer<ChampionListDto> {
  @Serializable
  @Keep
  @SuppressLint("UnsafeOptInUsageError")
  private class ChampionListDtoSurrogate(val data: Map<String, ChampionDto>)

  private val surrogateSerializer = ChampionListDtoSurrogate.serializer()

  override val descriptor: SerialDescriptor =
    SerialDescriptor(
      "com.companion.lol.network.dto.ChampionListDto",
      surrogateSerializer.descriptor,
    )

  override fun deserialize(decoder: Decoder): ChampionListDto {
    val surrogate = decoder.decodeSerializableValue(surrogateSerializer)
    return ChampionListDto(champions = surrogate.data.values.toList())
  }

  override fun serialize(encoder: Encoder, value: ChampionListDto) = error("Not Supported")
}
