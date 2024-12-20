package com.dicoding.eventproject.ui.detail

import FavoriteViewModel
import FavoriteViewModelFactory
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.dicoding.eventproject.R
import com.dicoding.eventproject.database.FavoriteEventEntity
import com.dicoding.eventproject.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: FavoriteViewModel by viewModels { FavoriteViewModelFactory(this) }
    private var isFavorited: Boolean = false
    private var eventId: Int = 0 // ID event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get event data from intent
        val imageLogo = intent.getStringExtra("imageLogo") ?: ""
        val name = intent.getStringExtra("name") ?: ""
        val ownerName = intent.getStringExtra("ownerName") ?: ""
        val beginTime = intent.getStringExtra("beginTime") ?: ""
        val quota = intent.getIntExtra("quota", 0)
        val registrants = intent.getIntExtra("registrants", 0)
        val description = intent.getStringExtra("description") ?: ""
        val link = intent.getStringExtra("link") ?: ""
        eventId = intent.getIntExtra("id", 0) // Ambil ID event

        // Calculate remaining quota
        val remainingQuota = maxOf(0, quota - registrants)

        // Set event details in the views
        Glide.with(this).load(imageLogo).into(binding.eventImage)
        binding.eventName.text = name
        binding.eventOwner.text = ownerName
        binding.eventTime.text = beginTime
        binding.eventQuota.text = "Sisa Kuota: $remainingQuota remaining"
        binding.eventDescription.text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        supportActionBar?.title = name

        binding.openLinkButton.setOnClickListener {
            if (link.isNotEmpty()) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(browserIntent)
            }
        }

        // Check if the event is already favorited
        checkIfFavorited(eventId)

        // Setup favorite button click listener
        binding.btnFavorite.setOnClickListener {
            if (isFavorited) {
                removeFavorite()
            } else {
                addFavorite()
            }
        }
    }

    private fun checkIfFavorited(eventId: Int) {
        viewModel.getFavoriteEventById(eventId).observe(this) { event ->
            if (event != null) {
                isFavorited = true
                binding.btnFavorite.setImageResource(R.drawable.favorite_full) // Ganti dengan ikon penuh
            } else {
                isFavorited = false
                binding.btnFavorite.setImageResource(R.drawable.favorite_border) // Ganti dengan ikon berbingkai
            }
        }
    }

    private fun addFavorite() {
        val remainingQuotaString = binding.eventQuota.text.toString()
        val remainingQuota = remainingQuotaString.substringAfter(": ").trim().split(" ")[0].toInt() // Extract only the number

        // Get the image logo URL directly from the intent
        val imageLogoUrl = intent.getStringExtra("imageLogo") ?: ""

        val favoriteEvent = FavoriteEventEntity(
            id = eventId,
            imageLogo = imageLogoUrl, // Use the correct image URL
            name = binding.eventName.text.toString(),
            ownerName = binding.eventOwner.text.toString(),
            beginTime = binding.eventTime.text.toString(),
            remainingQuota = remainingQuota,
            description = binding.eventDescription.text.toString(),
            link = intent.getStringExtra("link") ?: ""
        )
        viewModel.addFavoriteEvent(favoriteEvent)
        isFavorited = true
        binding.btnFavorite.setImageResource(R.drawable.favorite_full)

    }



    private fun removeFavorite() {
        viewModel.removeFavoriteEvent(eventId)
        isFavorited = false
        binding.btnFavorite.setImageResource(R.drawable.favorite_border)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
