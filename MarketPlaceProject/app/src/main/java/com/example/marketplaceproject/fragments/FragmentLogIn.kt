package com.example.marketplaceproject.fragments

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.example.marketplaceproject.viewModels.login.LoginViewModel
import com.example.marketplaceproject.viewModels.login.LoginViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentLogIn.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentLogIn : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var title : TextView
    private lateinit var usernameInput : EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var loginViewModel : LoginViewModel
    private lateinit var forgotPasswordLabel : TextView
    private lateinit var forgotPasswordLink : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //itt letrehozom a login viewModeljet!
        val factory = LoginViewModelFactory(this.requireContext(), Repository()) //factory altal peldanyositani fogom a loginViewModelt!
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java) //itt fog lenyegeben letrejonni a loginViewModel!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log_in, container, false)

        view?.apply {
            initializeView(this)
            changeColorOfUnderline()
            initializeListeners(this)
        }

        //itt csakis akkor fogjuk latni a main screent (Productok listajat) amikor kapunk tokent!
        //vagyis ha a token erteke(ami a viewModelben van) frissul csakis akkor fogunk atugorni loginrol Product Listbe!
        loginViewModel.token.observe(viewLifecycleOwner){
            Log.d("xxx", "navigate to list")
            findNavController().navigate(R.id.action_fragmentLogIn_to_timelineFragment)
        }

        return view
    }

    private fun initializeListeners(view: View) {
        loginButton.setOnClickListener{
            //itt meg kell hivjam majd a viewModelben a login Post kereset(coroutine hivas)

            //hogyha a User nem adta meg a username-t vagy jelszavat
            //akkor emlekeztesse a Usert, hogy meg kell adja ezeket(ez esetben nem fog elvegzodni a login muvelet)!
            if(usernameInput.text.isEmpty()){
                Toast.makeText(context,"Username field is empty!",Toast.LENGTH_LONG).show()
            }
            else if(passwordInput.text.isEmpty()){
                Toast.makeText(context,"Password field is empty!",Toast.LENGTH_LONG).show()
            }
            else{
                //itt a loginViewModelnek atadom a username-et, illetve a passwordot, hogy majd felhasznalhassam ezt a login-nal!
                loginViewModel.user.value.let {
                    if (it != null) {
                        it.username = usernameInput.text.toString() //eltarolom a username-et loginra a viewModelben!
                    }

                    if (it != null) {
                        it.password = passwordInput.text.toString() //atadom a viewModelnek a passwordot
                    }
                }

                //itt meghivom a login fuggvenyt(maga a coroutine hivas maga a viewModelben lesz es nem a fragmentben!)
                loginViewModel.login()
            }
        }

        registerButton.setOnClickListener {
            //ha a User ranyom a Sign Up gombra,akkor atnavigalja a Register reszre
            findNavController().navigate(R.id.action_fragmentLogIn_to_fragmentRegister)
        }

        forgotPasswordLink.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentLogIn_to_forgotPasswordFragment)
        }
    }

    private fun initializeView(view: View) {
        title = view.findViewById(R.id.logInTitle)
        usernameInput = view.findViewById(R.id.login_username_input)
        passwordInput = view.findViewById(R.id.passwordInput)
        loginButton  = view.findViewById(R.id.logInButton)
        registerButton = view.findViewById(R.id.login_registerButton)
        forgotPasswordLabel = view.findViewById(R.id.login_forgot_password_label)
        forgotPasswordLink = view.findViewById(R.id.login_forgot_password_hiperlink)
    }

    private fun changeColorOfUnderline() {
        //ez a kicsi kod megnezi, hogy a telefonunk night modban van vagy sem
        //ha night modban van akkor a text color feher lesz (hogy konyebben lehessen latni), ellenkezo esetben fekete szin marad (forras : Stack)
        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES ->{
                usernameInput.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
                passwordInput.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            }
            Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED ->{
                usernameInput.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
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
         * @return A new instance of fragment FragmentLogIn.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentLogIn().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}