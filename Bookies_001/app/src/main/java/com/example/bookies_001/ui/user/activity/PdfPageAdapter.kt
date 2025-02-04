package com.example.bookies_001.ui.user.activity

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookies_001.R

class PdfPagerAdapter(
    private val pdfRenderer: PdfRenderer,
    private val pageCount: Int
) : RecyclerView.Adapter<PdfPagerAdapter.PdfPageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfPageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pdf_page_item, parent, false)
        return PdfPageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PdfPageViewHolder, position: Int) {
        val page = pdfRenderer.openPage(position)
        val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page.close()

        holder.imageView.setImageBitmap(bitmap)
        Log.d("PdfPagerAdapter", "Rendered page: $position")
    }

    override fun getItemCount(): Int = pageCount

    class PdfPageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.pdfPageImageView)
    }
}
