package cz.utb.meteodenik.data

sealed interface VysledekSite<out T> {
    data class Uspech<out T>(val data: T) : VysledekSite<T>
    data class Selhani(val duvod: DuvodSelhani) : VysledekSite<Nothing>
}

/** Důvody pojmenované z pohledu aplikace, ne knihovny. Texty jsou v strings.xml. */
enum class DuvodSelhani {
    BEZ_PRIPOJENI,       // telefon není na síti nebo neprošlo DNS
    SERVER_NEODPOVIDA,   // odpověď nepřišla v limitu
    PRILIS_MNOHO_DOTAZU, // 429 – překročený limit API
    CHYBA_POZADAVKU,     // 4xx – chyba na naší straně
    CHYBA_SERVERU,       // 5xx – chyba na jejich straně
    NEPLATNA_ODPOVED,    // přišlo něco jiného než očekávaný JSON
    JINA_CHYBA_SITE      // ostatní IOException
}