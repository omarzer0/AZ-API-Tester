package az.zero.instabugtaskaz.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import az.zero.instabugtaskaz.R
import az.zero.instabugtaskaz.databinding.FragmentSearchBinding
import az.zero.instabugtaskaz.presentation.adapters.RequestWithResponseAdapter
import az.zero.instabugtaskaz.presentation.search.TypeToShow.*

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val TAG = "SearchFragment"
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: RequestWithResponseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        val viewModelFactory = SearchProviderFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]
        adapter = RequestWithResponseAdapter {

        }
        observeState()
        setClickListeners()
    }

    private fun observeState() {
        viewModel.searchState.observe(viewLifecycleOwner) {
            Log.e(TAG, "updateView: $it")

            binding.apply {
                val typeToShowId = when (it.typeToShow) {
                    ALL -> R.id.chpShowAll
                    GET -> R.id.chpGetOnly
                    POST -> R.id.chpPostOnly
                }
                chipGroupType.check(typeToShowId)

                val sortId = if (it.sortDesc) R.id.chpDesc else R.id.chpAsc
                chpGroupTime.check(sortId)
            }
        }
    }

    private fun setClickListeners() {
        binding.apply {
            chpGroupTime.setOnCheckedStateChangeListener { _, checkedIds ->
                if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener
                val sortDesc = when (checkedIds[0]) {
                    R.id.chpAsc -> false
                    else -> true
                }
                viewModel.updateSortBy(sortDesc)
            }

            chipGroupType.setOnCheckedStateChangeListener { _, checkedIds ->
                if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener
                val typeToShowId = when (checkedIds[0]) {
                    R.id.chpGetOnly -> GET
                    R.id.chpPostOnly -> POST
                    else -> ALL
                }
                viewModel.updateTypeToShow(typeToShowId)
            }
        }
    }


}