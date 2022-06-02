package az.zero.instabugtaskaz.data.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import az.zero.instabugtaskaz.data.db.RequestWithResponseColumns.REQUEST
import az.zero.instabugtaskaz.data.db.RequestWithResponseColumns.RESPONSE
import az.zero.instabugtaskaz.data.db.RequestWithResponseColumns.TIME_STAMP
import az.zero.instabugtaskaz.data.db.RequestWithResponseEntity.Companion.REQUEST_WITH_RESPONSE_TABLE_NAME
import az.zero.instabugtaskaz.domain.models.request.RequestData
import az.zero.instabugtaskaz.domain.models.response.ResponseData
import az.zero.instabugtaskaz.utils.AZExecutors
import az.zero.instabugtaskaz.utils.objectFromString
import az.zero.instabugtaskaz.utils.serializableToString

class DBDao private constructor(context: Context) {
    private val db: SqliteHelper by lazy { SqliteHelper(context) }
    private val writableDb by lazy { db.writableDatabase }
    private val readableDb by lazy { db.readableDatabase }


    companion object {
        /*
         * Double lock Singleton pattern prevents multiple database instances
         * Usually we use dagger but NO libraries allowed
         */
        @Volatile
        private var INSTANCE: DBDao? = null
        private val lock = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(lock) {
            INSTANCE ?: DBDao(context.applicationContext).also {
                INSTANCE = it
            }
        }
    }

    fun insertAsync(
        requestWithResponseEntity: RequestWithResponseEntity,
        onComplete: (Long) -> Unit
    ) {
        AZExecutors.executeDiskIO {
            val idInserted = insert(requestWithResponseEntity)
            onComplete(idInserted)
        }
    }

    fun getAllRequestWithResponsesAsync(onComplete: (List<RequestWithResponseEntity>) -> Unit) {
        AZExecutors.executeDiskIO {
            onComplete(getAllRequestWithResponses())
        }
    }


    private fun insert(requestWithResponseEntity: RequestWithResponseEntity): Long {
        val values = ContentValues().apply {
            put(REQUEST, serializableToString(requestWithResponseEntity.request))
            put(RESPONSE, serializableToString(requestWithResponseEntity.response))
            put(TIME_STAMP, requestWithResponseEntity.timestamp)
        }
        return writableDb.insert(REQUEST_WITH_RESPONSE_TABLE_NAME, null, values)
    }

    @SuppressLint("Range")
    private fun getAllRequestWithResponses(): List<RequestWithResponseEntity> {
        val result = mutableListOf<RequestWithResponseEntity>()
        val cursor: Cursor? = readableDb.query(
            REQUEST_WITH_RESPONSE_TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "$TIME_STAMP DESC"
        )
        cursor?.apply {
            while (moveToNext()) {
                val request = objectFromString(getString(getColumnIndex(REQUEST))) as RequestData
                val response = objectFromString(getString(getColumnIndex(RESPONSE))) as ResponseData
                val timestamp = getLong(getColumnIndex(TIME_STAMP))
                result.add(RequestWithResponseEntity(request, response, timestamp))
            }
        }
        cursor?.close()
        return result
    }
}