package com.example.biblioteca.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryService {
    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 10
    ): SearchResponse

    @GET("works/{workId}.json")
    suspend fun getWorkDetail(
        @Path("workId") workId: String
    ): WorkDetailResponse
}