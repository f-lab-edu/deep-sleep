package com.flab.deepsleep.ui.fragment

import com.flab.deepsleep.ui.adapter.PagingAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flab.deepsleep.databinding.FragmentHomeBinding
import com.flab.deepsleep.ui.activity.DetailsActivity
import com.flab.deepsleep.ui.listener.OnHeartButtonClick
import com.flab.deepsleep.ui.listener.OnPhotoItemClickListener
import com.flab.deepsleep.ui.main.UiItem
import com.flab.deepsleep.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val photoRecyclerView: RecyclerView by lazy { binding.photosRecyclerView }
    private val buttonClick = OnHeartButtonClick { uiItem ->
        isLikedCheck(uiItem)
    }
    private val onPhotoItemClickListener =
        OnPhotoItemClickListener { uiItem ->
            context?.let { DetailsActivity.startActivity(it, uiItem) }
        }
    private val pagingAdapter: PagingAdapter by lazy {
        PagingAdapter(buttonClick, onPhotoItemClickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.items.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }

    private fun setRecyclerView() {
        photoRecyclerView.layoutManager = LinearLayoutManager(context)
        photoRecyclerView.adapter = pagingAdapter
        photoRecyclerView.itemAnimator = null
    }

    private fun isLikedCheck(uiItem: UiItem) {
        if (uiItem.isLike) {
            uiItem.id?.let { homeViewModel.deletePhoto(it) }
        } else {
            homeViewModel.insertPhoto(uiItem)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}