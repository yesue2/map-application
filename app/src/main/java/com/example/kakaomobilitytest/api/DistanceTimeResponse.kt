package com.example.kakaomobilitytest.api

data class DistanceTimeResponse(
    val distance: Int,    // 거리 (미터 단위)
    val time: Int         // 시간 (초 단위)
)