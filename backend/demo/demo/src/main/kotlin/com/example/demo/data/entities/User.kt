package com.example.demo.data.entities

import jakarta.persistence.*
import java.util.*
import kotlin.collections.List

@Entity
@Table(name = "user", schema = "dbo")
open class User (username: String, encodedPassword: String) {
    @Id
    @Column(name = "id", nullable = false)
    open var id: UUID? = UUID.randomUUID()

    @Column(name = "username", nullable = false, length = 64)
    open var username: String? = username

    @Column(name = "passwordinfo", nullable = false, length = 256)
    open var passwordinfo: String? = encodedPassword

    @OneToMany(mappedBy = "userid")
    open var tokens: MutableSet<UserToken> = mutableSetOf()

    @OneToOne(mappedBy = "userid")
    open var refreshToken: RefreshToken? = null

}