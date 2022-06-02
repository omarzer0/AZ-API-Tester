package az.zero.instabugtaskaz.data.db

import android.provider.BaseColumns
import android.util.Log
import az.zero.instabugtaskaz.data.db.RequestWithResponseColumns.REQUEST
import az.zero.instabugtaskaz.data.db.RequestWithResponseColumns.RESPONSE
import az.zero.instabugtaskaz.data.db.RequestWithResponseColumns.TIME_STAMP
import az.zero.instabugtaskaz.domain.models.request.RequestData
import az.zero.instabugtaskaz.domain.models.response.ResponseData
import org.json.JSONObject
import java.io.Serializable

data class RequestWithResponseEntity(
    val request: RequestData,
    val response: ResponseData,
    val timestamp: Long
) : Serializable {
    companion object {
        const val REQUEST_WITH_RESPONSE_TABLE_NAME = "RequestWithResponseEntity"
    }
}

object RequestWithResponseColumns : BaseColumns {
    const val REQUEST = "request"
    const val RESPONSE = "response"
    const val TIME_STAMP = "timestamp"
}

