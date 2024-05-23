package phishingnet.api.data.entities

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "refreshtoken", schema = "dbo")
open class RefreshToken (owner: User, token: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('dbo.refreshtoken_id_seq'")
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "userid", nullable = false)
    open var userid: User = owner

    @Column(name = "rtoken", length = 256, nullable = false)
    open var rtoken: String = token
}