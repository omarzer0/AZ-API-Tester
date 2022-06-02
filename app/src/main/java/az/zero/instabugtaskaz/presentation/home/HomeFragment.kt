package az.zero.instabugtaskaz.presentation.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import az.zero.instabugtaskaz.R
import az.zero.instabugtaskaz.data.network.RequestHandler.RequestType.GET
import az.zero.instabugtaskaz.data.network.RequestHandler.RequestType.POST
import az.zero.instabugtaskaz.databinding.FragmentHomeBinding
import az.zero.instabugtaskaz.domain.models.ListMapItem
import az.zero.instabugtaskaz.domain.models.request.RequestData
import az.zero.instabugtaskaz.presentation.result.ResultActivity
import az.zero.instabugtaskaz.presentation.result.ResultActivity.Companion.REQUEST_WITH_RESPONSE_KEY
import az.zero.instabugtaskaz.utils.isValidURL
import az.zero.instabugtaskaz.utils.observeIfNotHandled
import com.google.android.material.textfield.TextInputEditText


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private var toast: Toast? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        val viewModelFactory = HomeProviderFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        observeState()
        setCheckListeners()
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

    private fun setCheckListeners() {
        TransitionManager.beginDelayedTransition(binding.root)
        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.get_radio_button -> viewModel.requestTypeChange(GET)
                R.id.post_radio_button -> viewModel.requestTypeChange(POST)
            }
        }
    }

    private fun observeState() {
        viewModel.homeState.observe(viewLifecycleOwner) {
            binding.apply {
                val getRadioChecked = it.requestType == GET
                getRadioButton.isChecked = getRadioChecked
                queryBodyHost.isVisible = getRadioChecked
                postRadioButton.isChecked = !getRadioChecked
                requestBodyHost.isVisible = !getRadioChecked

                addVmViews(paramsLl, it.queries) {
                    viewModel.updateQueriesList(it, isAdd = false)
                }

                addVmViews(headerLl, it.headers) {
                    viewModel.updateHeadersList(it, isAdd = false)
                }
            }
        }
    }


    private fun setClickListeners() {
        binding.apply {

            btnAddHeaders.setOnClickListener {
                addSingleView(isAddToQueries = false)
            }

            btnAddQueryPrams.setOnClickListener {
                addSingleView(isAddToQueries = true)
            }

            btnRequest.setOnClickListener { getRequestData() }
        }
    }

    private fun getRequestData() {
        if (!isNetworkConnected()) {
            showToast("Please connect to the Internet and try again!")
            return
        }

        var errorMessage = ""
        val uri = binding.urlEt.text.toString()
        if (!uri.isValidURL()) errorMessage = "Not a valid uri"

        val headers = viewModel.getHeadersViews().map {
            binding.headerLl.findViewWithTag<View>(it) ?: return
        }.map {
            val key = it.findViewById<TextInputEditText>(R.id.key_body_et).text.toString()
            val value = it.findViewById<TextInputEditText>(R.id.value_body_et).text.toString()
            ListMapItem(key, value)
        }

        val params = viewModel.getQueryParametersViews().map {
            binding.paramsLl.findViewWithTag<View>(it) ?: return
        }.map {
            val key = it.findViewById<TextInputEditText>(R.id.key_body_et).text.toString()
            val value = it.findViewById<TextInputEditText>(R.id.value_body_et).text.toString()
            ListMapItem(key, value)
        }

        val requestBody = binding.requestBodyEt.text.toString()

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
            showToast(errorMessage)
        }
    }

    private fun addSingleView(isAddToQueries: Boolean) {
        val newItem = Pair(System.currentTimeMillis().toString(), ListMapItem("", ""))
        if (isAddToQueries) viewModel.updateQueriesList(newItem)
        else viewModel.updateHeadersList(newItem)
    }

    private fun addVmViews(
        parent: LinearLayout,
        items: Set<Pair<String, ListMapItem>>,
        onRemove: (Pair<String, ListMapItem>) -> Unit
    ) {
        TransitionManager.beginDelayedTransition(binding.root)
        items.forEach { pair ->
            val exist = parent.findViewWithTag<View>(pair.first) != null
            if (!exist) {
                val toAddView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_add_view, parent, false)
                toAddView.tag = pair.first
                val keyEd = toAddView.findViewById<TextInputEditText>(R.id.key_body_et)
                keyEd.setText(pair.second.key ?: return)

                val valueEd = toAddView.findViewById<TextInputEditText>(R.id.value_body_et)
                valueEd.setText(pair.second.value ?: return)

                parent.addView(toAddView)

                toAddView.findViewById<ImageView>(R.id.removeView).setOnClickListener {
                    TransitionManager.beginDelayedTransition(binding.root)
                    parent.removeView(parent.findViewWithTag(pair.first))
                    onRemove(pair)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val headers = viewModel.getHeadersViews().map {
            binding.headerLl.findViewWithTag<View>(it) ?: return
        }.map {
            val key = it.findViewById<TextInputEditText>(R.id.key_body_et).text.toString()
            val value = it.findViewById<TextInputEditText>(R.id.value_body_et).text.toString()
            ListMapItem(key, value)
        }.map {
            Pair(System.nanoTime().toString(), it)
        }

        val params = viewModel.getQueryParametersViews().map {
            binding.paramsLl.findViewWithTag<View>(it) ?: return
        }.map {
            val key = it.findViewById<TextInputEditText>(R.id.key_body_et).text.toString()
            val value = it.findViewById<TextInputEditText>(R.id.value_body_et).text.toString()
            ListMapItem(key, value)
        }.map {
            Pair(System.nanoTime().toString(), it)
        }

        viewModel.updateAllHeadersList(headers)
        viewModel.updateAllQueriesList(params)
    }

    private fun isNetworkConnected(): Boolean {
        val cm = requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    private fun showToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        toast?.show()
    }
}

