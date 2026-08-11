package com.example.biblioteca.data.remote

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val docs: List<BookDoc>
)

data class BookDoc(
    val key: String,
    val title: String,
    @SerializedName("author_name") val authorName: List<String>?,
    @SerializedName("cover_i") val coverI: Int?,
    @SerializedName("first_publish_year") val firstPublishYear: Int?
) {
    fun getCoverUrl(): String? = coverI?.let { "https://covers.openlibrary.org/b/id/$it-L.jpg" }
    fun getWorkId(): String = key.substringAfterLast("/")
}

data class WorkDetailResponse(
    val description: Any?, // Puede ser String o un objeto con campo "value"
    val title: String,
    @SerializedName("covers") val covers: List<Int>?
) {
    fun getDescriptionText(): String {
        return when (description) {
            is String -> description
            is Map<*, *> -> description["value"]?.toString() ?: ""
            else -> "Sin descripción disponible."
        }
    }
}

data class SimulatedLibraryInfo(
    val esDigital: Boolean,
    val ubicacion: String? = null,
    val copiasTotales: Int = 0,
    val copiasDisponibles: Int = 0
)