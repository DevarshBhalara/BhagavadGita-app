package com.example.bhagavadgitaapp.listners

interface ItemClickListener<T> {

    /**
     * On click method for item click.
     *
     * @param item The data item
     * @param position Adapter position on which clicked
     */
    fun onClick(item: T, position: Int)
    fun onLikeClick(item: T, position: Int)

}