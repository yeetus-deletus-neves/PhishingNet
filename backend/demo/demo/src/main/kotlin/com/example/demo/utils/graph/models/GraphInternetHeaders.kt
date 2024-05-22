package com.example.demo.utils.graph.models

data class GraphInternetHeaders (
    val internetMessageHeaders: List<GraphHeader>
)

data class GraphHeader(
    val name: String,
    val value: String
)