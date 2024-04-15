package com.example.demo.data.repositories

import com.example.demo.data.entities.RefreshToken
import com.example.demo.data.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, Int>{

    fun findRefreshTokensByUserid(user: User): RefreshToken?
}