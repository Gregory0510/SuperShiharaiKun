CREATE TABLE users ( 
    user_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, -- ユーザーID 
    company_name VARCHAR(255) NOT NULL, -- 企業名 
    name VARCHAR(255) NOT NULL, -- 氏名 
    email VARCHAR(255) UNIQUE NOT NULL, -- メールアドレス 
    password VARCHAR(255) NOT NULL, -- パスワード 
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 作成日時 
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 更新日時 
);

CREATE TABLE invoices (
    invoice_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, -- 請求書ID
    user_id BIGINT UNSIGNED NOT NULL, -- 企業ID
    issue_date DATE NOT NULL, -- 発行日
    payment_amount DECIMAL(15, 2) NOT NULL, -- 支払金額
    fee DECIMAL(15, 2) NOT NULL, -- 手数料
    fee_rate DECIMAL(5, 2) NOT NULL, -- 手数料率
    tax_amount DECIMAL(15, 2) NOT NULL, -- 消費税
    tax_rate DECIMAL(5, 2) NOT NULL, -- 消費税率
    total_amount DECIMAL(15, 2) NOT NULL, -- 請求金額
    payment_due_date DATE NOT NULL, -- 支払期日
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 作成日時
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新日時

    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);