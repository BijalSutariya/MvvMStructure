package com.example.mvvmstructure.clientlayer.presentations.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmstructure.R
import com.example.mvvmstructure.clientlayer.presentations.MainViewModel
import com.example.mvvmstructure.clientlayer.presentations.MoviePresenter
import com.example.mvvmstructure.clientlayer.presentations.ViewController
import com.example.mvvmstructure.databinding.FragmentListBinding
import com.example.mvvmstructure.databinding.MovieItemBinding
import com.example.mvvmstructure.domainlayer.model.Movie
import com.example.mvvmstructure.framework.BaseApp
import com.example.mvvmstructure.viewmodellayer.ViewModelFactory
import java.util.*
import javax.inject.Inject

class ListFragment : Fragment(), ViewController {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var listFragmentBinding: FragmentListBinding
    private lateinit var mainViewModel: MainViewModel
    private var savedInstanceState: Bundle? =null
    private val RECYCLER_VIEW_STATE_KEY = "RECYCLER_VIEW_STATE_KEY"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApp.viewModelComponent.doInjection(this)
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
          listFragmentBinding = FragmentListBinding.inflate(inflater,container,false)
        return listFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.savedInstanceState = savedInstanceState
        setUpInitialThings(view)

        val moviePresenter = MoviePresenter(
            mainViewModel, this, MoviePresenter.PresenterType.LIST
        )
        listFragmentBinding.moviePresenter = moviePresenter
        listFragmentBinding.lifecycleOwner = getLifeCycleOwner()
    }



    private fun setUpInitialThings(view: View) {
        val movieListAdapter = object : MovieListAdapter() {
            public override fun onMovieItemClicked(imdbId: String) {
                val bundle = Bundle()
                bundle.putString("movie_id", imdbId)
                Navigation.findNavController(view).navigate(R.id.detailsFragment, bundle)
            }
        }
        listFragmentBinding.movieListRecyclerView.layoutManager = LinearLayoutManager(activity)
        listFragmentBinding.movieListRecyclerView.adapter = movieListAdapter
        val snapHelper = StartSnapHelper()
        snapHelper.attachToRecyclerView(listFragmentBinding.movieListRecyclerView)
    }

    override fun onPause() {
        super.onPause()
        val bundle = Bundle()
        bundle.putParcelable(
            RECYCLER_VIEW_STATE_KEY,
            Objects.requireNonNull(listFragmentBinding.movieListRecyclerView.layoutManager)?.onSaveInstanceState()
        )
        arguments = bundle
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(
            RECYCLER_VIEW_STATE_KEY,
            Objects.requireNonNull(listFragmentBinding.movieListRecyclerView.layoutManager)!!.onSaveInstanceState()
        )
    }

    override fun onSucceed() {

        savedInstanceState = arguments
        if (savedInstanceState != null) {
            val savedRecyclerLayoutState = savedInstanceState!!.getParcelable<Parcelable>(RECYCLER_VIEW_STATE_KEY)
            Objects.requireNonNull(listFragmentBinding.movieListRecyclerView.layoutManager)?.onRestoreInstanceState(savedRecyclerLayoutState)
        }
    }

    override fun onErrorOccurred(message: String) {

    }

    override fun onLoadingOccurred() {
    }

    override fun getLifeCycleOwner(): LifecycleOwner {
        return viewLifecycleOwner
    }



    abstract inner class MovieListAdapter internal constructor() :
        RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {

        private var movieList: List<Movie>

        init {
            this.movieList = ArrayList()
        }

        internal fun setMovieList(movieList: List<Movie>) {
            this.movieList = movieList
            notifyDataSetChanged()
        }

        internal abstract fun onMovieItemClicked(imdbId: String)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {

            val binding = MovieItemBinding.inflate(layoutInflater, parent, false)
            return MovieListViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
            val movie = movieList[position]
            holder.movieItemBinding.root.setOnClickListener { onMovieItemClicked(movie.imdbID) }
            holder.bind(movie)
        }

        override fun getItemCount(): Int {
            return movieList.size
        }


        inner class MovieListViewHolder(val movieItemBinding: MovieItemBinding) :
            RecyclerView.ViewHolder(movieItemBinding.root) {

            fun bind(movie: Movie) {
                movieItemBinding.movie = movie
                movieItemBinding.executePendingBindings()
            }
        }
    }


    internal inner class StartSnapHelper : LinearSnapHelper() {

        private var mVerticalHelper: OrientationHelper? = null
        private var mHorizontalHelper: OrientationHelper? = null

        @Throws(IllegalStateException::class)
        override fun attachToRecyclerView(recyclerView: RecyclerView?) {
            super.attachToRecyclerView(recyclerView)
        }

        override fun calculateDistanceToFinalSnap(
            layoutManager: RecyclerView.LayoutManager,
            targetView: View
        ): IntArray? {
            val out = IntArray(2)

            if (layoutManager.canScrollHorizontally()) {
                out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager))
            } else {
                out[0] = 0
            }

            if (layoutManager.canScrollVertically()) {
                out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager))
            } else {
                out[1] = 0
            }
            return out
        }

        override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {

            return if (layoutManager is LinearLayoutManager) {

                if (layoutManager.canScrollHorizontally()) {
                    getStartView(layoutManager, getHorizontalHelper(layoutManager))
                } else {
                    getStartView(layoutManager, getVerticalHelper(layoutManager))
                }
            } else super.findSnapView(layoutManager)

        }

        private fun distanceToStart(targetView: View, helper: OrientationHelper): Int {
            return helper.getDecoratedStart(targetView) - helper.startAfterPadding
        }

        private fun getStartView(
            layoutManager: RecyclerView.LayoutManager,
            helper: OrientationHelper
        ): View? {

            if (layoutManager is LinearLayoutManager) {
                val firstChild = layoutManager.findFirstVisibleItemPosition()

                val isLastItem = layoutManager
                    .findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1

                if (firstChild == RecyclerView.NO_POSITION || isLastItem) {
                    return null
                }

                val child = layoutManager.findViewByPosition(firstChild)

                return if (helper.getDecoratedEnd(child) >= helper.getDecoratedMeasurement(child) / 2 && helper.getDecoratedEnd(
                        child
                    ) > 0
                ) {
                    child
                } else {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                        null
                    } else {
                        layoutManager.findViewByPosition(firstChild + 1)
                    }
                }
            }

            return super.findSnapView(layoutManager)
        }

        private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
            if (mVerticalHelper == null) {
                mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
            }
            return mVerticalHelper!!
        }

        private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
            if (mHorizontalHelper == null) {
                mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
            }
            return mHorizontalHelper!!
        }
    }
}
