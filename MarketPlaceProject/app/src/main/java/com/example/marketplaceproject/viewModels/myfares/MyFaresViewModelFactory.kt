package com.example.marketplaceproject.viewModels.myfares

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.marketplaceproject.repository.Repository

class MyFaresViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyFaresViewModel(repository) as T
    }
}