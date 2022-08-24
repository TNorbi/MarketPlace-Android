package com.example.marketplaceproject.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.marketplaceproject.R
import com.example.marketplaceproject.repository.Repository
import com.example.marketplaceproject.viewModels.register.RegisterViewModel
import com.example.marketplaceproject.viewModels.register.RegisterViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentRegister.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentRegister : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var title: TextView
    private lateinit var registerButtomText: TextView
    private lateinit var usernameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var registerButton: Button
    private lateinit var loginHiperlink: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //itt letrehozom a registerViewModelt!
        val factory = RegisterViewModelFactory(this.requireContext(), Repository())
        registerViewModel =
            ViewModelProvider(requireActivity(), factory).get(RegisterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        view?.apply {
            initializeView(this)
            changeColorOfTextView()
            initializeListeners(this)
        }

        registerViewModel.registerResponse.observe(viewLifecycleOwner) {
            Log.d("xxx", "navigate to after registration fragment")
            findNavController().navigate(R.id.action_fragmentRegister_to_afterRegisterFragment)
        }

        return view
    }

    private fun initializeListeners(view: View) {
        loginHiperlink.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentRegister_to_fragmentLogIn)
        }

        registerButton.setOnClickListener {
            //meg kell nezzem, ha a User minden egyes adatot megadott!

            //----------------------ellenorizzuk az email cimet, ha a User jol adta meg vagy sem----------------------

            if (usernameInput.text.isEmpty() || emailInput.text.isEmpty() || passwordInput.text.isEmpty()) {
                Toast.makeText(
                    context,
                    "Username,email or password is missing!",
                    Toast.LENGTH_LONG
                ).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput.text).matches()) {
                //megnezzuk, hogyha egy ervenyes email cimet adott meg a User
                //abban az esetben, ha nem ervenyes az email cim,akkor nem regisztraljuk a User adatait
                //es szolni fogunk a Usernek, hogy adjon meg egy ervenyes email cimet!

                Toast.makeText(
                    context,
                    "Please enter valid email address!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                registerViewModel.user.value.let {
                    if (it != null) {
                        it.username = usernameInput.text.toString()
                    }

                    if (it != null) {
                        it.email = emailInput.text.toString()
                    }

                    if (it != null) {
                        it.password = passwordInput.text.toString()
                    }
                }

                registerViewModel.register()
            }
        }
    }

    private fun initializeView(view: View) {
        title = view.findViewById(R.id.registerTitle)
        registerButtomText = view.findViewById(R.id.register_bottom_text)
        loginHiperlink = view.findViewById(R.id.register_login_hiperlink)
        usernameInput = view.findViewById(R.id.login_username_input)
        emailInput = view.findViewById(R.id.register_email_input)
        passwordInput = view.findViewById(R.id.register_password_input)
        registerButton = view.findViewById(R.id.register_button)
    }

    //forras: StackOverFlow
    private fun changeColorOfTextView() {
        //ez a kicsi kod megnezi, hogy a telefonunk night modban van vagy sem
        //ha night modban van akkor a text color feher lesz (hogy konyebben lehessen latni), ellenkezo esetben fekete szin marad (forras : Stack)
        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                usernameInput.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                emailInput.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                passwordInput.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            }

            Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                usernameInput.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                emailInput.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                passwordInput.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
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
         * @return A new instance of fragment FragmentRegister.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentRegister().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}