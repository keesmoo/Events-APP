package com.dicoding.eventproject.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.eventproject.data.EventRepository
import com.dicoding.eventproject.data.response.EventResponse
import com.dicoding.eventproject.data.response.ListEventsItem
import com.dicoding.eventproject.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel(private val repository: EventRepository) : ViewModel() {

    private val _eventList = MutableLiveData<Resource<List<ListEventsItem>>>()
    val eventList: LiveData<Resource<List<ListEventsItem>>> get() = _eventList

    init {
        fetchUpcomingEvents()
    }

    fun fetchUpcomingEvents() {
        _eventList.value = Resource.Loading()

        repository.getUpcomingEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    _eventList.value = Resource.Success(response.body()?.listEvents ?: emptyList())
                } else {
                    _eventList.value = Resource.Error(response.message())
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _eventList.value = Resource.Error(t.message ?: "An unknown error occurred")
            }
        })
    }
}
