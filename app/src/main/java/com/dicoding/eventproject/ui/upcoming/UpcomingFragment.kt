package com.dicoding.eventproject.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.eventproject.data.EventRepository
import com.dicoding.eventproject.data.response.ListEventsItem
import com.dicoding.eventproject.data.retrofit.ApiConfig
import com.dicoding.eventproject.databinding.FragmentUpcomingBinding
import com.dicoding.eventproject.ui.detail.DetailActivity
import com.dicoding.eventproject.utils.Resource

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UpcomingViewModel by viewModels {
        UpcomingViewModelFactory(EventRepository(ApiConfig.getApiService()))
    }


    private lateinit var adapter: UpcomingEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.eventList.observe(viewLifecycleOwner) { resource ->
            handleResource(resource)
        }
    }

    private fun setupRecyclerView() {
        adapter = UpcomingEventsAdapter { event -> navigateToDetail(event) }
        binding.recyclerViewUpcoming.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@UpcomingFragment.adapter
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

    private fun handleResource(resource: Resource<List<ListEventsItem>>) {
        when (resource) {
            is Resource.Loading -> binding.upcomingProgressBar.visibility = View.VISIBLE
            is Resource.Success -> {
                binding.upcomingProgressBar.visibility = View.GONE
                adapter.submitList(resource.data)
            }
            is Resource.Error -> {
                binding.upcomingProgressBar.visibility = View.GONE
                binding.textViewEmpty.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
