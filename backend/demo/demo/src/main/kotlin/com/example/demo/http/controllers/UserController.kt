package com.example.demo.http.controllers

import com.example.demo.data.entities.User
import com.example.demo.http.ResponseTemplate
import com.example.demo.http.Uris
import com.example.demo.http.models.TokenInputModel
import com.example.demo.http.models.TokenOutputModel
import com.example.demo.http.models.UserCreateInputModel
import com.example.demo.http.models.UserOutputModel
import com.example.demo.services.userServices.*
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
class UserController(private val userServices: UserServices) {

    private val logger = KotlinLogging.logger {}

    @PostMapping(Uris.Users.USER)
    fun create(@RequestBody input: UserCreateInputModel): ResponseEntity<*>{
        logger.info { "POST: ${Uris.Users.USER} received" }

        return when(val res = userServices.createUser(input.username, input.password)) {
            is CreateUserInfo.UserCreated -> ResponseTemplate.Created(UserOutputModel(
                res.user.id.toString(),
                res.user.username!!
            ), "User ${input.username} created")
            is CreateUserInfo.UserAlreadyExists -> ResponseTemplate.Conflict("This user already exists")
            is CreateUserInfo.UnsafePassword -> ResponseTemplate.BadRequest("Password is unsafe, it must contain capital letters, numbers, and have a length of at least 8 characters")
        }
    }

    @PostMapping(Uris.Users.TOKEN)
    fun token(@RequestBody input: UserCreateInputModel): ResponseEntity<*>{
        logger.info { "POST: ${Uris.Users.TOKEN} received" }

        return when(val res = userServices.createUserToken(input.username, input.password)){
            is CreateUserTokenInfo.TokenCreated -> ResponseTemplate.Ok(
                TokenOutputModel(
                    res.token.tokenvalidationinfo!!,
                    res.token.createdAt.toString(),
                    res.token.lastUsedAt.toString()
                ), "Token generated")
            is CreateUserTokenInfo.AuthenticationFailed -> ResponseTemplate.Unauthorized("Authentication failed")
        }
    }

    @GetMapping(Uris.Users.BY_ID)
    fun getById(@PathVariable id: String): ResponseEntity<*>{
        logger.info { "GET: ${Uris.Users.BY_ID} received" }

        return when (val res = userServices.getUserById(UUID.fromString(id))){
            is GetUserInfo.UserFound -> ResponseTemplate.Ok(UserOutputModel(
                res.user.id.toString(),
                res.user.username!!
            ), "User ${res.user.username} found by ID")
            is GetUserInfo.UserNotFound -> ResponseTemplate.NotFound("User not found")
            is GetUserInfo.AuthenticationFailed -> ResponseTemplate.Unauthorized("Authentication failed")
        }
    }

    @PostMapping(Uris.Users.LINK)
    fun linkAccount(user: User, @RequestBody token: TokenInputModel): ResponseEntity<*>{
        logger.info { "POST: ${Uris.Users.LINK} received" }

        return when (userServices.createRefreshToken(user,token.token)){
            is CreateRefreshTokenInfo.TokenCreated -> ResponseTemplate.Ok(null, "Account ${user.username} linked")
            is CreateRefreshTokenInfo.TokenAlreadyExists -> ResponseTemplate.Conflict("Account is already linked")
        }
    }

    @DeleteMapping(Uris.Users.UNLINK)
    fun unlinkAccount(user: User): ResponseEntity<*>{
        logger.info { "DELETE: ${Uris.Users.UNLINK} received" }

        return when (userServices.removeRefreshToken(user)){
            is RemoveRefreshTokenInfo.AccountUnlinked -> ResponseTemplate.Ok(null, "Account ${user.username} unlinked")
            is RemoveRefreshTokenInfo.AccountNotLinked -> ResponseTemplate.Conflict("Unable to remove account link: Account not linked to any Microsoft account")
        }
    }
}