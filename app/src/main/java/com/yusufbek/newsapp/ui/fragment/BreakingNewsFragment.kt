package com.yusufbek.newsapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.yusufbek.newsapp.R
import com.yusufbek.newsapp.databinding.FragmentBreakingNewsBinding
import com.yusufbek.newsapp.ui.adapters.NewsAdapter
import com.yusufbek.newsapp.ui.other.Constants.QUERY_PAGE_SIZE
import com.yusufbek.newsapp.ui.other.Resource
import com.yusufbek.newsapp.ui.viewModels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreakingNewsFragment : Fragment() {

    private lateinit var binding: FragmentBreakingNewsBinding

    val viewModel: NewsViewModel by viewModels()

    private val newsAdapter = NewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }

        viewModel.breakingNewsResult.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.paginationProgressBar.visibility = View.VISIBLE
                    isLoading = true
                }
                is Resource.Success -> {
                    binding.paginationProgressBar.visibility = View.INVISIBLE
                    newsAdapter.submitList(response.data!!.articles.toList())
                    isLoading = false

                    val totalPages = response.data.totalResults / QUERY_PAGE_SIZE + 2
                    isLastPage = viewModel.breakingNewsPage == totalPages
                    if (isLastPage) {
                        binding.rvBreakingNews.setPadding(0, 0, 0, 0)
                    }
                }
                is Resource.Error -> {
                    binding.paginationProgressBar.visibility = View.INVISIBLE
                    isLoading = false
                    Snackbar.make(
                        requireView(),
                        "There was an error ${response.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return binding.root
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getBreakingNews("us")
                isScrolling = false
            }
        }
    }
}