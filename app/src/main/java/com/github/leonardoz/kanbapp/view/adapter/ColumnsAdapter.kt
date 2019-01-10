package com.github.leonardoz.kanbapp.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ColumnsAdapter(
    manager: FragmentManager
) : FragmentStatePagerAdapter(manager) {

    var dataset: List<Pair<String, Fragment>> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItem(position: Int): Fragment = dataset[position].second

    override fun getCount(): Int = dataset.size

    override fun getPageTitle(position: Int): CharSequence? {
        return dataset[position].first
    }


}