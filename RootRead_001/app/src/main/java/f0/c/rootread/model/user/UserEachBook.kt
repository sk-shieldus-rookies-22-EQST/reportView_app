package f0.c.rootread.model.user

data class UserEachBook(
    val book_id : Long,
    val title : String,
    val writer : String,
    val book_img_path : String ?= null
)
