package f0.c.rootread.model.purchase

data class EachCartItem(
    val cart_id: Long,
    val book_id: Long,
    val title: String,
    val price: Int,
    val book_img_path: String
)
