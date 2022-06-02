package az.zero.instabugtaskaz.presentation.home

import az.zero.instabugtaskaz.data.network.RequestHandler
import az.zero.instabugtaskaz.domain.models.ListMapItem

data class HomeViewModelState(
    val requestType: RequestHandler.RequestType = RequestHandler.RequestType.GET,
    val headers: Set<Pair<String, ListMapItem>> = mutableSetOf(),
    val queries: Set<Pair<String, ListMapItem>> = mutableSetOf(),
)
