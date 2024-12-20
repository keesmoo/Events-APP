package com.dicoding.eventproject.data.retrofit

import com.dicoding.eventproject.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getFinishedEvents(
        @Query("active") active: Int = 0
    ): Call<EventResponse>
    @GET("events")
    fun getUpcomingEvents(
        @Query("active") active: Int = 1
    ): Call<EventResponse>
    @GET("events")
    fun getEvents(
        @Query("active") active: Int,
        @Query("limit") limit: Int = 5
    ): Call<EventResponse>
}