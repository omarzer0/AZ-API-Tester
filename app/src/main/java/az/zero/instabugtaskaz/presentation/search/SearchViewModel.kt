package az.zero.instabugtaskaz.presentation.search

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import az.zero.instabugtaskaz.data.db.RequestWithResponseEntity
import az.zero.instabugtaskaz.data.repository.AppRepository
import az.zero.instabugtaskaz.presentation.search.TypeToShow.*
import az.zero.instabugtaskaz.utils.AZExecutors

class SearchViewModel(context: Context) : ViewModel() {
    private val repository = AppRepository(context)
    private var dataList: List<RequestWithResponseEntity> = emptyList()

    private val _searchState = MutableLiveData<SearchViewModelState>()
    val searchState: LiveData<SearchViewModelState> = _searchState


    private fun filterAndSort() {
        val sortingDesc = _searchState.value?.sortDesc ?: true
        val typeToShow = _searchState.value?.typeToShow ?: ALL

        val requestWithResponses = when (typeToShow) {
            ALL -> dataList
            GET -> dataList.filter { it.request.requestType == GET.name }
            POST -> dataList.filter { it.request.requestType == POST.name }
        }.also { list ->
            if (sortingDesc) list.sortedByDescending { it.timestamp }
            else dataList.sortedBy { it.timestamp }
        }
        _searchState.value = _searchState.value?.copy(
            typeToShow = typeToShow,
            requestWithResponses = requestWithResponses
        )
    }

    fun updateTypeToShow(typeToShow: TypeToShow) {
        _searchState.value = _searchState.value?.copy(typeToShow = typeToShow)
        filterAndSort()
    }

    fun updateSortBy(sortDesc: Boolean) {
        _searchState.value = _searchState.value?.copy(sortDesc = sortDesc)
        filterAndSort()
    }

    init {
        _searchState.value = SearchViewModelState()
        repository.getAllRequestWithResponse {
            AZExecutors.executeMainThread {
                val dataList = it
                _searchState.value = _searchState.value?.copy(requestWithResponses = dataList)
            }
        }
    }
}

data class SearchViewModelState(
    val typeToShow: TypeToShow = ALL,
    val sortDesc: Boolean = true,
    val requestWithResponses: List<RequestWithResponseEntity> = emptyList()
)

enum class TypeToShow {
    ALL,
    GET,
    POST
}


class SearchProviderFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(context) as T
    }
}
