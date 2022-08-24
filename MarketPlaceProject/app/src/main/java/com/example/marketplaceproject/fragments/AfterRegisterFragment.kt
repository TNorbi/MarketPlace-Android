package com.example.marketplaceproject.fragments

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.marketplaceproject.R
import com.example.marketplaceproject.repository.Repository
import com.example.marketplaceproject.viewModels.register.RegisterViewModel
import com.example.marketplaceproject.viewModels.register.RegisterViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AfterRegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AfterRegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var afterRegisterTitle: TextView
    private lateinit var activationTextView: TextView
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var loginHiperlink : TextView

    //itt letre kell hozzam a registration viewModeljet(peldanyositani)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //itt letrehozom a registerViewModelt!

        val factory = RegisterViewModelFactory(this.requireContext(), Repository())
        registerViewModel = ViewModelProvider(requireActivity(),factory).get(RegisterViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_after_register, container, false)

        view?.apply {
            initializeView(this)
            initializeListeners(this)
        }


        registerViewModel.activateResponse.observe(viewLifecycleOwner){
            //hogyha visszakapjuk a HTML kodot,akkor azt jelenti, hogy sikeresen aktivalva lett a user!
            Toast.makeText(context,"Your account has been activated! Now you can log in!",Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_afterRegisterFragment_to_fragmentLogIn)
        }

        return view
    }

    private fun initializeListeners(view: View) {
        activationTextView.setOnClickListener{
            //ha a user ranyom a textview-ra,akkor aktivalni fogjuk a fiokjat!
            registerViewModel.activateUser()
        }

        loginHiperlink.setOnClickListener {
            findNavController().navigate(R.id.action_afterRegisterFragment_to_fragmentLogIn)
        }
    }

    private fun initializeView(view: View) {
        afterRegisterTitle = view.findViewById(R.id.after_register_title)
        activationTextView = view.findViewById(R.id.email_not_received)
        loginHiperlink = view.findViewById(R.id.back_to_log_in)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AfterRegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AfterRegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}