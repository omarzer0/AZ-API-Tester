package az.zero.instabugtaskaz.presentation.result

import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionManager
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Transition
import az.zero.instabugtaskaz.data.db.RequestWithResponseEntity
import az.zero.instabugtaskaz.databinding.ActivityResultBinding
import az.zero.instabugtaskaz.presentation.adapters.ListMapAdapter

class ResultActivity : AppCompatActivity() {
    private val TAG = "ResultActivity"
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
                btnShowQueries.isVisible = it.queryParamVisible
                btnHideQueries.isVisible = !it.queryParamVisible
                rvQueryParams.isVisible = it.queryParamVisible

                btnShowRequestHeaders.isVisible = it.requestHeaderVisible
                btnHideRequestHeaders.isVisible = !it.requestHeaderVisible
                rvRequestHeaders.isVisible = it.requestHeaderVisible

                btnShowResponseHeaders.isVisible = it.responseHeaderVisible
                btnHideResponseHeaders.isVisible = !it.responseHeaderVisible
                rvResponseHeaders.isVisible = it.responseHeaderVisible
            }
        }
    }

    private fun handleClicks() {
        binding.apply {
            btnShowQueries.setOnClickListener {
                viewModel.toggle(WhichViewToGoggle.QUERY)
            }
            btnHideQueries.setOnClickListener {
                viewModel.toggle(WhichViewToGoggle.QUERY)
            }

            btnShowRequestHeaders.setOnClickListener {
                viewModel.toggle(WhichViewToGoggle.REQUEST_HEADER)
            }
            btnHideRequestHeaders.setOnClickListener {
                viewModel.toggle(WhichViewToGoggle.REQUEST_HEADER)
            }

            btnShowResponseHeaders.setOnClickListener {
                viewModel.toggle(WhichViewToGoggle.RESPONSE_HEADER)
            }

            btnHideResponseHeaders.setOnClickListener {
                viewModel.toggle(WhichViewToGoggle.RESPONSE_HEADER)
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
            }
        }
    }

    companion object {
        const val REQUEST_WITH_RESPONSE_KEY = "requestWithResponseEntity"
    }
}