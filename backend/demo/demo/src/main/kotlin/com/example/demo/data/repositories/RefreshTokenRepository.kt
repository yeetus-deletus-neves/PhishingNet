package com.example.demo.data.repositories

import com.example.demo.data.entities.RefreshToken
import java.util.*

interface RefreshTokenRepository {

    fun updateRefreshToken(userID: UUID, newToken: String): RefreshToken?
    fun getRefreshToken(userID: UUID): RefreshToken?
}