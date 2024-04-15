package com.example.demo.http.controllers

import com.example.demo.data.entities.User
import com.example.demo.http.ErrorTemplate
import com.example.demo.http.Uris
import com.example.demo.http.models.TokenInputModel
import com.example.demo.http.models.TokenOutputModel
import com.example.demo.http.models.UserCreateInputModel
import com.example.demo.http.models.UserOutputModel
import com.example.demo.services.userServices.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
class UserController(private val userServices: UserServices) {

    @PostMapping(Uris.Users.USER)
    fun create(@RequestBody input: UserCreateInputModel): ResponseEntity<*>{
        return when(val res = userServices.createUser(input.username, input.password)) {
            is CreateUserInfo.UserCreated -> ResponseEntity.status(201).body(UserOutputModel(
                res.user.id.toString(),
                res.user.username!!
            ))
            is CreateUserInfo.UserAlreadyExists -> ErrorTemplate.Conflict("This user already exists")
            is CreateUserInfo.UnsafePassword -> ErrorTemplate.BadRequest("Password is unsafe, it must contain capital letters, numbers, and have a length of at least 8 characters")
        }
    }

    @PostMapping(Uris.Users.TOKEN)
    fun token(@RequestBody input: UserCreateInputModel): ResponseEntity<*>{
        return when(val res = userServices.createUserToken(input.username, input.password)){
            is CreateUserTokenInfo.TokenCreated -> ResponseEntity.status(200).body(
                TokenOutputModel(
                    res.token.tokenvalidationinfo!!,
                    res.token.createdAt.toString(),
                    res.token.lastUsedAt.toString()
                ))
            is CreateUserTokenInfo.AuthenticationFailed -> ErrorTemplate.Unauthorized("Authentication failed")
        }
    }

    @GetMapping(Uris.Users.BY_ID)
    fun getById(@PathVariable id: String): ResponseEntity<*>{
        return when (val res = userServices.getUserById(UUID.fromString(id))){
            is GetUserInfo.UserFound -> ResponseEntity.status(200).body(UserOutputModel(
                res.user.id.toString(),
                res.user.username!!
            ))
            is GetUserInfo.UserNotFound -> ErrorTemplate.NotFound("User not found")
            is GetUserInfo.AuthenticationFailed -> ErrorTemplate.Unauthorized("Authentication failed")
        }
    }

    @PostMapping(Uris.Users.LINK)
    fun linkAccount(user: User, @RequestBody token: TokenInputModel): ResponseEntity<*>{
        return when (userServices.createRefreshToken(user,token.token)){
            is CreateRefreshTokenInfo.TokenCreated -> ResponseEntity.status(200).body(null)
            is CreateRefreshTokenInfo.TokenAlreadyExists -> ErrorTemplate.Conflict("Account is already linked")
        }
    }

    @DeleteMapping(Uris.Users.UNLINK)
    fun unlinkAccount(user: User): ResponseEntity<*>{
        return when (userServices.removeRefreshToken(user)){
            is RemoveRefreshTokenInfo.AccountUnlinked -> ResponseEntity.status(200).body(null)
            is RemoveRefreshTokenInfo.AccountNotLinked -> ErrorTemplate.Conflict("Unable to remove account link: Account not linked to any Microsoft account")
        }
    }
}