package com.dicoding.eventproject.ui.home

import HomeViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.eventproject.data.EventRepository
import com.dicoding.eventproject.data.response.ListEventsItem
import com.dicoding.eventproject.data.retrofit.ApiConfig
import com.dicoding.eventproject.databinding.FragmentHomeBinding
import com.dicoding.eventproject.ui.detail.DetailActivity
import com.dicoding.eventproject.ui.finished.FinishedEventsAdapter
import com.dicoding.eventproject.ui.upcoming.UpcomingEventsAdapter
import com.dicoding.eventproject.utils.Resource

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(EventRepository(ApiConfig.getApiService()))
    }

    private lateinit var upcomingAdapter: UpcomingEventsAdapter
    private lateinit var finishedAdapter: FinishedEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()

        viewModel.upcomingEvents.observe(viewLifecycleOwner) { resource ->
            handleResource(resource, binding.upcomingProgressBar, upcomingAdapter)
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner) { resource ->
            handleResource(resource, binding.finishedProgressBar, finishedAdapter)
        }

        viewModel.fetchUpcomingEvents(active = 1)
        viewModel.fetchFinishedEvents(active = 0)
    }

    private fun setupRecyclerViews() {
        upcomingAdapter = UpcomingEventsAdapter { event -> navigateToDetail(event) }
        binding.upcomingEventsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = upcomingAdapter
        }

        finishedAdapter = FinishedEventsAdapter { event -> navigateToDetail(event) }
        binding.finishedEventsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = finishedAdapter
        }
    }

    private fun navigateToDetail(event: ListEventsItem) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra("id", event.id)
            putExtra("name", event.name)
            putExtra("imageLogo", event.imageLogo)
            putExtra("ownerName", event.ownerName)
            putExtra("beginTime", event.beginTime)
            putExtra("quota", event.quota)
            putExtra("description", event.description)
            putExtra("link", event.link)
            putExtra("registrants", event.registrants)
        }
        startActivity(intent)
    }

    private fun <T> handleResource(
        resource: Resource<List<T>>,
        progressBar: View,
        adapter: RecyclerView.Adapter<*>
    ) {
        when (resource) {
            is Resource.Loading -> progressBar.visibility = View.VISIBLE
            is Resource.Success -> {
                progressBar.visibility = View.GONE
                @Suppress("UNCHECKED_CAST")
                (adapter as? ListAdapter<T, *>)?.submitList(resource.data)
            }
            is Resource.Error -> {
                progressBar.visibility = View.GONE
                // Menampilkan pesan kesalahan
                Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

