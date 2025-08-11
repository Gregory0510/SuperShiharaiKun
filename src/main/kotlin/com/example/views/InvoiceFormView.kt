package com.example.views

import kotlinx.html.*

fun HTML.invoiceForm() {
    head {
        title { +"Invoice Form" }
    }
    body {
        h1 { +"請求情報を入力してください。" }
        form(action = "/web/invoice", method = FormMethod.post) {
            p {
                label { +"支払金額" }
                numberInput(name = "paymentAmount") {
                    min = "0"
                    value = "10000"
                }
            }
            p {
                label { +"支払期日" }
                dateInput(name = "paymentDueDate") {
                    required = true
                }
            }
            p {
                submitInput { value = "登録" }
            }
        }
    }
}
