package com.elitegadgetlabs.borderhacks2021app.viewModels

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import javax.inject.Named

class SearchViewModel: ViewModel() {


    @Stable
    interface AutoCompleteEntity {
        fun filter(query: String): Boolean
    }

    @Stable
    interface ValueAutoCompleteEntity<T> : AutoCompleteEntity {
        val value: T
    }

    @Stable
    interface AutoCompleteScope<T : AutoCompleteEntity> : AutoCompleteDesignScope {
        var isSearching: Boolean
        fun filter(query: String)
        fun onItemSelected(block: ItemSelected<T> = {})
    }

    @Stable
    interface AutoCompleteDesignScope {
        var boxWidthPercentage: Float
        var shouldWrapContentHeight: Boolean
        var boxMaxHeight: Dp
        var boxBorderStroke: BorderStroke
        var boxShape: Shape
    }

}

private typealias ItemSelected<T> = (T) -> Unit