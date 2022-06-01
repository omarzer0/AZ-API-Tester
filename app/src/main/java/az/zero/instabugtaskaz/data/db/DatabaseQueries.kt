package az.zero.instabugtaskaz.data.db

import android.provider.BaseColumns
import az.zero.instabugtaskaz.data.db.RequestWithResponseColumns.REQUEST
import az.zero.instabugtaskaz.data.db.RequestWithResponseColumns.RESPONSE
import az.zero.instabugtaskaz.data.db.RequestWithResponseColumns.TIME_STAMP
import az.zero.instabugtaskaz.data.db.RequestWithResponseEntity.Companion.REQUEST_WITH_RESPONSE_TABLE_NAME

const val SQL_CREATE_REQUEST_WITH_RESPONSE_ENTITY =
    "CREATE TABLE $REQUEST_WITH_RESPONSE_TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "$REQUEST TEXT," +
            "$RESPONSE TEXT," +
            "$TIME_STAMP BIGINT" +
            ")"

//const val SQL_CREATE_REQUEST_WITH_RESPONSE_ENTITY =
//    "CREATE TABLE $REQUEST_WITH_RESPONSE_TABLE_NAME (" +
//            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
//            "blob BLOB" +
//            ")"

const val SQL_DELETE_REQUEST_WITH_RESPONSE_ENTITY =
    "DROP TABLE IF EXISTS $REQUEST_WITH_RESPONSE_TABLE_NAME"
