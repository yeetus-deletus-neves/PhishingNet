package com.example.demo.data.repositories

import com.example.demo.data.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UsersRepository : JpaRepository<User, UUID> {
    fun findUserById(id: UUID): User?
    fun findUserByUsername(username: String): User?
}