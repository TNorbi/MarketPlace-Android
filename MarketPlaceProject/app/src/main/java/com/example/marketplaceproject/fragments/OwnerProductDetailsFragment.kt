package com.example.marketplaceproject.fragments

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OwnerProductDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OwnerProductDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var ownerImageView: ImageView
    private lateinit var ownerUsername: TextView
    private lateinit var uploadDate: TextView
    private lateinit var productName: TextView
    private lateinit var editProduct: ImageView
    private lateinit var productPricePerUnit: TextView
    private lateinit var availabilityIcon: ImageView
    private lateinit var availabilityTextView: TextView
    private lateinit var productDetail: TextView
    private lateinit var totalItemsCircle: TextView
    private lateinit var pricePerUnitCircle: TextView
    private lateinit var soldItemsCircle: TextView
    private lateinit var revenueCircle: TextView
    private lateinit var timelineViewModel: TimelineViewModel
    private lateinit var addProductViewModel: AddProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //itt lekerem a letezo timelineviewmodelt!
        val factory = TimelineViewModelFactory(Repository())
        timelineViewModel =
            ViewModelProvider(this.requireActivity(), factory).get(TimelineViewModel::class.java)

        //itt lekerem a letezo addProductViewModelt (preview my fair miatt)
        val factory2 = AddProductViewModelFactory(requireContext(), Repository())
        addProductViewModel =
            ViewModelProvider(requireActivity(), factory2).get(AddProductViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_owner_product_details, container, false)

        view?.apply {
            initializeView(this)
            changeColorOfTexts()
            loadOwnerProductDetails()
            initializeListeners(this)
        }

        (activity as MainActivity).closeSearchView()

        return view
    }

    private fun loadOwnerProductDetails() {
        //itt feltoltom a View elemek adatait

        //ez place holderkent fog szerepelni
        ownerImageView.setImageResource(R.drawable.ic_bazaar_launcher_foreground)

        if (!addProductViewModel.previewMyFair) {
            //Owner nem preview modban nezi a termeket => tudja majd hasznalni az edit-et!
            //ugyanakkor a mar letezo listabol kell kiszedjem az infokat(letezo termek ez!)
            editProduct.visibility = View.VISIBLE

            val currentProduct =
                timelineViewModel.products.value!![timelineViewModel.adapterCurrentPosition]

            ownerUsername.text = currentProduct.username
            uploadDate.text = convertTimeStampToDate(currentProduct.creation_time)
            productName.text = currentProduct.title
            productPricePerUnit.text =
                "${currentProduct.price_per_unit} ${currentProduct.price_type}/${currentProduct.amount_type}"

            if (currentProduct.is_active) {
                availabilityIcon.setImageResource(R.drawable.ic_active_product)
                availabilityTextView.text = "Active"
                availabilityTextView.setTextColor(Color.parseColor("#00B5C0"))
            } else {
                availabilityIcon.setImageResource(R.drawable.ic_inactive_product)
                availabilityTextView.text = "Inactive"
            }

            productDetail.text = currentProduct.description
            totalItemsCircle.text = "${currentProduct.units} ${currentProduct.amount_type}"

            pricePerUnitCircle.text =
                "${currentProduct.price_per_unit} ${currentProduct.price_type}"

            soldItemsCircle.text = "Yet to be developed"
            revenueCircle.text = "Yet to be developed"

        } else {
            //Owner preview modban nezi meg a termeket
            //=> ez egy uj termek lesz,ami nincs fent az adatbazisban
            // => az edit gombot el kell rejteni!

            (activity as MainActivity).supportActionBar!!.title = "Product detail preview"
            editProduct.visibility = View.GONE

            ownerUsername.text = TokenApplication.username
            uploadDate.text = "Today"
            productName.text = addProductViewModel.newProduct.value!!.title
            productPricePerUnit.text =
                "${addProductViewModel.newProduct.value!!.price_per_unit} ${addProductViewModel.newProduct.value!!.price_type}/${addProductViewModel.newProduct.value!!.amount_type}"

            if (addProductViewModel.newProduct.value!!.is_active) {
                availabilityIcon.setImageResource(R.drawable.ic_active_product)
                availabilityTextView.text = "Active"
                availabilityTextView.setTextColor(Color.parseColor("#00B5C0"))
            } else {
                availabilityIcon.setImageResource(R.drawable.ic_inactive_product)
                availabilityTextView.text = "Inactive"
            }

            productDetail.text = addProductViewModel.newProduct.value!!.description
            totalItemsCircle.text =
                "${addProductViewModel.newProduct.value!!.units} ${addProductViewModel.newProduct.value!!.amount_type}"

            pricePerUnitCircle.text =
                "${addProductViewModel.newProduct.value!!.price_per_unit} ${addProductViewModel.newProduct.value!!.price_type}"

            soldItemsCircle.text = "0 ${addProductViewModel.newProduct.value!!.amount_type}"
            revenueCircle.text = "0 ${addProductViewModel.newProduct.value!!.price_type}"

            addProductViewModel.previewMyFair = false
        }
    }


    private fun convertTimeStampToDate(creationTime: Long): String {
        val pattern = "yyyy.MM.dd";
        val stamp = Timestamp(creationTime)
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date = simpleDateFormat.format(Date(stamp.getTime()))
        return date
    }

    private fun initializeView(view: View) {
        ownerImageView = view.findViewById(R.id.owner_imageview_owner_published)
        ownerUsername = view.findViewById(R.id.owner_username_owner_published)
        uploadDate = view.findViewById(R.id.upload_date_owner_published)
        productName = view.findViewById(R.id.product_name_owner_published)
        editProduct = view.findViewById(R.id.edit_owner_product)
        productPricePerUnit = view.findViewById(R.id.product_price_owner_published)
        availabilityIcon = view.findViewById(R.id.availability_icon_owner_published)
        availabilityTextView = view.findViewById(R.id.availability_text_owner_published)
        productDetail = view.findViewById(R.id.product_detail_owner_published)
        totalItemsCircle = view.findViewById(R.id.total_items_circle_textview)
        pricePerUnitCircle = view.findViewById(R.id.price_per_item_circle_textview)
        soldItemsCircle = view.findViewById(R.id.sold_items_circle_textview)
        revenueCircle = view.findViewById(R.id.revenue_circle_textview)
    }

    private fun initializeListeners(view: View) {
        editProduct.setOnClickListener {
            //ha ranyom az edit gombra,akkor lehet a termekunket modositani
            Toast.makeText(context, "Modify your product feature", Toast.LENGTH_SHORT).show()
            timelineViewModel.editOwnerProduct = true
            findNavController().navigate(R.id.action_ownerProductDetailsFragment_to_addProductFragment)
        }
    }

    //forras: StackOverFlow
    private fun changeColorOfTexts() {
        //ez a kicsi kod megnezi, hogy a telefonunk night modban van vagy sem
        //ha night modban van akkor a text color feher lesz (hogy konyebben lehessen latni), ellenkezo esetben fekete szin marad (forras : Stack)
        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                totalItemsCircle.setBackgroundResource(R.drawable.textview_circle_darkmode)
                totalItemsCircle.setTextColor(Color.parseColor("white"))

                pricePerUnitCircle.setBackgroundResource(R.drawable.textview_circle_darkmode)
                pricePerUnitCircle.setTextColor(Color.parseColor("white"))

                soldItemsCircle.setBackgroundResource(R.drawable.textview_circle_darkmode)
                soldItemsCircle.setTextColor(Color.parseColor("white"))

                revenueCircle.setBackgroundResource(R.drawable.textview_circle_darkmode)
                revenueCircle.setTextColor(Color.parseColor("white"))
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
         * @return A new instance of fragment OwnerProductDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OwnerProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}