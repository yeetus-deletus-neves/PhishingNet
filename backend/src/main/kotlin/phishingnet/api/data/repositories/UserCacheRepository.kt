package phishingnet.api.data.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import phishingnet.api.data.entities.UserCache
import java.util.*

@Transactional
@Repository
interface UserCacheRepository: JpaRepository<UserCache,UUID> {
    fun findUserCacheByIdAndConversationid(id: UUID, conversationid: String):UserCache?
    fun countUserCachesById(id: UUID):Int
    fun getFirstById(userCacheID: UUID): UserCache?
    fun removeByIdAndConversationid(userCacheID: UUID, conversationid: String)
}