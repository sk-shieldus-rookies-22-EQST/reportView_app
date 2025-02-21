package com.example.rootread

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.rootread.utils.SecurityUtils
import com.example.rootread.utils.SessionManager
import com.google.android.material.navigation.NavigationView

class ActiveMain : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: androidx.navigation.NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navigationView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SecurityUtils.performSecurityCheck(this)

        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        toolbar = findViewById(R.id.tool_bar)

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
                R.id.chargePointFragment,
                R.id.purchaseProcessFragment,
                R.id.qnaWriterFragment,
                R.id.reWriterFragment,
                R.id.eachBoardFragment,
                R.id.userConfirmFragment,
                R.id.findPWConfirmFragment,
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        // 초기 메뉴 설정
        updateNavigationMenu()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = false // 클릭 후 선택 상태 해제

            // popUpTo 옵션을 사용하여 시작 목적지까지의 백 스택을 모두 제거합니다.
            // navController.graph.startDestinationId는 네비게이션 그래프의 루트(시작) 목적지를 의미합니다.
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setPopUpTo(R.id.listFragment, inclusive = false)
                .build()

            when (menuItem.itemId) {
                R.id.loginFragment -> {
                    if (SessionManager.isLoggedIn(this)) {
                        SessionManager.clearSession(this)
                        Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        navController.navigate(R.id.loginFragment, null, navOptions)
                    }
                    updateNavigationMenu()
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.logoutFragment -> {
                    SessionManager.clearSession(this)
                    Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                    navController.navigate(R.id.listFragment,null, navOptions) // 루트 페이지로 이동
                    updateNavigationMenu()
                    drawerLayout.closeDrawers()
                    true
                }
                else -> {
                    navController.navigate(menuItem.itemId, null, navOptions)
                    drawerLayout.closeDrawers()
                    true
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

    fun updateNavigationMenu() {
        val menu = navigationView.menu

        Log.d("ActiveMain", "updateNavigationMenu 호출됨 세션 확인: ${SessionManager.isLoggedIn(this)}")

        // 로그인 상태에 따라 그룹 가시성 설정
        menu.setGroupVisible(R.id.group_logged_out, !SessionManager.isLoggedIn(this))
        menu.setGroupVisible(R.id.group_logged_in, SessionManager.isLoggedIn(this))

        // 항상 표시되는 공통 메뉴
        menu.findItem(R.id.listFragment)?.isVisible = true
        menu.findItem(R.id.boardFragment)?.isVisible = true

        // 강제 UI 업데이트
        invalidateOptionsMenu()
    }
}
