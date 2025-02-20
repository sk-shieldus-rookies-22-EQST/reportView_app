package com.example.rootread.ui.board.action

import com.example.rootread.api.BoardAPI
import com.example.rootread.model.board.BoardWriteRequest
import com.example.rootread.repository.BoardRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class WriteQnA(
    private val boardAPI: BoardAPI
) {
    fun submitQnA(boardWriteRequest: BoardWriteRequest, file: File?, callback: (Boolean) -> Unit) {
        val boardRepository = BoardRepository(boardAPI)

        // ✅ 문자열 데이터를 `RequestBody`로 변환
        val titlePart = RequestBody.create("text/plain".toMediaTypeOrNull(), boardWriteRequest.title)
        val contentPart = RequestBody.create("text/plain".toMediaTypeOrNull(), boardWriteRequest.content)
        val writerPart = RequestBody.create("text/plain".toMediaTypeOrNull(), boardWriteRequest.writer)

        // ✅ 파일을 `MultipartBody.Part`로 변환 (파일이 있는 경우만)
        val filePart: MultipartBody.Part? = file?.let {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), it)
            MultipartBody.Part.createFormData("qna_file", it.name, requestFile)
        }

        if (filePart == null) {

            boardRepository.writeQnA(boardWriteRequest) { response, error ->
                if (response != null) {
                    callback(response.status)
                } else {
                    error?.printStackTrace()
                    callback(false)
                }
            }
        } else {
            boardRepository.writeQnAMultipart(titlePart, contentPart, writerPart, filePart) { response, error ->
                if (response != null) {
                    callback(true)
                } else {
                    error?.printStackTrace()
                    callback(false)
                }
            }
        }

    }



}


