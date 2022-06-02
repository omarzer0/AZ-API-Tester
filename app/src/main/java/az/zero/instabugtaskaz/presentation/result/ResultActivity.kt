package az.zero.instabugtaskaz.presentation.result

import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import az.zero.instabugtaskaz.data.db.RequestWithResponseEntity
import az.zero.instabugtaskaz.data.network.RequestHandler.RequestType.GET
import az.zero.instabugtaskaz.databinding.ActivityResultBinding
import az.zero.instabugtaskaz.presentation.adapters.ListMapAdapter
import az.zero.instabugtaskaz.utils.show

class ResultActivity : AppCompatActivity() {
    private var data: RequestWithResponseEntity? = null
    private lateinit var viewModel: ResultViewModel
    private lateinit var binding: ActivityResultBinding
    private val queriesAdapter = ListMapAdapter()
    private val requestHeaderAdapter = ListMapAdapter()
    private val responseHeadersAdapter = ListMapAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ResultViewModel::class.java]
        initViews()

        val serializable = intent.getSerializableExtra(REQUEST_WITH_RESPONSE_KEY)
        data = if (serializable != null) serializable as RequestWithResponseEntity else null
        viewModel.passData(data)

        setDataToViews(viewModel.getData())
        observeState()
        handleClicks()

    }

    private fun observeState() {
        viewModel.resultState.observe(this) {
            binding.apply {
                TransitionManager.beginDelayedTransition(root)

                btnShowRequestHeaders.isVisible = it.requestHeaderVisible
                btnHideRequestHeaders.isVisible = !it.requestHeaderVisible
                rvRequestHeaders.isVisible = it.requestHeaderVisible

                btnShowResponseHeaders.isVisible = it.responseHeaderVisible
                btnHideResponseHeaders.isVisible = !it.responseHeaderVisible
                rvResponseHeaders.isVisible = it.responseHeaderVisible

                btnShowResponseBody.isVisible = it.responseBodyVisible
                btnHideResponseBody.isVisible = !it.responseBodyVisible
                tvResponseBody.isVisible = it.responseBodyVisible

                if (viewModel.getRequestType() == GET.name) {
                    btnShowQueries.isVisible = it.queryParamVisible
                    btnHideQueries.isVisible = !it.queryParamVisible
                    rvQueryParams.isVisible = it.queryParamVisible
                } else {
                    btnShowRequestBody.isVisible = it.requestBodyVisible
                    btnHideRequestBody.isVisible = !it.requestBodyVisible
                    tvRequestBody.isVisible = it.requestBodyVisible
                }
            }
        }
    }

    private fun handleClicks() {
        binding.apply {
            btnShowQueries.setOnClickListener {
                viewModel.toggle(WhichViewToToggle.QUERY)
            }
            btnHideQueries.setOnClickListener {
                viewModel.toggle(WhichViewToToggle.QUERY)
            }

            btnShowRequestHeaders.setOnClickListener {
                viewModel.toggle(WhichViewToToggle.REQUEST_HEADER)
            }
            btnHideRequestHeaders.setOnClickListener {
                viewModel.toggle(WhichViewToToggle.REQUEST_HEADER)
            }

            btnShowResponseHeaders.setOnClickListener {
                viewModel.toggle(WhichViewToToggle.RESPONSE_HEADER)
            }

            btnHideResponseHeaders.setOnClickListener {
                viewModel.toggle(WhichViewToToggle.RESPONSE_HEADER)
            }

            btnShowRequestBody.setOnClickListener {
                viewModel.toggle(WhichViewToToggle.REQUEST_BODY)
            }

            btnHideRequestBody.setOnClickListener {
                viewModel.toggle(WhichViewToToggle.REQUEST_BODY)
            }

            btnShowResponseBody.setOnClickListener {
                viewModel.toggle(WhichViewToToggle.RESOPNSE_BODY)
            }

            btnHideResponseBody.setOnClickListener {
                viewModel.toggle(WhichViewToToggle.RESOPNSE_BODY)
            }
        }
    }

    private fun initViews() {
        binding.apply {
            rvQueryParams.adapter = queriesAdapter
            rvRequestHeaders.adapter = requestHeaderAdapter
            rvResponseHeaders.adapter = responseHeadersAdapter
        }
    }

    private fun setDataToViews(viewModelData: RequestWithResponseEntity?) {
        viewModelData?.let {
            binding.apply {
                tvRequestType.text = it.request.requestType
                tvResponseCode.text = "${it.response.responseCode}"
                val color = if (it.response.success) Color.GREEN
                else Color.RED
                tvResponseCode.setBackgroundColor(color)
                tvErrorMessage.text = it.response.error.ifEmpty { "No Error" }

                queriesAdapter.changeItems(it.request.queryParameters)
                requestHeaderAdapter.changeItems(it.request.requestHeaders)
                responseHeadersAdapter.changeItems(it.response.responseHeaders)

                tvRequestBody.text = it.request.requestBody.ifEmpty { "No Request Body" }
                tvResponseBody.text = it.response.responseBody.ifEmpty { "No Response Body" }
                tvUri.text = it.request.uri

                val isGetRequest = viewModel.getRequestType() == GET.name
                if (isGetRequest) {
                    tvQueryParamsTitle.show()
                    btnShowQueries.show()
                    btnHideQueries.show()
                    rvQueryParams.show()
                } else {
                    tvRequestBodyTitle.show()
                    btnShowRequestBody.show()
                    btnHideRequestBody.show()
                    tvRequestBody.show()
                }

            }
        }
    }

    companion object {
        const val REQUEST_WITH_RESPONSE_KEY = "requestWithResponseEntity"
    }
}