package com.companion.lol.network

object EndPoints {
  object DDragon {
    private const val VERSION = "16.10.1"
    private const val BASE_ENDPOINT = "https://ddragon.leagueoflegends.com/cdn"
    private const val BASE_ENDPOINT_WITH_VERSION = "${BASE_ENDPOINT}/${VERSION}"
    const val BASE = "${BASE_ENDPOINT_WITH_VERSION}/data/en_US/"

    fun championSquareAsset(imageName: String) =
      "${BASE_ENDPOINT_WITH_VERSION}/img/champion/${imageName}"

    fun championSkinAsset(championName: String, skinNumber: Int) =
      "${BASE_ENDPOINT}/img/champion/splash/${championName}_${skinNumber}.jpg"
  }
}
