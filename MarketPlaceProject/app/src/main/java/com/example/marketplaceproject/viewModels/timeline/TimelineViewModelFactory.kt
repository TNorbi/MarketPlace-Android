package com.example.marketplaceproject.viewModels.timeline

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.marketplaceproject.repository.Repository

class TimelineViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TimelineViewModel(repository) as T
    }
}