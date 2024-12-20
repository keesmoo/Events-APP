package com.dicoding.eventproject.ui.finished

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
import com.dicoding.eventproject.databinding.FragmentFinishedBinding
import com.dicoding.eventproject.ui.detail.DetailActivity
import com.dicoding.eventproject.utils.Resource

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FinishedViewModel by viewModels {
        FinishedViewModelFactory(EventRepository(ApiConfig.getApiService()))
    }

    private lateinit var adapter: FinishedEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
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
        adapter = FinishedEventsAdapter { event -> navigateToDetail(event) }
        binding.recyclerViewFinished.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FinishedFragment.adapter
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
            is Resource.Loading -> binding.finishedProgressBar.visibility = View.VISIBLE
            is Resource.Success -> {
                binding.finishedProgressBar.visibility = View.GONE
                adapter.submitList(resource.data)
            }
            is Resource.Error -> {
                binding.finishedProgressBar.visibility = View.GONE
                binding.textViewEmpty.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
