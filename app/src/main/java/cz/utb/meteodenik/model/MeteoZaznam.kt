package cz.utb.meteodenik.model

import java.time.LocalDateTime

/** kodPocasi: Int → stav: StavPocasi, time: String → cas: LocalDateTime */
data class MeteoZaznam(
    val cas: LocalDateTime,
    val teplotaC: Double,
    val vlhkostProcent: Int,
    val rychlostVetruKmH: Double,
    val stav: StavPocasi
) {
    /** Doménová logika patří sem, ne do UI a ne do ViewModelu. */
    val jePodNulou: Boolean get() = teplotaC < 0.0
    val jeVetrno: Boolean get() = rychlostVetruKmH >= 35.0
}

enum class StavPocasi {
    JASNO, POLOJASNO, ZATAZENO, MLHA, MRHOLENI,
    DEST, SNEZENI, PREHANKY, BOURKA, NEZNAMY
}