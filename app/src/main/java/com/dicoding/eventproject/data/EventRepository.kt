package com.dicoding.eventproject.data

import com.dicoding.eventproject.data.response.EventResponse
import com.dicoding.eventproject.data.retrofit.ApiService
import retrofit2.Call

class EventRepository(private val apiService: ApiService) {

    fun getFinishedEvents(): Call<EventResponse> {
        // Mengambil data acara yang sudah selesai
        return apiService.getFinishedEvents()
    }

    fun getUpcomingEvents(): Call<EventResponse> {
        // Mengambil data acara yang akan datang
        return apiService.getUpcomingEvents()
    }

    fun getEvents(active: Int, limit: Int = 5): Call<EventResponse> {
        // Mengambil acara dengan kriteria tertentu
        return apiService.getEvents(active, limit)
    }
}
