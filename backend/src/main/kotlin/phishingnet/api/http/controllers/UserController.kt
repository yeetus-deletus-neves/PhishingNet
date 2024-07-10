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
    fun create(@RequestBody input: UserCreateInputModel): ResponseEntity<*> {
        logger.info { "POST: ${Uris.Users.USER} recebido" }

        return when (val res = userServices.createUser(input.username, input.password)) {
            is CreateUserInfo.UserCreated -> ResponseTemplate.Created(
                UserOutputModel(
                    res.user.id.toString(),
                    res.user.username
                ), "User ${input.username} created"
            )

            is CreateUserInfo.UserAlreadyExists ->
                ResponseTemplate.Conflict("O username fornecido já está a ser utilizado por outro utilizador.")

            is CreateUserInfo.UnsafePassword ->
                ResponseTemplate.BadRequest(
                    "A palavra-passe não é segura, deve conter letras maiúsculas, números e ter pelo menos 8 caracteres."
                )
        }
    }

    @PostMapping(Uris.Users.TOKEN)
    fun token(@RequestBody input: UserCreateInputModel): ResponseEntity<*> {
        logger.info { "POST: ${Uris.Users.TOKEN} recebido" }

        return when (val res = userServices.createUserToken(input.username, input.password)) {
            is CreateUserTokenInfo.TokenCreated -> ResponseTemplate.Ok(
                TokenOutputModel(
                    res.user.id.toString(),
                    res.user.linked_email,
                    TokenInfo(
                        res.token.tokenvalidationinfo,
                        res.token.createdAt.toString(),
                        res.token.lastUsedAt.toString()
                    )
                ), "Token gerado"
            )

            is CreateUserTokenInfo.AuthenticationFailed ->
                ResponseTemplate.Unauthorized("Falha na autenticação: palavra-passe inválida.")
        }
    }

    @GetMapping(Uris.Users.BY_ID)
    fun getById(@PathVariable id: String): ResponseEntity<*> {
        logger.info { "GET: ${Uris.Users.BY_ID} recebido" }

        return when (val res = userServices.getUserById(id)) {
            is GetUserInfo.UserFound -> ResponseTemplate.Ok(
                UserOutputModel(
                    res.user.id.toString(),
                    res.user.username
                ), "Utilizador ${res.user.username} encontrado por ID"
            )

            is GetUserInfo.UserNotFound -> ResponseTemplate.NotFound(
                "Não existe nenhum utilizador com este ID $id."
            )

            is GetUserInfo.AuthenticationFailed -> ResponseTemplate.Unauthorized("Falha na autenticação.")
            is GetUserInfo.InvalidID -> ResponseTemplate.BadRequest(
                "O ID fornecido é inválido. Por favor, certifique-se de que o formato está correto."
            )
        }
    }

    @PostMapping(Uris.Users.LINK)
    fun linkAccount(user: User, @RequestBody token: TokenInputModel): ResponseEntity<*> {
        logger.info { "POST: ${Uris.Users.LINK} recebido" }

        return when (userServices.createRefreshToken(user, token.token)) {
            is CreateRefreshTokenInfo.TokenCreated -> ResponseTemplate.Ok(
                LinkingOutputModel(user.linked_email),
                "Conta ${user.username} vinculada."
            )

            is CreateRefreshTokenInfo.TokenAlreadyExists -> ResponseTemplate.Conflict(
                "O utilizador já possui uma conta vinculada."
            )

            is CreateRefreshTokenInfo.InvalidToken -> ResponseTemplate.BadRequest(
                "O token Microsoft fornecido é inválido."
            )

            is CreateRefreshTokenInfo.UnableToObtainUserInformation -> ResponseTemplate.InternalServerError(
                "Algo correu mal: não foi possível obter as informações do utilizador."
            )
        }
    }

    @DeleteMapping(Uris.Users.UNLINK)
    fun unlinkAccount(user: User): ResponseEntity<*> {
        logger.info { "DELETE: ${Uris.Users.UNLINK} recebido" }

        return when (userServices.removeRefreshToken(user)) {
            is RemoveRefreshTokenInfo.AccountUnlinked -> ResponseTemplate.Ok(
                MessageOutputModel("Unlinked"),
                "Conta ${user.username} desvinculada."
            )

            is RemoveRefreshTokenInfo.AccountNotLinked -> ResponseTemplate.Conflict(
                "Não foi possível remover a vinculação da conta com a Microsoft: " +
                        "o utilizador não se encontra vinculado a nenhuma conta Microsoft."
            )
        }
    }
}