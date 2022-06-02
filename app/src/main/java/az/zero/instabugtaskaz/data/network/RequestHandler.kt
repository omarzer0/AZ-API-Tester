package az.zero.instabugtaskaz.data.network

import android.net.Uri
import az.zero.instabugtaskaz.domain.models.ListMapItem
import az.zero.instabugtaskaz.domain.models.request.RequestData
import az.zero.instabugtaskaz.domain.models.response.ResponseData
import az.zero.instabugtaskaz.utils.AZExecutors
import az.zero.instabugtaskaz.utils.readFromStream
import java.io.BufferedInputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object RequestHandler {
    fun networkCall(requestData: RequestData, onComplete: (ResponseData) -> Unit) {
        requestData.apply {
            val uri = initURI(link = uri, queryParameters = queryParameters)
            AZExecutors.executeNetworkOP {
                callApi(
                    uri = uri,
                    requestType = requestType,
                    headers = requestHeaders,
                    requestBody = requestBody,
                    onComplete
                )
            }
        }
    }

    private fun initURI(link: String, queryParameters: List<ListMapItem>): String {
        val schemaWithAuthority = link.split("://", ignoreCase = true)
        val schema = schemaWithAuthority[0]
        val authorityWithPaths = schemaWithAuthority[1].split("/", ignoreCase = true)
        val authority = authorityWithPaths[0]
        val paths = authorityWithPaths.toMutableList().apply { removeAt(0) }
        val builder = Uri.Builder()

        return builder.apply {
            scheme(schema)
            authority(authority)
            paths.forEach {
                appendPath(it)
            }
            queryParameters.forEach {
                appendQueryParameter(it.key, it.value)
            }
        }.build().toString()
    }

    private fun callApi(
        uri: String,
        requestType: String,
        headers: List<ListMapItem>,
        requestBody: String,
        onComplete: (ResponseData) -> Unit
    ) {
        val url = URL(uri)
        val httpURLConnection = url.openConnection() as HttpURLConnection
        try {
            httpURLConnection.apply {
                requestMethod = requestType
                connectTimeout = 4000
                readTimeout = 2000
                headers.forEach {
                    setRequestProperty(it.key, it.value)
                }.also {
                    /*
                     * added to send the request body as a JSON not a raw text
                     * */
                    setRequestProperty("Content-Type", "application/json")
                }

                if (requestType == RequestType.POST.name) {
                    val osw = OutputStreamWriter(httpURLConnection.outputStream, "UTF-8")
                    osw.write(requestBody)
                    osw.flush()
                    osw.close()
                }
                connect()

            }
            val responseCode = httpURLConnection.responseCode
            val isSuccess = responseCode in 200..299

            val responseHeaders: List<ListMapItem> = httpURLConnection.headerFields.map {
                ListMapItem(it.key, it.value?.toString())
            }

            onComplete(
                ResponseData(
                    responseCode,
                    isSuccess,
                    if (isSuccess) "" else httpURLConnection.responseMessage,
                    responseHeaders,
                    if (isSuccess) readFromStream(BufferedInputStream(httpURLConnection.inputStream)) else "",
                    requestType,
                    System.currentTimeMillis()
                )
            )
        } catch (e: Exception) {
            val responseHeaders: List<ListMapItem> = httpURLConnection.headerFields.map {
                ListMapItem(it.key, it.value?.toString() ?: "")
            }
            onComplete(
                ResponseData(
                    404,
                    false,
                    "Not Found",
                    responseHeaders,
                    "",
                    requestType,
                    System.currentTimeMillis()
                )
            )
        } finally {
            httpURLConnection.disconnect()
        }
    }

    enum class RequestType {
        GET,
        POST
    }
}

