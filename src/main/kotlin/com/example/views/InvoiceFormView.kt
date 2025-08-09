package com.example.views

import kotlinx.html.*

fun FlowContent.invoiceForm() {
    h1 { +"請求情報を入力してください。" }
    form(action = "/invoice", method = FormMethod.post) {
        p {
            label { +"会社名: " }
            textInput(name = "company")
        }
        p {
            label { +"管理者: " }
            textInput(name = "personInCharge")
        }
        p {
            label { +"ローン金額: " }
            numberInput(name = "loanAmount") {
                min = "0"
            }
        }
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
            label { +"支払期日" }
            dateTimeLocalInput(name = "dueDate") {
                required = true
            }
        }
        p {
            submitInput { value = "登録" }
        }
    }
}
