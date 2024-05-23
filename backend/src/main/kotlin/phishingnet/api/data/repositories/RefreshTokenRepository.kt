package phishingnet.api.data.repositories

import phishingnet.api.data.entities.RefreshToken
import phishingnet.api.data.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, Int>{

    fun findRefreshTokensByUserid(user: User): RefreshToken?
}