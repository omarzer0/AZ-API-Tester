package az.zero.instabugtaskaz.presentation.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import az.zero.instabugtaskaz.data.db.RequestWithResponseEntity
import az.zero.instabugtaskaz.data.network.RequestHandler.RequestType
import az.zero.instabugtaskaz.data.network.RequestHandler.RequestType.GET
import az.zero.instabugtaskaz.data.repository.AppRepository
import az.zero.instabugtaskaz.domain.models.ListMapItem
import az.zero.instabugtaskaz.domain.models.request.RequestData
import az.zero.instabugtaskaz.utils.Event

class HomeViewModel(context: Context) : ViewModel() {
    private val repository = AppRepository(context)

    private val _homeState = MutableLiveData<HomeViewModelState>()
    val homeState: LiveData<HomeViewModelState> = _homeState

    private val _homeEvent = MutableLiveData<Event<HomeViewModelEvents>>()
    val homeEvent: LiveData<Event<HomeViewModelEvents>> = _homeEvent

    init {
        _homeState.value = HomeViewModelState()
    }

    fun networkCall(requestData: RequestData) {
        repository.networkCall(requestData) {
            val requestWithResponse = RequestWithResponseEntity(
                request = requestData,
                response = it,
                timestamp = System.currentTimeMillis()
            )
            repository.saveToDb(requestWithResponse) { }
            _homeEvent.postValue(
                Event(HomeViewModelEvents.NavigateToResult(requestWithResponse))
            )

        }
    }

    fun requestTypeChange(requestType: RequestType) {
        _homeState.value = _homeState.value?.copy(
            requestType = requestType
        )
    }

    fun updateQueriesList(query: Pair<String, ListMapItem>, isAdd: Boolean = true) {
        val updatedQueries = _homeState.value?.copy()?.queries?.toMutableSet() ?: return
        if (isAdd) updatedQueries.add(query)
        else updatedQueries.remove(query)

        _homeState.value = _homeState.value?.copy(
            queries = updatedQueries
        )
    }

    fun updateAllQueriesList(queries: List<Pair<String, ListMapItem>>) {
        val updatedQueries = _homeState.value?.copy()?.queries?.toMutableSet() ?: return
        updatedQueries.clear()
        updatedQueries.addAll(queries)
        _homeState.value = _homeState.value?.copy(queries = updatedQueries)
    }

    fun updateHeadersList(query: Pair<String, ListMapItem>, isAdd: Boolean = true) {
        val updatedHeaders = _homeState.value?.copy()?.headers?.toMutableSet() ?: return
        if (isAdd) updatedHeaders.add(query)
        else updatedHeaders.remove(query)
        _homeState.value = _homeState.value?.copy(headers = updatedHeaders)
    }

    fun updateAllHeadersList(headers: List<Pair<String, ListMapItem>>) {
        val updatedHeaders = _homeState.value?.copy()?.headers?.toMutableSet() ?: return
        updatedHeaders.clear()
        updatedHeaders.addAll(headers)
        _homeState.value = _homeState.value?.copy(headers = updatedHeaders)
    }

    fun getRequestType() = _homeState.value?.requestType?.name ?: GET.name

    fun getQueryParametersViews(): List<String> {
        return _homeState.value?.queries?.map { it.first } ?: emptyList()
    }

    fun getHeadersViews(): List<String> {
        return _homeState.value?.headers?.map { it.first } ?: emptyList()
    }
}