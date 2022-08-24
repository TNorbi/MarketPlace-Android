package com.example.marketplaceproject.fragments

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.marketplaceproject.R
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.marketplaceproject.MainActivity
import com.example.marketplaceproject.repository.Repository
import com.example.marketplaceproject.viewModels.profile.ProfileViewModel
import com.example.marketplaceproject.viewModels.profile.ProfileViewModelFactory
import android.R.color
import android.app.AlertDialog

import android.content.res.ColorStateList
import android.util.Log
import android.util.Patterns
import androidx.navigation.fragment.findNavController


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileOwnerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileOwnerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var userPicture: ImageView
    private lateinit var username: TextView
    private lateinit var userEmail: EditText
    private lateinit var editUserName: EditText
    private lateinit var userPhoneNumber: EditText
    private lateinit var publishButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //lekerem a profileViewModelt,amit a MainActivityben hoztam letre
        val factory = ProfileViewModelFactory(Repository())
        profileViewModel =
            ViewModelProvider(requireActivity(), factory).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_owner, container, false)

        view?.apply {
            initializeView(this)
            changeUnderlineColor()
            listUserDatas()
            initializeListeners(this)
        }

        profileViewModel.token.observe(viewLifecycleOwner) {
            if (profileViewModel.modosultToken) {
                Toast.makeText(context, "Your settings have been saved!", Toast.LENGTH_LONG).show()
                profileViewModel.modosultToken = false
                findNavController().navigate(R.id.action_profileOwnerFragment_self)
            }

        }

        return view
    }

    //forras: StackOverFlow
    private fun changeUnderlineColor() {
        //ez a kicsi kod megnezi, hogy a telefonunk night modban van vagy sem
        //ha night modban van akkor a text color feher lesz (hogy konyebben lehessen latni), ellenkezo esetben fekete szin marad (forras : Stack)
        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            userEmail.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            editUserName.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            userPhoneNumber.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
        }
    }

    private fun listUserDatas() {
        userPicture.setImageResource(R.drawable.ic_bazaar_launcher_foreground) //ez ideiglenesen van igy, majd kesobb kell modositsam
        username.text = profileViewModel.user.value!!.username
        userEmail.setText(profileViewModel.user.value!!.email)
        if (profileViewModel.user.value!!.phone_number != "null") {
            userPhoneNumber.setText(profileViewModel.user.value!!.phone_number)
        }
    }

    private fun initializeListeners(view: View) {
        publishButton.setOnClickListener {
            //megnezem, hogyha egy ervenyes roman telefonszamot adott meg vagy sem

            val romanianPhonePattern =
                "^(\\+4|)?(07[0-8]{1}[0-9]{1}|02[0-9]{2}|03[0-9]{2}){1}?(\\s|\\.|\\-)?([0-9]{3}(\\s|\\.|\\-|)){2}\$"
            if (userPhoneNumber.text.isNotEmpty() && !userPhoneNumber.text.matches(
                    Regex(
                        romanianPhonePattern
                    )
                )
            ) {
                //ha nem adott egy jo telefonszamot,akkor ertesitem, hogy hibas telefonszamot adott meg

                Toast.makeText(
                    context,
                    "Please give a valid romanian phone number!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //hogyha jo volt a telefonszam akkor ellenorizni fogom az email cimet
                //----------------------ellenorizzuk az email cimet, ha a User jol adta meg vagy sem----------------------

                //hogyha az uj email box-a user,akkor nem fogjuk frissiteni a User adatait(muszaly kell legyen email cim!)
                if (userEmail.text.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Email field is empty! Please give a valid email address!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    //megnezzuk, hogyha egy ervenyes email cimet adott meg a User
                    //abban az esetben, ha nem ervenyes az email cim,akkor nem frissitjuk a User adatait
                    //es szolni fogunk a Usernek, hogy adjon meg egy ervenyes email cimet!

                    if (!Patterns.EMAIL_ADDRESS.matcher(userEmail.text).matches()) {
                        Toast.makeText(
                            context,
                            "Please enter valid email address!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {

                        //hogyha az uj email cim kulonbozik a kurens email cimtol,akkor megkerdezzuk a Usertol,
                        // hogyha biztosan meg akarja ezt valtoztatni (egy Alert Dialoggal fogom ezt megtenni)
                        //ha nem akarja akkor a fieldeket visszaallitom a kurens ertekekre!
                        if (userEmail.text.toString() != profileViewModel.user.value!!.email) {


                            val alertDialog = AlertDialog.Builder(context)
                            alertDialog.setTitle("Submit")
                            alertDialog.setMessage("Are you sure you want to change your email address and your data?")

                            alertDialog.setPositiveButton("Yes") { _, _ ->

                                profileViewModel.user.let {
                                    if (editUserName.text.isNotEmpty()) {
                                        it.value!!.username = editUserName.text.toString()
                                    }
                                    it.value!!.email = userEmail.text.toString()
                                    it.value!!.phone_number = userPhoneNumber.text.toString()
                                }

                                profileViewModel.updateUserInfo()

                            }

                            alertDialog.setNegativeButton("No") { _, _ ->
                                userEmail.setText(profileViewModel.user.value!!.email)
                                userPhoneNumber.setText(profileViewModel.user.value!!.phone_number)
                            }

                            alertDialog.show()
                        } else {
                            //ha az uj email cim megegyezik a kurenssel , akkor megkerdezzuk a Usertol, hogyha biztos menteni akarja modositasait
                            //ha nem akarja akkor visszaallitjuk az eredeti ertekekre!

                            val alertDialog = AlertDialog.Builder(context)
                            alertDialog.setTitle("Submit")
                            alertDialog.setMessage("Are you sure you want to change your data?")

                            alertDialog.setPositiveButton("Yes") { _, _ ->

                                profileViewModel.user.let {
                                    if (editUserName.text.isNotEmpty()) {
                                        it.value!!.username = editUserName.text.toString()
                                    }
                                    it.value!!.email = userEmail.text.toString()
                                    it.value!!.phone_number = userPhoneNumber.text.toString()
                                }

                                profileViewModel.updateUserInfo()

                            }

                            alertDialog.setNegativeButton("No") { _, _ ->
                                userEmail.setText(profileViewModel.user.value!!.email)
                                userPhoneNumber.setText(profileViewModel.user.value!!.phone_number)
                            }

                            alertDialog.show()
                        }
                    }
                }
            }
        }
    }

    private fun initializeView(view: View) {
        userPicture = view.findViewById(R.id.userPicture)
        username = view.findViewById(R.id.userName)
        userEmail = view.findViewById(R.id.userEmail)
        editUserName = view.findViewById(R.id.edit_username)
        userPhoneNumber = view.findViewById(R.id.user_phone_number)
        publishButton = view.findViewById(R.id.publishButton)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileOwnerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileOwnerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}