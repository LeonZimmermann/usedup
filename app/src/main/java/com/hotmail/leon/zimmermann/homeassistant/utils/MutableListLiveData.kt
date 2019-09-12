package com.hotmail.leon.zimmermann.homeassistant.utils

import androidx.lifecycle.MutableLiveData

class MutableListLiveData<T>(list: MutableList<T>): MutableLiveData<MutableList<T>>(list) {

    /**
     * adds an item to the internal mutable list and
     * notifies all observers
     * value = value for observer notification
     */
    fun add(item: T) {
        value!!.add(item)
        value = value
    }

    /**
     * removes an item from the internal mutable list and
     * notifies all observers
     * value = value for observer notification
     */
    fun remove(item: T) {
        value!!.remove(item)
        value = value
    }

    /**
     *  changes an item from the internal mutable list and
     *  notifies all observers
     *  value = value for observer notification
     */
    fun change(item: T, function: (T) -> Unit) {
        function(item)
        value = value
    }

    operator fun plus(item: T) = add(item)
    operator fun minus(item: T) = remove(item)
}