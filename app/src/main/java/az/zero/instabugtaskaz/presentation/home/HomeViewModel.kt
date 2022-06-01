package az.zero.instabugtaskaz.presentation.home

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import az.zero.instabugtaskaz.data.db.RequestWithResponseEntity
import az.zero.instabugtaskaz.data.network.RequestHandler.RequestType
import az.zero.instabugtaskaz.data.network.RequestHandler.RequestType.GET
import az.zero.instabugtaskaz.data.repository.AppRepository
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
            Log.e("RequestHandler", "onViewCreated: $it")
            val requestWithResponse = RequestWithResponseEntity(
                request = requestData,
                response = it,
                timestamp = System.currentTimeMillis()
            )
            repository.saveToDb(requestWithResponse) { idInserted ->
                Log.e("networkCall", "id inserter $idInserted")
            }
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

    fun updateHeadsList(headersViews: List<View>) {
        _homeState.value = _homeState.value?.copy(
            headers = headersViews
        )
    }

    fun getRequestType() = _homeState.value?.requestType?.name ?: GET.name
}

data class HomeViewModelState(
    val requestType: RequestType = GET,
    val headers: List<View> = listOf(),
)

sealed class HomeViewModelEvents {
    data class NavigateToResult(val requestWithResponseEntity: RequestWithResponseEntity) :
        HomeViewModelEvents()

}

class HomeProviderFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(context) as T
    }
}