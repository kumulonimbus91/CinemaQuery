package com.nenad.cinemaquery.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nenad.cinemaquery.data.model.Result
import com.nenad.cinemaquery.databinding.ItemMoviePreviewBinding
import com.nenad.cinemaquery.util.Constants.imgPath


class MoviesAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.ViewHolder {

        return ViewHolder.from(parent)

    }

    override fun onBindViewHolder(holder: MoviesAdapter.ViewHolder, position: Int) {
        val movie = differ.currentList[position]

        holder.bind(movie)


        holder.itemView.apply {
            Glide.with(this).load(imgPath + movie.poster_path).into(holder.binding.ivMoviePoster)
            holder.binding.movieTitle.text = movie.title
            holder.binding.movieDescription.text = movie.overview

            setOnClickListener {
                onItemClickListener?.let {
                    it(movie)
                }
            }


        }


    }


    override fun getItemCount(): Int {
        return differ.currentList.size

    }

    class ViewHolder constructor(
        val binding: ItemMoviePreviewBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                val binding = ItemMoviePreviewBinding.inflate(layoutInflater, parent, false)




                return ViewHolder(binding)
            }
        }

        fun bind(item: Result) {


        }


        override fun onClick(p0: View?) {

        }


    }

    private var onItemClickListener: ((Result) -> Unit)? = null

    fun setOnClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener

    }


}