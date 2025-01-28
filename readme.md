# 앱 개요

# 앱 동작

# 개인 별 설정 

- \[변경] 부분을 내 PC의 계정 명으로 변경하면 됨
```text
local.properties
# 현재 PC 사용자 이름
sdk.dir=C\:\\Users\\[변경]\\AppData\\Local\\Android\\Sdk

-------------------------------------------------------------
App.kt
# 테스트용 서버 동작시키는 주소
retrofit = Retrofit.Builder()
            .baseUrl("http://[변경]:8000/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

-------------------------------------------------------------
network_security_config.xml
# 테스트용 서버 동작시키는 주소
<domain includeSubdomains="true">[변경]</domain>

```