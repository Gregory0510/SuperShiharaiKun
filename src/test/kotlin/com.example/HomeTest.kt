import com.example.module
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeTest {

    @Test
    // ホーム画面の表示テスト
    fun testHomePage() = testApplication {
        application {
            module()
        }

        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("html", response.contentType()?.contentSubtype)
    }
}