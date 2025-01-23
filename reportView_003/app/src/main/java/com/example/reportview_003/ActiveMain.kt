package com.example.reportview_003

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.reportview_003.ui.auth.LoginFragment
import com.example.reportview_003.ui.board.BoardFragment
import com.example.reportview_003.ui.user.UserFragment
import com.example.reportview_003.ui.view.BookDetailFragment
import com.example.reportview_003.ui.view.ListFragment
import com.example.reportview_003.ui.view.BookViewFragment
import com.example.reportview_003.utils.SessionManager
import com.google.android.material.navigation.NavigationView

class ActiveMain : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var orgView: NavigationView
    private lateinit var viewPager: ViewPager2
    private lateinit var toggle: ActionBarDrawerToggle

    // Page position and navigation item ID mappings
    private val pageToNavItemMap = mapOf(
        0 to R.id.nav_login,
        1 to R.id.list_main,
        2 to R.id.book_viewer,
        3 to R.id.board_main,
        4 to R.id.userinfo_main,
        5 to R.id.book_detail_main
    )

    private val navItemToPageMap = pageToNavItemMap.entries.associate { it.value to it.key }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupDrawerToggle()
        setupViewPager()
        setupNavigationMenu()
    }

    private fun initViews() {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        orgView = navigationView
    }

    private fun setupDrawerToggle() {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setupViewPager() {
        viewPager = findViewById(R.id.view_pager)

        // fragment 추가시 아래 변수에 추가
        val initialFragments = listOf(
            LoginFragment(),
            ListFragment(),
            BookViewFragment(),
            BoardFragment(),
            UserFragment(),
            BookDetailFragment()
        )

        viewPager.isUserInputEnabled = false

        val adapter = SectionPagerAdapter(this, initialFragments)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                pageToNavItemMap[position]?.let { navItemId ->
                    navigationView.setCheckedItem(navItemId)
                }
            }
        })
    }

    private fun setupNavigationMenu() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // 메뉴 아이템 선택 시 ViewPager 페이지 이동
            navItemToPageMap[menuItem.itemId]?.let { pagePosition ->
                viewPager.currentItem = pagePosition
            }

            if (menuItem.itemId == R.id.nav_logout) {
                performLogout()
            }

            drawerLayout.closeDrawers()
            true
        }
    }

    override fun onResume() {
        super.onResume()
        updateNavigationMenu()
    }

    fun updateNavigationMenu() {
        val menu = navigationView.menu

        // Clear previous login/logout menu items
        menu.findItem(R.id.nav_login)?.let { menu.removeItem(it.itemId) }
        menu.findItem(R.id.nav_logout)?.let { menu.removeItem(it.itemId) }

        // Add appropriate menu item based on session status
        if (SessionManager.isLoggedIn(this)) {
            menu.add(0, R.id.nav_logout, 0, "로그아웃").setIcon(R.drawable.file_open_black)
        } else {
            menu.add(0, R.id.nav_login, 0, "로그인").setIcon(R.drawable.filter_black)
        }

        // Force navigation view to refresh
        navigationView.invalidate()
        navigationView.requestLayout()
    }

    private fun performLogout() {
        SessionManager.clearSession(this)
        Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
        updateNavigationMenu()
        viewPager.currentItem = 0
    }

    fun onLoginSuccess() {
        SessionManager.saveLoginSession(this, "your_auth_token")
        updateNavigationMenu()
        viewPager.currentItem = 1
    }
}
