package az.zero.instabugtaskaz.presentation.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import az.zero.instabugtaskaz.data.db.RequestWithResponseEntity
import az.zero.instabugtaskaz.data.network.RequestHandler.RequestType.GET
import az.zero.instabugtaskaz.presentation.result.WhichViewToToggle.*

class ResultViewModel : ViewModel() {
    private val _resultState = MutableLiveData<ResultViewModelState>()
    val resultState: LiveData<ResultViewModelState> = _resultState

    private var data: RequestWithResponseEntity? = null

    fun passData(passedData: RequestWithResponseEntity?) {
        if (data != null) return
        data = passedData
    }

    fun getData(): RequestWithResponseEntity? = data

    fun getRequestType(): String = data?.request?.requestType ?: GET.name

    fun toggle(whichViewToToggle: WhichViewToToggle) {
        val oldState = _resultState.value?.copy() ?: return
        _resultState.value = when (whichViewToToggle) {
            QUERY -> oldState.copy(queryParamVisible = !oldState.queryParamVisible)
            REQUEST_HEADER -> oldState.copy(requestHeaderVisible = !oldState.requestHeaderVisible)
            RESPONSE_HEADER -> oldState.copy(responseHeaderVisible = !oldState.responseHeaderVisible)
            REQUEST_BODY -> oldState.copy(requestBodyVisible = !oldState.requestBodyVisible)
            RESOPNSE_BODY -> oldState.copy(responseBodyVisible = !oldState.responseBodyVisible)
        }
    }

    init {
        _resultState.value = ResultViewModelState()
    }
}