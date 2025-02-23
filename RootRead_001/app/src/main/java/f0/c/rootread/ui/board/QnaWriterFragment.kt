package f0.c.rootread.ui.board

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
import f0.c.rootread.ActiveMain
import f0.c.rootread.App
import f0.c.rootread.R
import f0.c.rootread.api.BoardAPI
import f0.c.rootread.model.board.BoardWriteRequest
import f0.c.rootread.ui.board.action.WriteQnA
import f0.c.rootread.utils.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class QnaWriterFragment : Fragment() {

    private lateinit var titleInput: EditText
    private lateinit var contentInput: EditText
    private lateinit var submitButton: Button
    private lateinit var cancelButton: Button
    private lateinit var checkSecret: CheckBox

    private var selectedFile: File? = null // ✅ 선택된 파일 저장

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
        cancelButton = view.findViewById(R.id.button_cancel)
        checkSecret = view.findViewById(R.id.check_secret)

        val app = requireActivity().application as App
        val boardAPI = app.retrofit.create(BoardAPI::class.java)
        val writeQnA = WriteQnA(boardAPI)


        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        submitButton.setOnClickListener {
            val title = titleInput.text.toString()
            val content = contentInput.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val filePart = selectedFile?.let { file ->
                    val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                    MultipartBody.Part.createFormData("qna_file", file.name, requestFile)
                }

                val boardWriteRequest = BoardWriteRequest(
                    title = title,
                    content = content,
                    writer = SessionManager.getUserID(requireContext()).toString(),
                    secret = checkSecret.isChecked
                )

                writeQnA.submitQnA(boardWriteRequest, selectedFile) { success ->
                    if (success) {
                        Toast.makeText(requireContext(), "Q&A 작성 완료", Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    } else {
                        Toast.makeText(requireContext(), "작성 실패", Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "제목과 내용을 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }

}