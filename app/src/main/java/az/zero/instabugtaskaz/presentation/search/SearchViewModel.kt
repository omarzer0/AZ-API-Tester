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
        }.let { list ->
            if (sortingDesc) list.sortedByDescending { it.timestamp }
            else list.sortedBy { it.timestamp }
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

    fun checkForUpdate() {
        repository.getAllRequestWithResponse {
            AZExecutors.executeMainThread {
                if (dataList.size == it.size) return@executeMainThread
                dataList = it
                filterAndSort()
            }
        }
    }

    init {
        _searchState.value = SearchViewModelState()
        repository.getAllRequestWithResponse {
            /**
             * MutableLivedata.setValue() runs on the main thread so we should run that code on
             * the Main thread or use postValue() with consideration
             * */
            AZExecutors.executeMainThread {
                dataList = it
                _searchState.value = _searchState.value?.copy(requestWithResponses = dataList)
            }
        }
    }
}