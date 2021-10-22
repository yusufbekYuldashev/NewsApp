package com.yusufbek.newsapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.yusufbek.newsapp.databinding.FragmentArticleBinding
import com.yusufbek.newsapp.databinding.FragmentBreakingNewsBinding
import com.yusufbek.newsapp.ui.NewsActivity
import com.yusufbek.newsapp.ui.viewModels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding

    lateinit var viewModel: NewsViewModel
    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentArticleBinding.inflate(inflater, container, false)

        viewModel = (activity as NewsActivity).viewModel

        val article = args.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url!!)
        }

        binding.fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(requireView(), "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }

        return binding.root
    }

}