package com.flab.deepsleep.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flab.deepsleep.databinding.FragmentBookmarkBinding
import com.flab.deepsleep.ui.adapter.PhotoAdapter
import com.flab.deepsleep.ui.photo.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private val photoRecyclerView: RecyclerView by lazy { binding.bookmarkRecyclerview }
    private val photoAdapter: PhotoAdapter by lazy { PhotoAdapter() }
    private val photoViewModel: PhotoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()

        lifecycleScope.launch {
            photoViewModel.allPhotos.collect { photos ->
                photoAdapter.submitList(photos)
            }
        }
    }

    private fun setRecyclerView() {
        photoRecyclerView.layoutManager = LinearLayoutManager(context)
        photoRecyclerView.adapter = photoAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}