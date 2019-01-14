package com.fastaccess.github.ui.modules.notifications

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.viewpager.widget.ViewPager
import com.fastaccess.data.model.FragmentType
import com.fastaccess.data.model.ViewPagerModel
import com.fastaccess.github.R
import com.fastaccess.github.base.BaseFragment
import com.fastaccess.github.base.BasePagerFragment
import com.fastaccess.github.base.BaseViewModel
import com.fastaccess.github.ui.adapter.PagerAdapter
import com.fastaccess.github.ui.modules.notifications.fragment.read.AllNotificationsFragment
import com.fastaccess.github.ui.modules.notifications.fragment.unread.UnreadNotificationsFragment
import com.fastaccess.github.utils.EXTRA
import kotlinx.android.synthetic.main.appbar_tabs_center_title_layout.*
import kotlinx.android.synthetic.main.toolbar_fragment_pager_layout.*

/**
 * Created by Kosh on 04.11.18.
 */
class NotificationPagerFragment : BasePagerFragment() {
    override fun viewModel(): BaseViewModel? = null
    override fun layoutRes(): Int = R.layout.toolbar_fragment_pager_layout
    override fun onPageSelected(page: Int) = (pager.adapter?.instantiateItem(pager, page) as? BaseFragment)?.onScrollToTop() ?: Unit
    override fun onFragmentCreatedWithUser(view: View, savedInstanceState: Bundle?) {
        setupToolbar(R.string.notifications, R.menu.notification_menu) { item: MenuItem ->
            if (item.itemId == R.id.markAllAsRead) {
                (pager.adapter?.instantiateItem(pager, 0) as? UnreadNotificationsFragment)?.markAllAsRead()
            }
        }

        pager.isEnabled = false
        pager.adapter = PagerAdapter(childFragmentManager, arrayListOf(
            ViewPagerModel(getString(R.string.unread), UnreadNotificationsFragment.newInstance(), FragmentType.UNREAD_NOTIFICATIONS),
            ViewPagerModel(getString(R.string.read), AllNotificationsFragment.newInstance(), FragmentType.ALL_NOTIFICATIONS)
        ))
        tabs.setupWithViewPager(pager)
        if (savedInstanceState == null) {
            FragmentType.getTypeSafely(arguments?.getString(EXTRA) ?: "")?.let {
                val index = (pager.adapter as PagerAdapter).getIndex(it)
                if (index >= 0) pager.currentItem = index
            }
        }
        pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                toolbar?.menu?.findItem(R.id.markAllAsRead)?.isVisible = position == 0
            }
        })
        toolbar?.menu?.findItem(R.id.markAllAsRead)?.isVisible = pager.currentItem == 0
    }

    companion object {
        fun newInstance(page: String? = FragmentType.UNREAD_NOTIFICATIONS.tabName) = NotificationPagerFragment().apply {
            arguments = bundleOf(Pair(EXTRA, page))
        }
    }
}