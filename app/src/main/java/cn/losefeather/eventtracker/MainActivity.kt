package cn.losefeather.eventtracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.losefeather.eventtracker.ui.theme.EventTrackerTheme
import cn.losefeather.library_tracker.annotation.AutoTrack

// 定义路由常量
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Second : Screen("second")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventTrackerTheme {
                // 创建导航控制器
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 设置导航宿主
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 首页
                        composable(Screen.Home.route) {
                            HomeScreen(onNavigateToSecond = {
                                navController.navigate(Screen.Second.route)
                            })
                        }
                        // 第二个页面
                        composable(Screen.Second.route) {
                            SecondScreen(onNavigateBack = {
                                navController.popBackStack()
                            })
                        }
                    }
                }
            }
        }
    }
}

@AutoTrack("首页")
@Composable
fun HomeScreen(onNavigateToSecond: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "首页 - 点击跳转到第二个页面",
            modifier = Modifier
                .align(Alignment.Center)
                .clickable {
                    onNavigateToSecond() // 点击触发跳转
                }
        )
    }

    // 页面生命周期追踪
    DisposableEffect(Unit) {
        // 页面进入事件
        trackPageEnter("首页")
        onDispose {
            // 页面离开事件
            trackPageLeave("首页")
        }
    }
}

@AutoTrack("第二个页面")
@Composable
fun SecondScreen(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "第二个页面",
            modifier = Modifier.padding(20.dp)
        )
        Text(
            text = "点击返回首页",
            modifier = Modifier.clickable {
                onNavigateBack() // 点击返回
            }
        )
    }

    // 页面生命周期追踪
    DisposableEffect(Unit) {
        trackPageEnter("第二个页面")
        onDispose {
            trackPageLeave("第二个页面")
        }
    }
}

// 模拟页面追踪函数（实际项目中替换为真实实现）
fun trackPageEnter(pageName: String) {
    Log.e("Tracker", "trackPageEnter: $pageName")
}

fun trackPageLeave(pageName: String) {
    Log.e("Tracker", "trackPageLeave: $pageName")
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    EventTrackerTheme {
        HomeScreen(onNavigateToSecond = {})
    }
}

@Preview(showBackground = true)
@Composable
fun SecondScreenPreview() {
    EventTrackerTheme {
        SecondScreen(onNavigateBack = {})
    }
}