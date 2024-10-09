package com.example.kakaomobilitytest.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.mavericksViewModel
import com.airbnb.mvrx.compose.collectAsState
import com.example.kakaomobilitytest.api.Location
import com.example.kakaomobilitytest.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = mavericksViewModel()) {
    val state by viewModel.collectAsState()

    when {
        state.errorMessage != null -> {
            // 에러 메시지가 있을 때 다이얼로그 표시
            ShowErrorDialog(
                errorCode = state.errorCode ?: 4041, // 상태에서 에러 코드 가져오기
                errorMessage = state.errorMessage ?: "not_found", // 상태에서 에러 메시지 가져오기
                origin = state.selectedOrigin ?: "",
                destination = state.selectedDestination ?: ""
            ) {
                // 다이얼로그가 닫힐 때 에러 초기화
                viewModel.clearError()
            }
        }
        state.locations.isNotEmpty() -> {
            LocationListScreen(
                locations = state.locations,
                onLocationSelected = { origin, destination ->
                    viewModel.getRoutes(origin, destination) // 경로 조회
                }
            ) // 로케이션 리스트 화면 출력
            Log.d("MainScreen", "경로 있음")


        }
        else -> {
            Text(text = "Loading...") // 로딩 상태 처리
        }
    }
}

@Composable
fun LocationListScreen(locations: List<Location>, onLocationSelected: (String, String) -> Unit) {
    LazyColumn {
        items(locations) { location ->
            LocationItem(
                origin = location.origin,
                destination = location.destination,
                onClick = { onLocationSelected(location.origin, location.destination) }
            )
        }
    }
}

@Composable
fun LocationItem(origin: String, destination: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Text(text = "출발지 : $origin", color = MaterialTheme.colorScheme.onBackground)
        Text(text = "도착지 : $destination", color = MaterialTheme.colorScheme.primary)
        Divider(color = MaterialTheme.colorScheme.secondary, thickness = 1.dp)
    }
}