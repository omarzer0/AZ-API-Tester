package az.zero.instabugtaskaz.domain.models.request

import az.zero.instabugtaskaz.domain.models.ListMapItem
import java.io.Serializable

data class RequestData(
    val uri: String,
    val requestHeaders: List<ListMapItem>,
    // This is for later when we add dynamic paths
    val paths: List<String>,
    val requestBody: String,
    val queryParameters: List<ListMapItem>,
    val requestType: String,
    // This is for later when we separate responses and requests
    val executionTime: Long
) : Serializable


/**
 * Request has:
 *      1- request headers
 *      2- request body or request queries parameters
 *      3- request type (GET or POST)
 *      4- execution time
 *      5- Uri
 */