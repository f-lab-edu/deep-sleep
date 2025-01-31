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
import com.flab.deepsleep.ui.adapter.PhotoListAdapter
import com.flab.deepsleep.ui.viewmodel.BookmarkViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private val photoRecyclerView: RecyclerView by lazy { binding.bookmarkRecyclerview }
    private val bookmarkViewModel: BookmarkViewModel by activityViewModels()
    private val photoListAdapter: PhotoListAdapter by lazy {
        PhotoListAdapter { photo ->
            photo.id?.let {
                bookmarkViewModel.deletePhoto(it)
            }
        }
    }

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
            bookmarkViewModel.getAllPhotos().collect {
                photoListAdapter.submitList(it)
            }
        }
    }

    private fun setRecyclerView() {
        photoRecyclerView.layoutManager = LinearLayoutManager(context)
        photoRecyclerView.adapter = photoListAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}