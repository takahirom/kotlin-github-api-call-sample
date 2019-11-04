import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.header
import io.ktor.client.request.post
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class Issue(
    val title: String,
    val body: String,
    val labels: List<String> = listOf()
)

suspend fun main() {
    val client = HttpClient(OkHttp)

    val issues = listOf(
        Issue(
            title = "title",
            body = "body",
            labels = listOf("bug")
        )
    )

    issues.forEach { issue ->
        val body = Json.nonstrict.stringify(
            Issue.serializer(),
            issue
        )
        client.post<Any>("https://api.github.com/repos/takahirom/kotlin-github-api-call-sample/issues") {
            this.body = body
            header("Authorization", "token ${System.getenv("GITHUB_API_KEY")}")
        }
    }
}
