package az.zero.instabugtaskaz.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import az.zero.instabugtaskaz.data.db.DBDao
import az.zero.instabugtaskaz.data.db.RequestWithResponseEntity
import az.zero.instabugtaskaz.data.network.RequestHandler
import az.zero.instabugtaskaz.domain.models.request.RequestData
import az.zero.instabugtaskaz.domain.models.response.ResponseData

class AppRepository private constructor(context: Context) {
    /** Usually we use dagger but NO libraries allowed*/
    private val dbDao = DBDao(context)

    fun networkCall(requestData: RequestData, onComplete: (ResponseData) -> Unit) {
        RequestHandler.networkCall(requestData, onComplete)
    }

    fun saveToDb(requestWithResponseEntity: RequestWithResponseEntity, onComplete: (Long) -> Unit) {
        dbDao.insertAsync(requestWithResponseEntity, onComplete)
    }

    fun getAllRequestWithResponse(onComplete: (List<RequestWithResponseEntity>) -> Unit) =
        dbDao.getAllRequestWithResponsesAsync(onComplete)


    companion object {
        /**
         * Double lock Singleton pattern prevents multiple Repository instances
         * Usually we use dagger but NO libraries allowed
         */
        @Volatile
        private var INSTANCE: AppRepository? = null
        private val lock = Any()
        operator fun invoke(context: Context) = INSTANCE ?: synchronized(lock) {
            INSTANCE ?: AppRepository(context)
        }
    }
}