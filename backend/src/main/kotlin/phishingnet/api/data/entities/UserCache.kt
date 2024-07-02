package phishingnet.api.data.entities

import jakarta.persistence.*
import java.io.Serializable
import java.util.UUID

@Embeddable
data class CacheID(
    @JoinColumn(name = "id", nullable = false)
    val id:UUID,
    val conversationid: String
):Serializable


@Entity
@IdClass(CacheID::class)
@Table(name = "usercache", schema = "dbo")
open class UserCache(cache:CacheID,threat_json:String) {

    @Id
    @Column(name = "id", nullable = false)
    open var id:UUID = cache.id

    @Id
    @Column(name = "conversation_id", nullable = false)
    open var conversationid:String = cache.conversationid

    @Column(name = "threat", nullable = false)
    open var threat:String = threat_json
}


