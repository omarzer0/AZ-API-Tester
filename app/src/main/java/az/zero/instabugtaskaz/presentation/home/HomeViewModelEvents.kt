package az.zero.instabugtaskaz.presentation.home

import az.zero.instabugtaskaz.data.db.RequestWithResponseEntity


sealed class HomeViewModelEvents {
    data class NavigateToResult(val requestWithResponseEntity: RequestWithResponseEntity) :
        HomeViewModelEvents()

}