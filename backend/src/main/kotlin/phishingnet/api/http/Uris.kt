package phishingnet.api.http

import org.springframework.web.util.UriTemplate

object Uris {

    object Users {
        const val ME = "/me"
        const val USER = "/user"
        const val BY_ID = "/user/{id}"
        const val TOKEN = "/user/signIn"
        const val LINK = "/user/link"
        const val UNLINK = "/user/unlink"

        fun byId(id: String) = UriTemplate(BY_ID).expand(id)
    }

    object Analysis {
        const val ANALYSE = "/analyse"
    }

}