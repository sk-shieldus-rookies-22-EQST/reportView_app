package f0.c.rootread.model.board

import okhttp3.MultipartBody

data class BoardWriteRequest(
    val title: String,
    val content: String,
    val writer: String,
    val qna_file: MultipartBody.Part? = null, // ✅ 기본값을 null로 설정
    val secret: Boolean
)

