package com.yusufteker.worthy

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform