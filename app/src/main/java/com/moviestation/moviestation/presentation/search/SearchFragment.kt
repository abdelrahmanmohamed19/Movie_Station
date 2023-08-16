package com.moviestation.moviestation.presentation.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.moviestation.databinding.FragmentSearchBinding
import com.moviestation.moviestation.data.remote.dto.Tv
import com.moviestation.moviestation.presentation.adapter.MainAdapter
import com.moviestation.moviestation.domain.model.Trending
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var navController: NavController
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        navController = findNavController()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.SearchTextField?.addTextChangedListener{text ->
                lifecycleScope.launch {
                    delay(1000)
                    viewModel.getSearchedItem(text.toString())
                    viewModel.searchedItem.collect{
                        val myAdapter = MainAdapter(navController,"search")
                        myAdapter.setList(mapSearchItemToTrending(it))
                        binding?.SearchRecyclerView?.adapter=myAdapter
                    }
                }
            }

            }

    private fun mapSearchItemToTrending(searchList : List<Tv>) : List<Trending>{
        val newList = mutableListOf<Trending>()
        searchList.forEach{
            newList.add(Trending(it.name,it.poster,it.voteAverage,it.overView))
        }
        return newList
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}