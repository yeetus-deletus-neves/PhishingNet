package phishingnet.api.utils.graph.models

data class GraphMessageResponse(
    val value: List<GraphMessage>
)

data class GraphMessage(
    val id: String,
    val hasAttachments: Boolean,
    val subject: String,
    val importance: String,
    val isRead: Boolean,
    val isDraft: Boolean,
    val body: GraphBody,
    val sender: GraphSender,
    val from: GraphFrom
)

data class GraphBody(
    val content: String
)

data class GraphSender(
    val emailAddress: GraphEmailAddress
)

data class GraphFrom(
    val emailAddress: GraphEmailAddress
)

data class GraphEmailAddress(
    val name: String,
    val address: String
)