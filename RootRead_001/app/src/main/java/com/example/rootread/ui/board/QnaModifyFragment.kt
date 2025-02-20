package com.example.rootread.ui.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rootread.App
import com.example.rootread.R
import com.example.rootread.api.BoardAPI
import com.example.rootread.model.board.BoardModifyRequest
import com.example.rootread.ui.board.action.UpdateQnA
import com.example.rootread.utils.SessionManager

class QnaModifyFragment : Fragment() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText
    private lateinit var buttonSubmit: Button
    private lateinit var buttonCancel: Button
    private lateinit var checkSecret: CheckBox
    private var qnaId: Long = -1 // 수정할 글의 ID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 로그인 상태에 따라 페이지 이동
        if (!SessionManager.isLoggedIn(requireContext())) {
            Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()

            findNavController().popBackStack(R.id.listFragment,true)
            findNavController().navigate(R.id.listFragment) // 루트 페이지로 이동

            return null
        }

        val view = inflater.inflate(R.layout.qna_write_main, container, false)

        val app = requireActivity().application as App
        val boardAPI = app.retrofit.create(BoardAPI::class.java)

        editTextTitle = view.findViewById(R.id.edit_text_title)
        editTextContent = view.findViewById(R.id.edit_text_content)
        buttonSubmit = view.findViewById(R.id.button_submit)
        buttonCancel = view.findViewById(R.id.button_cancel)
        checkSecret = view.findViewById(R.id.check_secret)

        // 기존 데이터를 전달받아 초기화
        arguments?.let {
            qnaId = it.getLong("qna_id", -1)
            editTextTitle.setText(it.getString("title", ""))
            editTextContent.setText(it.getString("content", ""))
            checkSecret.isChecked = it.getBoolean("secret")
        }

        buttonSubmit.text = "수정 완료"

        // 수정 동작 처리
        buttonSubmit.setOnClickListener {
            val title = editTextTitle.text.toString()
            val content = editTextContent.text.toString()
            val secretState = checkSecret.isChecked

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val updateQnA = UpdateQnA(requireContext(), boardAPI)
                val boardModifyResponse = BoardModifyRequest(
                    qna_id = qnaId,
                    writer = SessionManager.getUserID(requireContext()).toString(),
                    title = title,
                    content = content,
                    secret = secretState
                )

                updateQnA.updateComment(boardModifyResponse) { success ->
                    if (success.status) {
                        Toast.makeText(requireContext(), "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(requireContext(), "수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 취소 버튼 동작
        buttonCancel.setOnClickListener {
            requireActivity()
        }

        return view
    }
}