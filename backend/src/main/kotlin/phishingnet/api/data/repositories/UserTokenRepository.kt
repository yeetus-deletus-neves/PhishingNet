package phishingnet.api.data.repositories

import phishingnet.api.data.entities.User
import phishingnet.api.data.entities.UserToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserTokenRepository: JpaRepository<UserToken, String> {

    fun findUserTokenByTokenvalidationinfo(userToken: String): UserToken?
    fun findTopByUseridOrderByLastUsedAtAsc(user: User): UserToken?
    fun countUserTokensByUserid(user: User): Int
    fun removeTopByTokenvalidationinfoOrderByLastUsedAtAsc(userToken: String)
}