package phishingnet.api.utils.graph.models

data class GraphEmailDetails (
    val messageInfo: GraphMessage,
    val headers: GraphInternetHeaders,
)