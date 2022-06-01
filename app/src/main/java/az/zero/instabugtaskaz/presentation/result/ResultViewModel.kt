package az.zero.instabugtaskaz.presentation.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import az.zero.instabugtaskaz.data.db.RequestWithResponseEntity
import az.zero.instabugtaskaz.presentation.result.WhichViewToGoggle.*

class ResultViewModel : ViewModel() {
    private val _resultState = MutableLiveData<ResultViewModelState>()
    val resultState: LiveData<ResultViewModelState> = _resultState

    private var data: RequestWithResponseEntity? = null

    fun passData(passedData: RequestWithResponseEntity?) {
        if (data != null) return
        data = passedData
    }

    fun getData(): RequestWithResponseEntity? = data

    fun toggle(whichViewToGoggle: WhichViewToGoggle) {
        val oldState = _resultState.value?.copy() ?: return
        _resultState.value = when (whichViewToGoggle) {
            QUERY -> oldState.copy(queryParamVisible = !oldState.queryParamVisible)
            REQUEST_HEADER -> oldState.copy(requestHeaderVisible = !oldState.requestHeaderVisible)
            RESPONSE_HEADER -> oldState.copy(responseHeaderVisible = !oldState.responseHeaderVisible)
        }
    }

    init {
        _resultState.value = ResultViewModelState()
    }
}

enum class WhichViewToGoggle {
    QUERY,
    REQUEST_HEADER,
    RESPONSE_HEADER
}

data class ResultViewModelState(
    val queryParamVisible: Boolean = false,
    val requestHeaderVisible: Boolean = false,
    val responseHeaderVisible: Boolean = false,
)