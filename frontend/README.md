# ScholarshipHub Frontend

獎學金申請管理系統前端程式碼

## 目錄結構

```
frontend/
├── index.html              # 首頁 (待建立)
├── css/
│   └── styles.css          # 共用樣式變數
├── js/
│   └── main.js             # 共用 JavaScript
├── components/             # 可重用組件
│   ├── header.html
│   └── footer.html
├── pages/
│   ├── auth/               # 認證頁面
│   │   └── login.html      # 登入/註冊
│   ├── student/            # 學生頁面
│   │   ├── profile.html    # 個人資料
│   │   ├── applications.html # 我的申請
│   │   └── recommendations.html # 推薦信請求
│   ├── teacher/            # 教師頁面
│   │   ├── profile.html    # 個人資料
│   │   └── manage-recommendations.html # 推薦管理
│   └── scholarship/        # 獎學金頁面
│       ├── list.html       # 獎學金列表
│       ├── detail.html     # 獎學金詳情
│       └── apply.html      # 申請表單
└── assets/
    └── images/             # 圖片資源
```

## 技術說明

| 技術 | 版本/來源 |
|------|----------|
| Tailwind CSS | CDN |
| Material Symbols | Google Fonts |
| 字型 | Lexend, Noto Sans TC |

## 本地預覽

直接在瀏覽器開啟 HTML 檔案即可預覽。

## 設計參考

原始設計稿位於 `c:\Users\User\Desktop\stitch_\` 各子目錄的 `screen.png`。
