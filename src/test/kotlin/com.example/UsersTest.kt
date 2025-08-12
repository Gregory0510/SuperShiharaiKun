import com.example.module
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Test

class UsersTest {

    @Test
    // WEB用のユーザー登録画面-getのテスト
    fun testGetProfile() = testApplication {
        application {
            module()
        }
        val response = client.get("/profile")
        println("response1: " + response)
        val contentType = response.contentType()
        println("Response status: ${response.status}")
        println("Response Content-Type: $contentType")
        println("Response body: ${response.bodyAsText()}")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("html", response.contentType()?.contentSubtype)
    }

    @Test
    // WEB用のユーザ登録-postのテスト
    fun testPostProfile() = testApplication {
        application {
            module()
        }

        val response = client.post("/profile") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                listOf(
                    "companyName" to "Test Company",
                    "name" to "Test User",
                    "email" to "test@example.com",
                    "password" to "password123",
                    "passwordConfirm" to "password123"
                ).formUrlEncode()
            )
        }

        println("Response status: ${response.status}")
        println("Response Content-Type: ${response.contentType()}")
        println("Response body: ${response.bodyAsText()}")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("text/html; charset=UTF-8", response.contentType().toString())
    }

    @Test
    // WEB用のユーザー一覧-getのテスト
    fun testGetUsers() = testApplication {
        application {
            module()
        }
        val response = client.get("/users")
        println("response1: " + response)
        val contentType = response.contentType()
        println("Response status: ${response.status}")
        println("Response Content-Type: $contentType")
        println("Response body: ${response.bodyAsText()}")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("html", response.contentType()?.contentSubtype)
    }

}