package com.example.demo.utils.graph.models

data class GraphHeaders(
    val name:String,
    val value:String
)

data class ApiMessageHeadersResponse(
    val id:String,
    val internetMessageHeaders:List<GraphHeaders>
)
