package com.example.mvvmstructure.clientlayer.presentations

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmstructure.clientlayer.presentations.fragment.ListFragment
import com.example.mvvmstructure.domainlayer.model.Movie
import java.util.*


class BindingAdapter {

    companion object {
        @JvmStatic
        @BindingAdapter("list")
        fun setList(view: RecyclerView, movieList: List<Movie>?) {
            if (movieList != null) {
                (Objects.requireNonNull(view.adapter) as ListFragment.MovieListAdapter).setMovieList(movieList)
            }
        }

        @JvmStatic
        @BindingAdapter("imageSource")
        fun setImage(view: ImageView, url: String?) {
            Glide.with(view.context).load(url).into(view)
        }

    }
}