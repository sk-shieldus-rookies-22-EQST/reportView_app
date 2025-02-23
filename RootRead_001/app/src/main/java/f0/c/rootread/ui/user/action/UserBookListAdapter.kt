package f0.c.rootread.ui.user.action

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import f0.c.rootread.R
import f0.c.rootread.model.user.UserEachBook

class UserBookListAdapter(
    private val context: Context,
    private val bookList: MutableList<UserEachBook>,
) : RecyclerView.Adapter<UserBookListAdapter.UserBookViewHolder>() {

    inner class UserBookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookImage: ImageView = view.findViewById(R.id.user_book_img)
        val bookTitle: TextView = view.findViewById(R.id.user_book_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserBookViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_book_list, parent, false)
        return UserBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserBookViewHolder, position: Int) {
        val item = bookList[position]
        holder.bookTitle.text = item.title as? String ?: "Unknown Title"

        // 클릭 이벤트 설정
        holder.itemView.setOnClickListener {
            val fileOpenAction = FileOpenAction(context)
            fileOpenAction.openFile(
                bookId = (item.book_id as? Number)?.toLong() ?: -1L
            )
        }

        val IMG_PATH = "https://3.35.84.46" + item.book_img_path
//        .load(item["book_img_path"] as? String ?: "")
        // 이미지를 URL에서 불러오려면 Glide 또는 Picasso 사용
        Glide.with(context)
            .load(IMG_PATH)
            .placeholder(R.drawable.root_read_logo)
            .into(holder.bookImage)


    }

    override fun getItemCount(): Int = bookList.size
}