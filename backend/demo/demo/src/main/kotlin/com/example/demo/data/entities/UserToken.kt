package com.example.demo.data.entities

import jakarta.persistence.*
import java.time.Duration
import java.time.Instant

@Entity
@Table(name = "usertoken", schema = "dbo")
open class UserToken(token: String, user: User, instant: Instant) {
    @Id
    @Column(name = "tokenvalidationinfo", nullable = false, length = 256)
    open var tokenvalidationinfo: String = token

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid", nullable = false)
    open var userid: User = user

    @Column(name = "created_at", nullable = false)
    open var createdAt: Long = instant.toEpochMilli()

    @Column(name = "last_used_at", nullable = false)
    open var lastUsedAt: Long = instant.toEpochMilli()

    companion object{
        val MAX_TOKENS: Int = 3
        val TOKENS_TTL: Duration = Duration.ofDays(1)
    }
}