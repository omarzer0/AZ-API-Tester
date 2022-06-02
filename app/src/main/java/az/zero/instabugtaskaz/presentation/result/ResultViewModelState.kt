package az.zero.instabugtaskaz.presentation.result

data class ResultViewModelState(
    val queryParamVisible: Boolean = false,
    val requestHeaderVisible: Boolean = false,
    val responseHeaderVisible: Boolean = false,
    val requestBodyVisible: Boolean = false,
    val responseBodyVisible: Boolean = false,
)