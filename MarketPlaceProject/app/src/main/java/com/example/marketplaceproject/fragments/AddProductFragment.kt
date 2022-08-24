package com.example.marketplaceproject.fragments

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.marketplaceproject.MainActivity
import com.example.marketplaceproject.R
import com.example.marketplaceproject.TokenApplication
import com.example.marketplaceproject.repository.Repository
import com.example.marketplaceproject.viewModels.addproduct.AddProductViewModel
import com.example.marketplaceproject.viewModels.addproduct.AddProductViewModelFactory
import com.example.marketplaceproject.viewModels.timeline.TimelineViewModel
import com.example.marketplaceproject.viewModels.timeline.TimelineViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddProductFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var addProductViewModel: AddProductViewModel
    private lateinit var listViewModel: TimelineViewModel
    private lateinit var availabilitySwitch: Switch
    private lateinit var titleInput: TextInputEditText
    private lateinit var priceAmountInput: TextInputEditText
    private lateinit var priceTypeSpinner: Spinner
    private lateinit var availableAmountInput: TextInputEditText
    private lateinit var availableAmountSpinner: Spinner
    private lateinit var productDescription: TextInputEditText
    private lateinit var launchFare: Button
    private lateinit var previewButton: Button
    private lateinit var priceType: String
    private lateinit var amountType: String
    private lateinit var priceTypes: Array<String>
    private lateinit var amount_type: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //itt letrehozom az addproduct viewModeljet
        val factory = AddProductViewModelFactory(requireContext(), Repository())
        addProductViewModel =
            ViewModelProvider(requireActivity(), factory).get(AddProductViewModel::class.java)

        val factory2 = TimelineViewModelFactory(Repository())
        listViewModel =
            ViewModelProvider(requireActivity(), factory2).get(TimelineViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_product, container, false)

        view?.apply {
            initializeView(this)
            initializeSpinners()

            if (listViewModel.editOwnerProduct) {
                //hogyha a mar letezo termekunket akarjuk modositani,
                // akkor ennek adatai fognak megjelenni a formon
                (activity as MainActivity).supportActionBar!!.title = "Edit your fare"
                launchFare.text = "Edit my fair"
                showProductData()
            }

            addProductViewModel.previewMyFair = false
            initializeListeners(this)
        }

        addProductViewModel.productID.observe(viewLifecycleOwner) {
            //hogyha a productId modosult,akkor azt jelenti, hogy sikeresen hozzaadodott a termekunk az adatbazisra
            //ebben az esetben visszaterunk a My Market ablakra

            if (addProductViewModel.modosultProductID) {
                addProductViewModel.modosultProductID = false

                Toast.makeText(
                    context,
                    "Your product has been successfully added!",
                    Toast.LENGTH_LONG
                )
                    .show()

                listViewModel.getProducts()

                findNavController().navigate(R.id.action_addProductFragment_to_myMarketFragment)
            }
        }

        addProductViewModel.updatedItemID.observe(viewLifecycleOwner) {
            //hogyha az updatedItemId erteke modosul,akkor azt fogja jelenteni, hogy a
            // termek sikeresen lett modositva => visszaterunk a product details ablakra,
            // ahol majd a friss adatok fognak megjelenni

            if (addProductViewModel.modosultupdatedItemID) {
                addProductViewModel.modosultupdatedItemID = false

                Toast.makeText(
                    context,
                    "Your product's data has been successfully updated!",
                    Toast.LENGTH_LONG
                ).show()

                //frissitem a listviewmodelben levo products tartalmat a legfrisebb adatokkal
                listViewModel.editOwnerProduct = false
                listViewModel.getProducts()
                listViewModel.products.value!![listViewModel.adapterCurrentPosition] =
                    addProductViewModel.newProduct.value!!
                findNavController().navigate(R.id.action_addProductFragment_to_ownerProductDetailsFragment)
            }
        }

        return view
    }

    private fun showProductData() {
        //ebben a fuggvenyben fogom feltolteni a kurens termek adatait a formon

        val currentProduct = listViewModel.products.value!![listViewModel.adapterCurrentPosition]

        titleInput.setText(currentProduct.title)
        priceAmountInput.setText(currentProduct.price_per_unit)
        availableAmountInput.setText(currentProduct.units)
        productDescription.setText(currentProduct.description)
        availabilitySwitch.isChecked = currentProduct.is_active

        if (availabilitySwitch.isChecked) {
            availabilitySwitch.text = "Active"
            availabilitySwitch.setTextColor(Color.parseColor("#00B5C0"))
        } else {
            availabilitySwitch.text = "Inactive"
            availabilitySwitch.setTextColor(Color.parseColor("#9A9A9A"))
        }

        //torlom a string elejerol es vegerol a " karaktert,
        // hogy a spinner tudjam kivalasztani a kurens elemet!
        currentProduct.price_type = currentProduct.price_type.replace("\"", "")
        currentProduct.amount_type = currentProduct.amount_type.replace("\"", "")

        priceTypeSpinner.setSelection(priceTypes.indexOf(currentProduct.price_type))
        availableAmountSpinner.setSelection(amount_type.indexOf(currentProduct.amount_type))
    }

    private fun initializeSpinners() {
        //------------------------price type spinner inicializalasa----------------------

        priceTypes = resources.getStringArray(R.array.Price_Type)

        val adapterPrice =
            ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, priceTypes)
        priceTypeSpinner.adapter = adapterPrice

        priceTypeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                priceType = priceTypes[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        //---------------------------available amount spinner inicializalasa--------------
        amount_type = resources.getStringArray(R.array.Amount_type)
        val adapterAmountType =
            ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, amount_type)
        availableAmountSpinner.adapter = adapterAmountType

        availableAmountSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                amountType = amount_type[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    private fun initializeView(view: View) {
        availabilitySwitch = view.findViewById(R.id.availability_switch)
        titleInput = view.findViewById(R.id.title_input_edit)

        priceAmountInput = view.findViewById(R.id.price_amount_input)
        priceAmountInput.inputType = InputType.TYPE_CLASS_NUMBER
        priceTypeSpinner = view.findViewById(R.id.price_type_spinner)

        availableAmountInput = view.findViewById(R.id.available_amount_textinput)
        availableAmountInput.inputType = InputType.TYPE_CLASS_NUMBER
        availableAmountSpinner = view.findViewById(R.id.available_amount_spinner)

        productDescription = view.findViewById(R.id.product_description_input)

        launchFare = view.findViewById(R.id.launch_fair_button)
        previewButton = view.findViewById(R.id.preview_my_fair_button)
    }

    private fun initializeListeners(view: View) {
        availabilitySwitch.setOnClickListener {
            if (availabilitySwitch.isChecked) {
                availabilitySwitch.text = "Active"
                availabilitySwitch.setTextColor(Color.parseColor("#00B5C0"))
            } else {
                availabilitySwitch.text = "Inactive"
                availabilitySwitch.setTextColor(Color.parseColor("#9A9A9A"))
            }
        }

        launchFare.setOnClickListener {
            //hogyha a User megnyomja a Launch Fare gombot, akkor feltolti az uj termeket az adatbazisba
            if (titleInput.text!!.isEmpty() || productDescription.text!!.isEmpty() || priceAmountInput.text!!.isEmpty() || availableAmountInput.text!!.isEmpty()) {
                Toast.makeText(
                    context,
                    "Title, description , price or quantity missing",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                addProductViewModel.newProduct.value.let {
                    //itt feltoltom a viewModelt a user altal megadott adatokkal,majd elinditom az Add Product kerest/muveletet
                    Log.d("xxx", "Title: ${titleInput.text}")
                    it!!.title = titleInput.text.toString()
                    it!!.description = productDescription.text.toString()
                    it!!.price_per_unit = priceAmountInput.text.toString()
                    it!!.units = availableAmountInput.text.toString()
                    it!!.is_active = availabilitySwitch.isChecked
                    it!!.amount_type = amountType
                    it!!.price_type = priceType
                }

                //hogyha az Owner az edit gombra nyomott, ami altal modositani akarja a
                // mar letezo termeket,akkor az Update Product endpoint hivodik meg
                //ha pedig uj termeket akarna beszurni,akkor az Add Product endpoint hivodik meg
                if (listViewModel.editOwnerProduct) {
                    addProductViewModel.newProduct.value!!.product_id =
                        listViewModel.products.value!![listViewModel.adapterCurrentPosition].product_id
                    addProductViewModel.updateProduct()
                } else {
                    addProductViewModel.addProduct()
                }
            }
        }

        previewButton.setOnClickListener {
            //hogyha a User ranyom a Preview my fair gombra,akkor megjeleniti
            //egy ablakban, hogy a termek hirdetese hogyan fog majd kinezni korulbelul

            //(itt fel fogom hasznalni az OwnerProductDetail fragmenst annyi kulonbseggel,
            // hogy az edit gombot el fogom rejteni)

            addProductViewModel.newProduct.value.let {
                //itt feltoltom a viewModelt a user altal megadott adatokkal,majd elinditom az Add Product kerest/muveletet
                it!!.title = titleInput.text.toString()
                it!!.description = productDescription.text.toString()
                it!!.price_per_unit = priceAmountInput.text.toString()
                it!!.units = availableAmountInput.text.toString()
                it!!.is_active = availabilitySwitch.isChecked
                it!!.amount_type = amountType
                it!!.price_type = priceType
            }

            addProductViewModel.previewMyFair = true
            findNavController().navigate(R.id.action_addProductFragment_to_ownerProductDetailsFragment)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddProductFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddProductFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}