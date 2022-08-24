package com.example.marketplaceproject.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.marketplaceproject.R
import com.example.marketplaceproject.repository.Repository
import com.example.marketplaceproject.viewModels.forgotpassword.ForgotPasswordViewModel
import com.example.marketplaceproject.viewModels.forgotpassword.ForgotPasswordViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForgotPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgotPasswordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var usernameInput : EditText
    private lateinit var emailInput : EditText
    private lateinit var emailMeButton : Button
    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //itt letrehozom a viewmodelt
        val factory = ForgotPasswordViewModelFactory(this.requireContext(), Repository())
        forgotPasswordViewModel = ViewModelProvider(this,factory).get(ForgotPasswordViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_forgot_password, container, false)
        
        view?.apply { 
            initializeView(this)
            changeColorOfUnderline()
            initializeListeners(this)
        }

        forgotPasswordViewModel.code.observe(viewLifecycleOwner){
            //hogyha megkaptam a 200-as kodot,akkor atnavigalom egy masik fragmentbe,
            // ahol ertesiteni fogom a usert, hogy fog kapni egy email az uj
            // jelszoval kapcsolatosan

            findNavController().navigate(R.id.action_forgotPasswordFragment_to_afterForgotPasswordFragment)
        }

        return view
    }

    private fun initializeView(view: View) {
        usernameInput = view.findViewById(R.id.username_input)
        emailInput = view.findViewById(R.id.email_input)
        emailMeButton = view.findViewById(R.id.email_me_button)
    }

    private fun initializeListeners(view: View){
        emailMeButton.setOnClickListener {
            //hogyha a User ranyom az Email Me buttonre,akkor elkuldjuk a reset password kerest(POST valtozatat)
            forgotPasswordViewModel.user.value!!.username = usernameInput.text.toString()
            forgotPasswordViewModel.user.value!!.email = emailInput.text.toString()

            forgotPasswordViewModel.resetUserPassword()
        }
    }

    //forras: StackOverFlow
    private fun changeColorOfUnderline() {
        //ez a kicsi kod megnezi, hogy a telefonunk night modban van vagy sem
        //ha night modban van akkor a text color feher lesz (hogy konyebben lehessen latni), ellenkezo esetben fekete szin marad (forras : Stack)
        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES ->{
                usernameInput.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                emailInput.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            }
            Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED ->{
                usernameInput.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                emailInput.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForgotPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForgotPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}