package com.example.views

import kotlinx.html.*

fun FlowContent.invoiceSearchForm() {
    h1 { +"請求書の支払期日の期間を入力してください。" }
    form(action = "/invoice/list", method = FormMethod.post) {
        p {
            label { +"開始日時" }
            dateTimeLocalInput(name = "dateFrom") {
                required = true
            }
        }
        p {
            label { +"終了日時" }
            dateTimeLocalInput(name = "dateTo") {
                required = true
            }
        }
        p {
            submitInput { value = "検索" }
        }
    }
}
