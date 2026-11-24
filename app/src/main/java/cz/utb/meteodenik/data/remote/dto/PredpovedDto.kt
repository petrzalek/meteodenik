package cz.utb.meteodenik.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PredpovedDto(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val elevation: Double,
    @SerialName("current_units") val jednotky: JednotkyDto,
    val current: AktualneDto
)

@Serializable
data class JednotkyDto(
    @SerialName("temperature_2m") val teplota: String,
    @SerialName("wind_speed_10m") val vitr: String
)

@Serializable
data class AktualneDto(
    val time: String,
    val interval: Int,
    @SerialName("temperature_2m") val teplotaC: Double,
    @SerialName("relative_humidity_2m") val vlhkostProcent: Int,
    @SerialName("weather_code") val kodPocasi: Int,
    @SerialName("wind_speed_10m") val rychlostVetruKmH: Double
)

@Serializable
data class GeoOdpovedDto(
    val results: List<GeoMistoDto> = emptyList()   // klíč CHYBÍ, když se nic nenajde
)

@Serializable
data class GeoMistoDto(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String? = null,
    val admin1: String? = null,        // kraj / region
    val timezone: String? = null
)