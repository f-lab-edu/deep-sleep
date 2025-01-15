package com.flab.deepsleep.ui.home

import PhotoAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flab.deepsleep.databinding.FragmentHomeBinding
import com.flab.deepsleep.ui.photo.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val photoViewModel: PhotoViewModel by viewModels()
    val binding: FragmentHomeBinding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val photoRecyclerView: RecyclerView by lazy { binding.photosRecyclerView }
    private val photoAdapter: PhotoAdapter by lazy {
        PhotoAdapter { singlePhoto ->
            photoViewModel.insertPhoto(singlePhoto)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()


        viewLifecycleOwner.lifecycleScope.launch {
            Timber.d("HomeFragment Coroutine 시작됨 (onViewCreated)")  // 로그 추가
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoViewModel.items.collectLatest {
                    photoAdapter.submitData(it)

                    Timber.d("HomeFragment $it")
                }
            }
        }
    }
    private fun setRecyclerView() {
        photoRecyclerView.layoutManager = GridLayoutManager(context, 2)
        photoRecyclerView.adapter = photoAdapter
    }

}