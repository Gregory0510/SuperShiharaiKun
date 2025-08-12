import com.example.module
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Test

class InvoicesTest {

    @Test
    // WEB用の請求書登録画面-getのテスト
    fun testGetInvoice() = testApplication {
        application {
            module()
        }
        val response = client.get("/invoice")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("html", response.contentType()?.contentSubtype)
    }

    @Test
    // WEB用の請求書検索画面-getのテスト
    fun testGetInvoiceList() = testApplication {
        application {
            module()
        }
        val response = client.get("/invoice/list")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("html", response.contentType()?.contentSubtype)
    }
}