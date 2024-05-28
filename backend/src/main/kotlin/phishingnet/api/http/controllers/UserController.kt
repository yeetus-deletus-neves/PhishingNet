package phishingnet.api.http.controllers

import phishingnet.api.data.entities.User
import phishingnet.api.http.ResponseTemplate
import phishingnet.api.http.Uris
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import phishingnet.api.http.models.*
import phishingnet.api.services.userServices.*

@RestController
class UserController(private val userServices: UserServices) {

    private val logger = KotlinLogging.logger {}

    @PostMapping(Uris.Users.USER)
    fun create(@RequestBody input: UserCreateInputModel): ResponseEntity<*>{
        logger.info { "POST: ${Uris.Users.USER} received" }

        return when(val res = userServices.createUser(input.username, input.password)) {
            is CreateUserInfo.UserCreated -> ResponseTemplate.Created(
                UserOutputModel(
                res.user.id.toString(),
                res.user.username
            ), "User ${input.username} created")
            is CreateUserInfo.UserAlreadyExists -> ResponseTemplate.Conflict("The provided username is already being used by another user.")
            is CreateUserInfo.UnsafePassword -> ResponseTemplate.BadRequest("Password is unsafe, it must contain capital letters, numbers, and have a length of at least 8 characters.")
        }
    }

    @PostMapping(Uris.Users.TOKEN)
    fun token(@RequestBody input: UserCreateInputModel): ResponseEntity<*>{
        logger.info { "POST: ${Uris.Users.TOKEN} received" }

        return when(val res = userServices.createUserToken(input.username, input.password)){
            is CreateUserTokenInfo.TokenCreated -> ResponseTemplate.Ok(
                TokenOutputModel(
                    res.user.id.toString(),
                    res.user.linked_email,
                    TokenInfo(
                        res.token.tokenvalidationinfo,
                        res.token.createdAt.toString(),
                        res.token.lastUsedAt.toString()
                )
                ), "Token generated")
            is CreateUserTokenInfo.AuthenticationFailed -> ResponseTemplate.Unauthorized("Authentication failed: Invalid password.")
        }
    }

    @GetMapping(Uris.Users.BY_ID)
    fun getById(@PathVariable id: String): ResponseEntity<*>{
        logger.info { "GET: ${Uris.Users.BY_ID} received" }

        return when (val res = userServices.getUserById(id)){
            is GetUserInfo.UserFound -> ResponseTemplate.Ok(
                UserOutputModel(
                res.user.id.toString(),
                    res.user.username
            ), "User ${res.user.username} found by ID")
            is GetUserInfo.UserNotFound -> ResponseTemplate.NotFound("There is no used with ID $id.")
            is GetUserInfo.AuthenticationFailed -> ResponseTemplate.Unauthorized("Authentication failed.")
            is GetUserInfo.InvalidID -> ResponseTemplate.BadRequest("The provided ID is invalid. Plase make sure the format is correct.")
        }
    }

    @PostMapping(Uris.Users.LINK)
    fun linkAccount(user: User, @RequestBody token: TokenInputModel): ResponseEntity<*>{
        logger.info { "POST: ${Uris.Users.LINK} received" }

        return when (userServices.createRefreshToken(user,token.token)){
            is CreateRefreshTokenInfo.TokenCreated -> ResponseTemplate.Ok(LinkingOutputModel(user.linked_email), "Account ${user.username} linked.")
            is CreateRefreshTokenInfo.TokenAlreadyExists -> ResponseTemplate.Conflict("User already has a linked account.")
            is CreateRefreshTokenInfo.InvalidToken -> ResponseTemplate.BadRequest("The provided Microsoft token is invalid.")
            is CreateRefreshTokenInfo.UnableToObtainUserInformation -> ResponseTemplate.InternalServerError("Something went wrong: Unable to obtain user information.")
        }
    }

    @DeleteMapping(Uris.Users.UNLINK)
    fun unlinkAccount(user: User): ResponseEntity<*>{
        logger.info { "DELETE: ${Uris.Users.UNLINK} received" }

        return when (userServices.removeRefreshToken(user)){
            is RemoveRefreshTokenInfo.AccountUnlinked -> ResponseTemplate.Ok(MessageOutputModel("Unlinked"), "Account ${user.username} unlinked.")
            is RemoveRefreshTokenInfo.AccountNotLinked -> ResponseTemplate.Conflict("Unable to remove account link: User is not linked to any Microsoft account.")
        }
    }
}