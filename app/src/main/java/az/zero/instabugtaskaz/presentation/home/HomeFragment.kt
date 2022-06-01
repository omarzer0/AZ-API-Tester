package az.zero.instabugtaskaz.presentation.home

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import az.zero.instabugtaskaz.R
import az.zero.instabugtaskaz.data.network.RequestHandler.RequestType.GET
import az.zero.instabugtaskaz.data.network.RequestHandler.RequestType.POST
import az.zero.instabugtaskaz.domain.models.ListMapItem
import az.zero.instabugtaskaz.domain.models.request.RequestData
import az.zero.instabugtaskaz.presentation.result.ResultActivity
import az.zero.instabugtaskaz.presentation.result.ResultActivity.Companion.REQUEST_WITH_RESPONSE_KEY
import az.zero.instabugtaskaz.utils.isValidURL
import az.zero.instabugtaskaz.utils.observeIfNotHandled
import com.google.android.material.textfield.TextInputEditText


class HomeFragment : Fragment(R.layout.fragment_home) {
    private val TAG = "HomeFragment"
    private lateinit var viewModel: HomeViewModel
    private lateinit var requestRadioGroup: RadioGroup
    private lateinit var getRadioButton: RadioButton
    private lateinit var postRadioButton: RadioButton
    private lateinit var requestButton: Button
    private lateinit var btnAddHeaders: ImageView
    private lateinit var btnAddQueryParams: ImageView
    private lateinit var headersLinearLayout: LinearLayout
    private lateinit var paramsLinearLayout: LinearLayout
    private lateinit var uriEd: TextInputEditText
    private lateinit var requestBodyEd: TextInputEditText
    private lateinit var requestBodyHost: Group
    private lateinit var rootSV: ScrollView

    private val headersViews = mutableListOf<View>()
    private val queryParametersViews = mutableListOf<View>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)

        // TODO: don't send request if no internet available

        val viewModelFactory = HomeProviderFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        observeState()
        setListeners()
        setClickListeners()
        observeEvents()
    }

    private fun observeEvents() {
        viewModel.homeEvent.observeIfNotHandled(viewLifecycleOwner) {
            when (it) {
                is HomeViewModelEvents.NavigateToResult -> {
                    /**
                     * Because I use tab layout I couldn't correctly replace the parent with
                     * the new fragment and it won't be good to use childFragmentManager
                     * as it will replace the current fragment leaving the tabs to be visible
                     * and this shouldn't be the case in my design
                     *
                     * Yes, this can be easily done with Navigation component but for the sake of
                     * this task "NO third party libraries are allowed"
                     *
                     * TODO: If their is time, use Precerable as it is more preferment especially in this case
                     *
                     * */
                    val intent = Intent(requireContext(), ResultActivity::class.java)
                    intent.putExtra(REQUEST_WITH_RESPONSE_KEY, it.requestWithResponseEntity)
                    startActivity(intent)
                }
            }
        }
    }


    private fun setListeners() {
        TransitionManager.beginDelayedTransition(rootSV)
        requestRadioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.get_radio_button -> viewModel.requestTypeChange(GET)
                R.id.post_radio_button -> viewModel.requestTypeChange(POST)
            }
        }
    }

    private fun observeState() {
        viewModel.homeState.observe(viewLifecycleOwner) {
            Log.e(TAG, "updateView: $it")
            val getRadioChecked = it.requestType == GET
            getRadioButton.isChecked = getRadioChecked
            postRadioButton.isChecked = !getRadioChecked
            requestBodyHost.isVisible = !getRadioChecked

        }
    }


    private fun setClickListeners() {
        btnAddHeaders.setOnClickListener {
            addViewsList(headersLinearLayout) {
                Log.e(TAG, "setClickListeners: dasdasdasa")
                headersViews.add(it)
            }
            addViewsToParent(headersLinearLayout, headersViews) {
                headersViews.remove(it)
            }
        }

        btnAddQueryParams.setOnClickListener {
            addViewsList(paramsLinearLayout) {
                queryParametersViews.add(it)
            }
            addViewsToParent(paramsLinearLayout, queryParametersViews) {
                queryParametersViews.add(it)
            }
        }

        requestButton.setOnClickListener {
            getRequestData()
        }
    }

    private fun getRequestData() {
        var errorMessage = ""
        val uri = "https://content.guardianapis.com"
        if (!uri.isValidURL()) errorMessage = "Not a valid uri"

        val headers = headersViews.map {
            val key = it.findViewById<TextInputEditText>(R.id.key_body_et).text.toString()
            val value = it.findViewById<TextInputEditText>(R.id.value_body_et).text.toString()
            ListMapItem(key, value)
        }

        val params = queryParametersViews.map {
            val key = it.findViewById<TextInputEditText>(R.id.key_body_et).text.toString()
            val value = it.findViewById<TextInputEditText>(R.id.value_body_et).text.toString()
            ListMapItem(key, value)
        }

        val requestBody = requestBodyEd.text.toString()

        val requestData = RequestData(
            uri = uri,
            requestHeaders = headers,
            paths = listOf(),
            requestBody = requestBody,
            queryParameters = params,
            requestType = viewModel.getRequestType(),
            executionTime = System.currentTimeMillis()
        )

        if (errorMessage.isEmpty()) {
            viewModel.networkCall(requestData)
        } else {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }


    private fun addViewsList(parent: LinearLayout, onAdd: (View) -> Unit) {
        val toAddView = LayoutInflater.from(requireContext())
            .inflate(R.layout.item_add_view, parent, false)
        onAdd(toAddView)
    }

    private fun addViewsToParent(
        parent: LinearLayout,
        viewsToAdd: MutableList<View>,
        onRemove: (View) -> Unit
    ) {
        TransitionManager.beginDelayedTransition(rootSV)
        viewsToAdd.forEach {
            if (it.parent == null) {
                parent.addView(it)
                it.findViewById<ImageView>(R.id.removeView).setOnClickListener { _ ->
                    Log.e(TAG, "setClickListeners: ${it.tag}")
                    TransitionManager.beginDelayedTransition(rootSV)
                    parent.removeView(it)
                    onRemove(it)
                }
            }
        }
    }


    private fun initViews(view: View) {
        view.apply {
            requestRadioGroup = findViewById(R.id.radioGroup)
            getRadioButton = findViewById(R.id.get_radio_button)
            postRadioButton = findViewById(R.id.post_radio_button)
            requestButton = findViewById(R.id.btn_request)
            btnAddHeaders = findViewById(R.id.btnAddHeaders)
            btnAddQueryParams = findViewById(R.id.btnAddQueryPrams)
            headersLinearLayout = findViewById(R.id.header_ll)
            paramsLinearLayout = findViewById(R.id.params_ll)
            uriEd = findViewById(R.id.url_et)
            requestBodyEd = findViewById(R.id.request_body_et)
            requestBodyHost = findViewById(R.id.request_body_host)
            rootSV = findViewById(R.id.root_scroll_view)
        }
    }
}