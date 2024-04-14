package com.example.demo.data.entities

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "refreshtoken")
open class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('dbo.refreshtoken_id_seq'")
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid", nullable = false)
    open var userid: User? = null

    @Column(name = "rtoken", length = 256)
    open var rtoken: String? = null
}