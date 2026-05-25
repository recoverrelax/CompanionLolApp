package com.companion.lol.network.serializer

import android.annotation.SuppressLint
import androidx.annotation.Keep
import com.companion.lol.network.dto.ChampionDetailsDto
import com.companion.lol.network.dto.other.ChampionDetailsData
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object ChampionDetailsDtoSerializer : KSerializer<ChampionDetailsDto> {
  @Serializable
  @Keep
  @SuppressLint("UnsafeOptInUsageError")
  class ChampionDetailsDtoSurrogate(val data: HashMap<String, ChampionDetailsData>)

  private val surrogateSerializer = ChampionDetailsDtoSurrogate.serializer()

  override val descriptor: SerialDescriptor =
    SerialDescriptor(
      "com.companion.lol.network.dto.ChampionDetailsDto",
      surrogateSerializer.descriptor,
    )

  override fun deserialize(decoder: Decoder): ChampionDetailsDto {
    val surrogate = decoder.decodeSerializableValue(surrogateSerializer)
    return ChampionDetailsDto(info = surrogate.data.values.first())
  }

  override fun serialize(encoder: Encoder, value: ChampionDetailsDto) = error("Not Supported")
}
