package com.example.reportview_003

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.reportview_003.utils.SessionManager
import com.google.android.material.navigation.NavigationView

class ActiveMain : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        val toolbar: Toolbar = findViewById(R.id.tool_bar)

        // Toolbar 설정
        setSupportActionBar(toolbar)

        // DrawerLayout 및 NavController 설정
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.listFragment,
                R.id.loginFragment,
                R.id.boardFragment,
                R.id.userinfofragment,
                R.id.bookDetailFragment,
                R.id.purchaseFragment,
                R.id.findIDFragment,
                R.id.findPWFragment,
                R.id.signupFragment,
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        // 초기 메뉴 설정
        updateNavigationMenu()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_login -> {
                    if (SessionManager.isLoggedIn(this)) {
                        // 로그아웃 로직
                        SessionManager.clearSession(this)
                        Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 로그인 화면으로 이동
                        navController.navigate(R.id.loginFragment)
                    }
                    // 메뉴 업데이트
                    updateNavigationMenu()
                    true
                }
                else -> {
                    val handled = NavigationUI.onNavDestinationSelected(menuItem, navController)
                    if (handled) drawerLayout.closeDrawers()
                    handled
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // 네비게이션 메뉴 업데이트
    private fun updateNavigationMenu() {
        val menu = navigationView.menu
        menu.clear()

        // 기본 메뉴 추가
        menu.add(0, R.id.listFragment, 1, "홈").setIcon(R.drawable.home_black)
        menu.add(0, R.id.boardFragment, 4, "게시판").setIcon(R.drawable.web_black)
        menu.add(0, R.id.userinfofragment, 2, "내 정보").setIcon(R.drawable.person_black)

        // 로그인 상태에 따라 로그인/로그아웃 메뉴 추가
        if (SessionManager.isLoggedIn(this)) {
            menu.add(0, R.id.menu_login, 0, "로그아웃").setIcon(R.drawable.logout_black)
            menu.add(0, R.id.userBookListFragment, 5, "내 서제").setIcon(R.drawable.books_black)
        } else {
            menu.add(0, R.id.menu_login, 0, "로그인").setIcon(R.drawable.login_black)
        }

        // 강제 UI 업데이트
        navigationView.invalidate()
    }
}
