package az.zero.instabugtaskaz.presentation.search

import az.zero.instabugtaskaz.data.db.RequestWithResponseEntity

data class SearchViewModelState(
    val typeToShow: TypeToShow = TypeToShow.ALL,
    val sortDesc: Boolean = true,
    val requestWithResponses: List<RequestWithResponseEntity> = emptyList()
)
