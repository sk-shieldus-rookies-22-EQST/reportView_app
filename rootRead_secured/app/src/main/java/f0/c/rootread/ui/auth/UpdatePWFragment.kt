package f0.c.rootread.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import f0.c.rootread.App
import f0.c.rootread.R
import f0.c.rootread.api.AuthAPI
import f0.c.rootread.model.auth.FindPWRequest
import f0.c.rootread.ui.auth.action.FindPWAction

class UpdatePWFragment: Fragment(), View.OnClickListener {

    private lateinit var findPWID: TextView
    private lateinit var findPWpasswd: EditText
    private lateinit var findPWrepasswd: EditText
    private lateinit var findPWButton: Button

    private var userID = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.find_pw, container, false)
        userID = arguments?.getString("user_id") ?: ""

        findPWID = view.findViewById(R.id.user_id)
        findPWpasswd = view.findViewById(R.id.find_pw)
        findPWrepasswd = view.findViewById(R.id.find_pw_passwd)
        findPWButton = view.findViewById(R.id.find_pw_button)

        findPWID.text = userID

        findPWButton.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        val userPhone = findPWpasswd.text.toString()

        if (findPWpasswd.text.toString() != findPWrepasswd.text.toString()) {
            Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val findPWRequest = FindPWRequest(
            user_id = userID,
            new_user_pw = userPhone,
        )

        val app = requireActivity().application as App
        val findPWApi = app.retrofit.create(AuthAPI::class.java)

        val findPWAction = FindPWAction(requireContext(), findPWApi)
        findPWAction.doFindPW(findPWRequest) { data ->
            if (isAdded) {
                if (data != null){
                    // pw 변경 로직 추가
                    findNavController().navigate(R.id.action_findPWFragment_to_loginFragment)
                } else {
                    Log.e("PurchaseFragment", "Fragment is not attached to a context while loading data.")
                }
            }
        }
    }
}