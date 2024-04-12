package com.example.demo.http

import org.springframework.web.util.UriTemplate

object Uris {

    object Users {
        const val ME = "/me"
        const val USER = "/user"
        const val BY_ID = "/user/{id}"
        const val TOKEN = "/user/signIn"

        fun byId(id: String) = UriTemplate(BY_ID).expand(id)
    }
}