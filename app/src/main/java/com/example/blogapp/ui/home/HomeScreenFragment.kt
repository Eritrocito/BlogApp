package com.example.blogapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
//import com.bumptech.glide.load.engine.Resource
import com.example.blogapp.R
import com.example.blogapp.data.model.Post
import com.example.blogapp.data.remote.home.HomeScreenDataSource
import com.example.blogapp.databinding.FragmentHomeScreenBinding
import com.example.blogapp.domain.home.HomeScreenRepoImpl
import com.example.blogapp.presentation.HomeScreenViewModel
import com.example.blogapp.presentation.HomeScreenViewModelFactory
import com.example.blogapp.ui.home.adapter.HomeScreenAdapter
import com.example.blogapp.core.Result



class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel by viewModels<HomeScreenViewModel>{ HomeScreenViewModelFactory(
        HomeScreenRepoImpl(
        HomeScreenDataSource()
    )
    )}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentHomeScreenBinding.bind(view)

        viewModel.fetchLatestPosts().observe(viewLifecycleOwner, Observer{result ->
            when(result){
                is Result.Loading ->{
                    binding.progressBar.visibility=View.VISIBLE
                }
                is Result.Success ->{
                    binding.progressBar.visibility=View.GONE
                    binding.rvHome.adapter= HomeScreenAdapter(result.data)
                }
                is Result.Failure ->{
                    binding.progressBar.visibility=View.GONE
                    Toast.makeText(requireContext(),"${result.exception} ",Toast.LENGTH_LONG).show()
                    Log.e("Firebase","Excepcion",result.exception);

                }

            }
        })

        val postList = listOf(Post())


        binding.rvHome.adapter = HomeScreenAdapter(postList)

    }
}



