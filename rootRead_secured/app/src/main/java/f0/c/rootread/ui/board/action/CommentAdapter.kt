package f0.c.rootread.ui.board.action

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import f0.c.rootread.R
import f0.c.rootread.model.board.Comment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField

class CommentAdapter(
    private val context: Context,
    private val comments: MutableList<Comment>
    ): BaseAdapter() {

    override fun getCount(): Int = comments.size

    override fun getItem(position: Int): Comment = comments[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.qna_detail_comment, parent, false)

        val createdAt: TextView = view.findViewById(R.id.qna_re_created_at)
        val contentTextView: TextView = view.findViewById(R.id.qna_re_content)

        val item = getItem(position)
        val createdAtText = item.qna_re_created_at.toString() as? String ?: "No Created At"
        val contentText = item.qna_re_content as? String ?: "No Content"

        // LocalDateTime 입력 포맷: "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
        val inputFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .appendFraction(ChronoField.MICRO_OF_SECOND, 5, 6, true) // 최소 5자리, 최대 6자리 허용
            .toFormatter()
        // 출력 포맷: "yyyy-MM-dd"
        val outputFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        val formattedDate = try {
            val parsedDate = LocalDateTime.parse(createdAtText, inputFormatter)
            parsedDate.format(outputFormatter)
        } catch (e: DateTimeParseException) {
            Log.e("DateFormatter", "Date parsing error: ${e.message}")
            "날짜 없음"
        }
        createdAt.text = formattedDate

        contentTextView.text = contentText

        return view
    }
}