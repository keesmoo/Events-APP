package com.dicoding.eventproject.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.eventproject.data.response.EventResponse
import com.dicoding.eventproject.data.response.ListEventsItem
import com.dicoding.eventproject.data.EventRepository
import com.dicoding.eventproject.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val repository: EventRepository) : ViewModel() {

    private val _upcomingEvents = MutableLiveData<Resource<List<ListEventsItem>>>()
    val upcomingEvents: LiveData<Resource<List<ListEventsItem>>> get() = _upcomingEvents

    private val _finishedEvents = MutableLiveData<Resource<List<ListEventsItem>>>()
    val finishedEvents: LiveData<Resource<List<ListEventsItem>>> get() = _finishedEvents

    // Function to fetch upcoming events
    fun fetchUpcomingEvents(active: Int, limit: Int = 5) {
        _upcomingEvents.value = Resource.Loading()

        repository.getEvents(active, limit).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val events = response.body()!!.listEvents.filter { it.quota > it.registrants }
                    _upcomingEvents.value = Resource.Success(events)
                } else {
                    _upcomingEvents.value = Resource.Error(response.message())
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _upcomingEvents.value = Resource.Error(t.message ?: "An unknown error occurred")
            }
        })
    }

    // Function to fetch finished events
    fun fetchFinishedEvents(active: Int, limit: Int = 5) {
        _finishedEvents.value = Resource.Loading()

        repository.getEvents(active, limit).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val events = response.body()!!.listEvents.filter { it.registrants >= it.quota }
                    _finishedEvents.value = Resource.Success(events)
                } else {
                    _finishedEvents.value = Resource.Error(response.message())
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _finishedEvents.value = Resource.Error(t.message ?: "An unknown error occurred")
            }
        })
    }
}
