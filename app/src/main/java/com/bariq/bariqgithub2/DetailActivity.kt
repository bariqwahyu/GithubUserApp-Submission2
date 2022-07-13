package com.bariq.bariqgithub2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bariq.bariqgithub2.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.StringBuilder

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailUserMainViewModel by viewModels<DetailUserMainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title = resources.getString(R.string.app_nameDetail)

        val username = intent.getStringExtra(EXTRA_USER) as String
        detailUserMainViewModel.getUser(username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        binding.viewPager.adapter = sectionsPagerAdapter
        supportActionBar?.elevation = 0f

        detailUserMainViewModel.user.observe(this) { user ->
            setData(user)
        }
        detailUserMainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        detailUserMainViewModel.toastText.observe(this) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://github.com/${binding.tvUsername.text}")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun setData(user: DetailUserResponse) {
        binding.apply {
            tvName.text = user.name
            tvUsername.text = user.login
            tvRepo.text = (StringBuilder("${user.publicRepos} Repository"))
            tvCompany.text = user.company
            tvLocation.text = user.location
        }
        Glide.with(this)
            .load(user.avatarUrl)
            .circleCrop()
            .into(binding.imgProfile)
        val countFollow = arrayOf(user.followers, user.following)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position], countFollow[position])
        }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }
}