package az.zero.instabugtaskaz.utils

import android.text.format.DateFormat
import android.util.Base64
import android.util.Patterns
import org.json.JSONObject
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*

fun String.isValidURL() = this.isNotEmpty() && Patterns.WEB_URL.matcher(this).matches()

fun String.hasQueryParams() = this.contains("?")

fun getDateWithTime(timestamp: Long): String {
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = timestamp * 1000L
    return DateFormat.format("dd-MM-yyyy hh:mm:ss", calendar).toString()
}

fun readFromStream(inputStream: InputStream?): String {
    val stringBuilder = StringBuilder()
    if (inputStream != null) {
        val inputStreamReader = InputStreamReader(inputStream, StandardCharsets.UTF_8)
        val bufferedReader = BufferedReader(inputStreamReader)
        var oneLine = bufferedReader.readLine()
        while (oneLine != null) {
            stringBuilder.append(oneLine)
            oneLine = bufferedReader.readLine()
        }
    }
    return stringBuilder.toString()
}

fun serializableToString(o: Serializable): String? {
    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeObject(o)
    oos.close()
    return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
}

fun objectFromString(s: String): Any {
    val data: ByteArray = Base64.decode(s, Base64.DEFAULT)
    val ois = ObjectInputStream(
        ByteArrayInputStream(data)
    )
    val o: Any = ois.readObject()
    ois.close()
    return o
}