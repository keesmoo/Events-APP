package com.dicoding.eventproject.ui.favorite

import FavoriteViewModel
import FavoriteViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.eventproject.database.FavoriteEventEntity
import com.dicoding.eventproject.databinding.FragmentFavoriteBinding
import com.dicoding.eventproject.ui.detail.DetailActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteViewModel by viewModels {
        FavoriteViewModelFactory(requireContext())
    }

    private lateinit var adapter: FavoriteEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // Observe loading state
        viewModel.loadingState.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.favoriteProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        // Mengamati perubahan dalam daftar event favorit
        viewModel.favoriteEvents.observe(viewLifecycleOwner, Observer { favoriteEvents ->
            if (favoriteEvents.isEmpty()) {
                binding.textViewEmpty.visibility = View.VISIBLE
                binding.recyclerViewFavorite.visibility = View.GONE
            } else {
                binding.textViewEmpty.visibility = View.GONE
                binding.recyclerViewFavorite.visibility = View.VISIBLE
                adapter.submitList(favoriteEvents)
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = FavoriteEventsAdapter { event -> navigateToDetail(event) }
        binding.recyclerViewFavorite.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoriteFragment.adapter
        }
    }

    private fun navigateToDetail(event: FavoriteEventEntity) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra("id", event.id)
            putExtra("name", event.name)
            putExtra("imageLogo", event.imageLogo)
            putExtra("ownerName", event.ownerName)
            putExtra("beginTime", event.beginTime)
            putExtra("quota", event.remainingQuota)
            putExtra("description", event.description)
            putExtra("link", event.link)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
