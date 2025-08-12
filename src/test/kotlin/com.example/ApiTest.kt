import com.example.module
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertTrue

class ApiTest {

    @Test
    // API用のログイン-postのテスト
    fun testPostApiLogin() = testApplication {
        application {
            module()
        }

        val response = client.post("/api/login") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                listOf(
                    "email" to "dsdsd@gmail.com",
                    "password" to "password"
                ).formUrlEncode()
            )
        }

        println("Response status: ${response.status}")
        println("Response Content-Type: ${response.contentType()}")
        println("Response body: ${response.bodyAsText()}")

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("\"token\": "))
    }
}