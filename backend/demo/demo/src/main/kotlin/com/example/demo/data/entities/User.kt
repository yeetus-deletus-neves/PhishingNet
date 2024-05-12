package com.example.demo.data.entities

import jakarta.persistence.*
import java.util.*
import kotlin.collections.List

@Entity
@Table(name = "user", schema = "dbo")
open class User (username: String, encodedPassword: String, salt: String) {
    @Id
    @Column(name = "id", nullable = false)
    open var id: UUID = UUID.randomUUID()

    @Column(name = "username", nullable = false, length = 64)
    open var username: String = username

    @Column(name = "passwordinfo", nullable = false, length = 256)
    open var passwordinfo: String = encodedPassword

    @Column(name = "passwordsalt", nullable = false, length = 128)
    open var passwordSalt: String = salt

    @Column(name = "linked_email", nullable = true, length = 200)
    open var linked_email: String? = null

    @OneToMany(mappedBy = "userid")
    open var tokens: MutableSet<UserToken> = mutableSetOf()

    @OneToOne(mappedBy = "userid")
    open var refreshToken: RefreshToken? = null

}