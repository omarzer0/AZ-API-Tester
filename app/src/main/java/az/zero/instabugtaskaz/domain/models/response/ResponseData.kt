package az.zero.instabugtaskaz.domain.models.response

import az.zero.instabugtaskaz.domain.models.ListMapItem
import java.io.Serializable

data class ResponseData(
    val responseCode: Int,
    val success:Boolean,
    val error: String = "",
    val responseHeaders: List<ListMapItem>,
    val responseBody: String,
    val requestType: String,
    val executionTime: Long
):Serializable

/**
 * Response has:
 *      1- response code
 *      2- error if any
 *      3- response headers
 *      4- response body
 *      5- request type (GET or POST)
 *      6- execution time
 */

