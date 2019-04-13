package com.example.mvvmstructure.clientlayer.presentations.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmstructure.clientlayer.presentations.MainViewModel
import com.example.mvvmstructure.clientlayer.presentations.MoviePresenter
import com.example.mvvmstructure.clientlayer.presentations.ViewController
import com.example.mvvmstructure.databinding.FragmentDetailsBinding
import com.example.mvvmstructure.framework.BaseApp
import com.example.mvvmstructure.viewmodellayer.ViewModelFactory
import javax.inject.Inject

class DetailsFragment : Fragment(), ViewController {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var fragmentDetailsBinding: FragmentDetailsBinding
    private lateinit var mainViewModel: MainViewModel
    private var moviePresenter: MoviePresenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApp.viewModelComponent.doInjection(this)
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentDetailsBinding = FragmentDetailsBinding.inflate(inflater, container, false)

        return fragmentDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moviePresenter = MoviePresenter(mainViewModel, this, MoviePresenter.PresenterType.DETAILS)
        fragmentDetailsBinding.moviePresenter = moviePresenter
        fragmentDetailsBinding.lifecycleOwner = getLifeCycleOwner()
    }

    override fun onStart() {
        super.onStart()
        val bundle = arguments
        if (bundle != null)
            moviePresenter!!.getMovieIdLiveData().value = bundle.getString("movie_id")
    }

    override fun getLifeCycleOwner(): LifecycleOwner {
        return viewLifecycleOwner
    }

    override fun onErrorOccurred(message: String) {

    }

    override fun onSucceed() {

    }

    override fun onLoadingOccurred() {

    }


}
