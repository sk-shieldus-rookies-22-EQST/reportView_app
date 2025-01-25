package com.example.reportview_003

import android.os.Bundle
import android.util.Log
import android.view.Menu
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
                R.id.userPurchaseFragment,
                R.id.userBookListFragment,
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        // 초기 메뉴 설정
        updateNavigationMenu()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.loginFragment -> {
                    if (SessionManager.isLoggedIn(this)) {
                        // 로그아웃 로직
                        SessionManager.clearSession(this)
                        Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()

                        updateNavigationMenu()

                        navController.navigate(R.id.listFragment)
                    } else {
                        // 로그인 화면으로 이동
                        navController.navigate(R.id.loginFragment)
                    }
                    // 드로어 닫기
                    drawerLayout.closeDrawers()
                    true
                }
                else -> {
                    val handled = NavigationUI.onNavDestinationSelected(menuItem, navController)
                    if (handled) drawerLayout.closeDrawers() // 드로어 닫기
                    handled
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun addMenuItem(
        menu: Menu,
        itemId: Int,
        order: Int,
        title: String,
        iconRes: Int
    ) {
        menu.add(0, itemId, order, title).setIcon(iconRes)
    }

    private fun updateNavigationMenu() {
        val menu = navigationView.menu

        Log.d("ActiveMain", "updateNavigationMenu 호출됨 세션 확인: ${SessionManager.isLoggedIn(this)}")

        menu.clear()

        // 기본 메뉴
        addMenuItem(menu, R.id.listFragment, 1, "홈", R.drawable.home_black)
        addMenuItem(menu, R.id.boardFragment, 6, "게시판", R.drawable.web_black)
        addMenuItem(menu, R.id.bookDetailFragment, 7, "책 상세", R.drawable.purchase_black)
        addMenuItem(menu, R.id.purchaseFragment, 3, "장바구니", R.drawable.cart_black)

        if (SessionManager.isLoggedIn(this)) {
            // 로그인 상태 메뉴
            addMenuItem(menu, R.id.loginFragment, 0, "로그아웃", R.drawable.logout_black)
            addMenuItem(menu, R.id.userinfofragment, 2, "내 정보", R.drawable.person_black)
            addMenuItem(menu, R.id.userBookListFragment, 4, "내 서제", R.drawable.book_black)
            addMenuItem(menu, R.id.userPurchaseFragment, 5, "결제내역", R.drawable.purchase_black)
        } else {
            // 로그아웃 상태 메뉴
            addMenuItem(menu, R.id.loginFragment, 0, "로그인", R.drawable.login_black)
        }

        // 강제 UI 업데이트
        invalidateOptionsMenu()
    }
}
