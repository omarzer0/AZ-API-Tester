package az.zero.instabugtaskaz.utils

import android.text.format.DateFormat
import android.util.Base64
import android.util.Patterns
import org.json.JSONObject
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*

fun String.isValidURL() = this.isNotEmpty() && Patterns.WEB_URL.matcher(this).matches()

fun getDateWithTime(timestamp: Long): String {
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = timestamp * 1000L
    return DateFormat.format("dd-MM-yyyy hh:mm:ss", calendar).toString()
}

fun String.toJson(): JSONObject? {
    return try {
        JSONObject(this)
    } catch (e: Exception) {
        null
    }
}

fun getDataFromJson(json: JSONObject): MutableList<Map<String, String>>? {
    val resultList: MutableList<Map<String, String>>? = null
    val keys = json.keys()
    while (keys.hasNext()) {
        val key = keys.next() ?: return null
        val value = json.getString(key) ?: return null
        resultList?.add(mapOf(key to value))
    }
    return resultList
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

//private lateinit var binding: FragmentResultBinding
//    private var data: RequestWithResponseEntity? = null
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding = FragmentResultBinding.bind(view)
//
//        val serializable = arguments?.getSerializable(REQUEST_WITH_RESPONSE_KEY)
//        data = if (serializable != null) serializable as RequestWithResponseEntity else null
//
//        setDataToViews()
//
//    }
//
//    private fun setDataToViews() {
//        data?.apply {
//            binding.apply {
//
//            }
//        }
//    }
//
//
//    companion object {
//        private const val REQUEST_WITH_RESPONSE_KEY = "requestWithResponseEntity"
//
//        @JvmStatic
//        fun newInstance(requestWithResponseEntity: RequestWithResponseEntity) =
//            ResultFragment().apply {
//                arguments = Bundle().apply {
//                    putSerializable(REQUEST_WITH_RESPONSE_KEY, requestWithResponseEntity)
//                }
//            }
//    }


//    fun openFragment(requestWithResponse: RequestWithResponseEntity) {
//
//        val ft: FragmentTransaction  = childFragmentManager.beginTransaction()
//        val fragment: Fragment = ResultFragment.newInstance(requestWithResponse)
//        ft.replace(R.id.root_frame, fragment)
//        ft.commit()
//    }

