package f0.c.rootread.model.view

data class EachBook(
    val book_id : Long = 0L,
    val title : String = "Non Title",
    val price : Int = 0,
    val writer : String = "Non Writer",
    val write_date : String = "Non Date",
    val book_img_path : String ?= null,
)
