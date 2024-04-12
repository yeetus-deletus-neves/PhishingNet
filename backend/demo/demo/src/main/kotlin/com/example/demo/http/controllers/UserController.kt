package com.example.demo.http.controllers

import com.example.demo.http.Uris
import com.example.demo.http.models.UserCreateInputModel
import com.example.demo.http.models.UserHomeOutputModel
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
            is CreateUserInfo.UserCreated -> ResponseEntity.status(201).body(res.userID);
            is CreateUserInfo.UserAlreadyExists -> ResponseEntity.status(409).body("This user already exists");
            is CreateUserInfo.UnsafePassword -> ResponseEntity.status(400).body("Password is unsafe, it must contain capital letters, numbers, and have a length of at least 8 characters")
        }
    }

    @GetMapping(Uris.Users.TOKEN)
    fun token(@RequestBody input: UserCreateInputModel): ResponseEntity<*>{
        return when(val res = userServices.createUserToken(input.username, input.password)){
            is CreateUserTokenInfo.TokenCreated -> ResponseEntity.status(200).body(res.token);
            is CreateUserTokenInfo.AuthenticationFailed -> ResponseEntity.status(401).body("Authentication Failed");
        }
    }

    @GetMapping(Uris.Users.BY_ID)
    fun getById(@PathVariable id: String): ResponseEntity<*>{
        return when (val res = userServices.getUserById(UUID.fromString(id))){
            is GetUserInfo.UserFound -> ResponseEntity.status(200).body(UserHomeOutputModel(
                res.user.username,
                res.user.userID.toString()
            ))
            is GetUserInfo.UserNotFound -> ResponseEntity.status(404).body("User not found")
            is GetUserInfo.AuthenticationFailed -> ResponseEntity.status(401).body("Authentication Failed");
        }
    }
}