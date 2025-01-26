package com.example.reportview_003.ui.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reportview_003.ActiveMain
import com.example.reportview_003.App
import com.example.reportview_003.R
import com.example.reportview_003.api.BoardAPI
import com.example.reportview_003.ui.board.action.WriteQnA
import com.example.reportview_003.utils.SessionManager

class QnaWriterFragment : Fragment() {

    private lateinit var titleInput: EditText
    private lateinit var contentInput: EditText
    private lateinit var submitButton: Button
    private lateinit var cancelButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 로그인 상태에 따라 페이지 이동 
        if (!SessionManager.isLoggedIn(requireContext())) {
            Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.listFragment) // 루트 페이지로 이동

            (activity as? ActiveMain)?.apply {
                navigationView.setCheckedItem(R.id.listFragment)
            }
            return null
        }

        val view = inflater.inflate(R.layout.qna_write_main, container, false)

        titleInput = view.findViewById(R.id.edit_text_title)
        contentInput = view.findViewById(R.id.edit_text_content)
        submitButton = view.findViewById(R.id.button_submit)

        val app = requireActivity().application as App
        val boardAPI = app.retrofit.create(BoardAPI::class.java)
        val writeQnA = WriteQnA(requireContext(), boardAPI)

        submitButton.setOnClickListener {
            val title = titleInput.text.toString()
            val content = contentInput.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                writeQnA.submitQnA(title, content) { success ->
                    if (success) {
                        Toast.makeText(requireContext(), "Q&A 작성 완료", Toast.LENGTH_SHORT).show()
                        requireActivity()
                    } else {
                        Toast.makeText(requireContext(), "작성 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "제목과 내용을 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}