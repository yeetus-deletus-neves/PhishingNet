package com.example.demo.http.controllers

import com.example.demo.http.ErrorTemplate
import com.example.demo.http.Uris
import com.example.demo.http.models.UserCreateInputModel
import com.example.demo.http.models.UserOutputModel
import com.example.demo.services.userServices.CreateUserInfo
import com.example.demo.services.userServices.CreateUserTokenInfo
import com.example.demo.services.userServices.GetUserInfo
import com.example.demo.services.userServices.UserServices
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class UserController(private val userServices: UserServices) {

    @PostMapping(Uris.Users.USER)
    fun create(@RequestBody input: UserCreateInputModel): ResponseEntity<*>{
        return when(val res = userServices.createUser(input.username, input.password)) {
            is CreateUserInfo.UserCreated -> ResponseEntity.status(201).body(UserOutputModel(
                res.user.userID.toString(),
                res.user.username
            ))
            is CreateUserInfo.UserAlreadyExists -> ErrorTemplate.Conflict("This user already exists")
            is CreateUserInfo.UnsafePassword -> ErrorTemplate.BadRequest("Password is unsafe, it must contain capital letters, numbers, and have a length of at least 8 characters")
        }
    }

    @GetMapping(Uris.Users.TOKEN)
    fun token(@RequestBody input: UserCreateInputModel): ResponseEntity<*>{
        return when(val res = userServices.createUserToken(input.username, input.password)){
            is CreateUserTokenInfo.TokenCreated -> ResponseEntity.status(200).body(res.token)
            is CreateUserTokenInfo.AuthenticationFailed -> ErrorTemplate.Unauthorized("Authentication failed")
        }
    }

    @GetMapping(Uris.Users.BY_ID)
    fun getById(@PathVariable id: String): ResponseEntity<*>{
        return when (val res = userServices.getUserById(UUID.fromString(id))){
            is GetUserInfo.UserFound -> ResponseEntity.status(200).body(UserOutputModel(
                res.user.userID.toString(),
                res.user.username
            ))
            is GetUserInfo.UserNotFound -> ErrorTemplate.NotFound("User not found")
            is GetUserInfo.AuthenticationFailed -> ErrorTemplate.Unauthorized("Authentication failed")
        }
    }
}