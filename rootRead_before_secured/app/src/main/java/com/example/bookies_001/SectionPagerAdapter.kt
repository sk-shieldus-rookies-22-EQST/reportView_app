package com.example.bookies_001

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(
    activity: FragmentActivity,
    private var fragments: List<Fragment>
): FragmentStateAdapter(activity) {

    private val fragmentIds = fragments.map { it.hashCode().toLong() }

    override fun getItemCount(): Int = fragments.size

    override fun getItemId(position: Int): Long = fragmentIds[position]

    override fun containsItem(itemId: Long): Boolean = fragmentIds.contains(itemId)

    override fun createFragment(position: Int): Fragment {
        if (position in fragments.indices) {
            return fragments[position]
        } else {
            throw IllegalArgumentException("Invalid position")
        }
    }

    fun updateFragments(newFragments: List<Fragment>) {
        fragments = newFragments
        notifyDataSetChanged()
    }
}