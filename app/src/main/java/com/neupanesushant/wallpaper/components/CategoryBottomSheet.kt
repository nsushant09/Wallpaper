package com.neupanesushant.wallpaper.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.neupanesushant.wallpaper.R
import com.neupanesushant.wallpaper.adapter.CategoryDisplayAdapter
import com.neupanesushant.wallpaper.databinding.BottomSheetCategoryBinding
import com.neupanesushant.wallpaper.model.Constants

class CategoryBottomSheet(private val bottomSheetCallback: BottomSheetCallback) :
    BottomSheetDialogFragment() {

    private val onCategoryClick: (String) -> Unit = { category ->
        this.dismissAllowingStateLoss()
        bottomSheetCallback.onClick(category)
    }
    private lateinit var binding: BottomSheetCategoryBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupEventListeners()
        setupObservers()
    }

    private fun setupView() {
        setCategoryRvData(Constants.categoryList)
    }

    private fun setupEventListeners() {

    }

    private fun setupObservers() {

    }

    private fun setCategoryRvData(categoryList: List<String>) {
        binding.categoryRv.layoutManager = LinearLayoutManager(requireContext())
        val adapter =
            CategoryDisplayAdapter(requireContext(), categoryList.sorted(), onCategoryClick)
        binding.categoryRv.adapter = adapter
    }

    companion object {
        fun getInstance(bottomSheetCallback: BottomSheetCallback): CategoryBottomSheet =
            CategoryBottomSheet(bottomSheetCallback)
    }

}