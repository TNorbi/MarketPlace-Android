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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.marketplaceproject.MainActivity
import com.example.marketplaceproject.R
import com.example.marketplaceproject.repository.Repository
import com.example.marketplaceproject.viewModels.profile.ProfileViewModel
import com.example.marketplaceproject.viewModels.profile.ProfileViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileOwnerByOthersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileOwnerByOthersFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var profileViewModel : ProfileViewModel
    private lateinit var ownerPicture: ImageView
    private lateinit var ownerUsername : TextView
    private lateinit var emailLabel : TextView
    private lateinit var ownerEmail: TextView
    private lateinit var phoneNumberLabel : TextView
    private lateinit var ownerPhoneNumber : TextView
    private lateinit var chatButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //itt lekerem a mar letezo profileViewModelt
        val factory = ProfileViewModelFactory(Repository())
        profileViewModel =
            ViewModelProvider(requireActivity(), factory).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile_owner_by_others, container, false)

        view?.apply {
            initializeView(this)
            initializeListeners(this)
            loadUserData()
        }

        return view
    }

    private fun loadUserData() {
        ownerPicture.setImageResource(R.drawable.ic_bazaar_launcher_foreground) //placeholder, itt kesobb be kell tegyem az owner valodi kepet
        ownerUsername.text = profileViewModel.user.value!!.username
        ownerEmail.text = profileViewModel.user.value!!.email
        if (profileViewModel.user.value!!.phone_number != "null") {
            ownerPhoneNumber.text = profileViewModel.user.value!!.phone_number
        }
    }

    private fun initializeListeners(view: View) {
        chatButton.setOnClickListener {
            Toast.makeText(context,"Yet to be developed",Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeView(view: View) {
        ownerPicture = view.findViewById(R.id.ownerPicture)
        emailLabel = view.findViewById(R.id.email_label)
        phoneNumberLabel = view.findViewById(R.id.phone_number_label)
        ownerUsername = view.findViewById(R.id.owner_username)
        ownerEmail = view.findViewById(R.id.owner_email)
        ownerPhoneNumber = view.findViewById(R.id.owner_phone_number)
        chatButton = view.findViewById(R.id.chat_message_button)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileOwnerByOthersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileOwnerByOthersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}