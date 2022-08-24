package com.example.marketplaceproject.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.marketplaceproject.R
import com.example.marketplaceproject.repository.Repository
import com.example.marketplaceproject.viewModels.myfares.MyFaresViewModel
import com.example.marketplaceproject.viewModels.myfares.MyFaresViewModelFactory
import com.example.marketplaceproject.viewModels.timeline.TimelineViewModel
import com.example.marketplaceproject.viewModels.timeline.TimelineViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

class OrderNowDialogFragment: DialogFragment() {

    private lateinit var exitButton: ImageButton
    private lateinit var ownerImage: ImageView
    private lateinit var ownerName: TextView
    private lateinit var productTitle: TextView
    private lateinit var pricePerUnit: TextView
    private lateinit var availabilityIcon: ImageView
    private lateinit var availabilityText: TextView
    private lateinit var uploadDate: TextView
    private lateinit var amountType: TextView
    private lateinit var amountInput: TextInputEditText
    private lateinit var commentInput: TextInputEditText
    private lateinit var cancelButton: Button
    private lateinit var sendMyOrderButton: Button
    private lateinit var timelineViewModel: TimelineViewModel
    private lateinit var myFaresViewModel: MyFaresViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //lekerem a letezo timelineViewmodelt!
        val factory = TimelineViewModelFactory(Repository())
        timelineViewModel =
            ViewModelProvider(requireActivity(), factory).get(TimelineViewModel::class.java)

        //lekerem/letrehozom a my fares viewModeljet
        val factoryMyFares = MyFaresViewModelFactory(Repository())
        myFaresViewModel = ViewModelProvider(requireActivity(),factoryMyFares).get(MyFaresViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_now_dialog, container, false)

        view?.apply {
            initializeView(this)
            loadProductData()
            initializeListeners(this)
        }

        myFaresViewModel.newOrderID.observe(viewLifecycleOwner){
            if(myFaresViewModel.modosultNewOrderID){
                myFaresViewModel.modosultNewOrderID = false

                dismiss()

                val dialog = AfterNewOrderDialogFragment()

                dialog.show(requireActivity().supportFragmentManager,"afterOrderDialog")
            }
        }

        return view
    }

    private fun loadProductData() {
        val currentProduct =
            timelineViewModel.products.value!![timelineViewModel.adapterCurrentPosition]

        //placeholder kepet rakok a termekrol(mert nem mukodik a kep lekeres/feltoltes a postmanben)
        ownerImage.setImageResource(R.drawable.ic_bazaar_launcher_foreground)

        ownerName.text = currentProduct.username
        productTitle.text = currentProduct.title
        pricePerUnit.text =
            "${currentProduct.price_per_unit} ${currentProduct.price_type}/${currentProduct.amount_type}"

        if (currentProduct.is_active) {
            availabilityIcon.setImageResource(R.drawable.ic_active_product)
            availabilityText.text = "Active"
            availabilityText.setTextColor(Color.parseColor("#33B5E5"))
        } else {
            availabilityIcon.setImageResource(R.drawable.ic_inactive_product)
            availabilityText.text = "Inactive"
            availabilityText.setTextColor(Color.parseColor("grey"))
        }

        amountType.text = currentProduct.amount_type
        uploadDate.text = convertTimeStampToDate(currentProduct.creation_time)

    }

    private fun convertTimeStampToDate(creationTime: Long): String {
        val pattern = "yyyy.MM.dd";
        val stamp = Timestamp(creationTime)
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date = simpleDateFormat.format(Date(stamp.getTime()))
        return date
    }


    private fun initializeView(view: View) {
        exitButton = view.findViewById(R.id.exit_button)
        ownerImage = view.findViewById(R.id.owner_imageview_ordernow)
        ownerName = view.findViewById(R.id.owner_username_ordernow)
        productTitle = view.findViewById(R.id.product_name_ordernow)
        pricePerUnit = view.findViewById(R.id.price_per_unit_ordernow)
        availabilityIcon = view.findViewById(R.id.availability_icon_ordernow)
        availabilityText = view.findViewById(R.id.availability_text_ordernow)
        amountInput = view.findViewById(R.id.amount_input_ordernow)
        commentInput = view.findViewById(R.id.comment_input_ordernow)
        cancelButton = view.findViewById(R.id.cancel_ordernow_button)
        sendMyOrderButton = view.findViewById(R.id.send_my_order_button)
        uploadDate = view.findViewById(R.id.upload_date_ordernow)
        amountType = view.findViewById(R.id.amount_type_ordernow)
    }

    private fun initializeListeners(view: View) {
        exitButton.setOnClickListener {
            Toast.makeText(context, "Exit button pressed", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        cancelButton.setOnClickListener {
            Toast.makeText(context, "Cancel button pressed", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        sendMyOrderButton.setOnClickListener {
            Toast.makeText(context, "Send my order button pressed", Toast.LENGTH_SHORT).show()

            myFaresViewModel.newOrder.value.let {
                it!!.title = productTitle.text.toString()
                it.description = commentInput.text.toString()
                it.price_per_unit = timelineViewModel.products.value!![timelineViewModel.adapterCurrentPosition].price_per_unit
                it.units = amountInput.text.toString()
                it.owner_username = ownerName.text.toString()
            }

            myFaresViewModel.addOrder()
        }
    }
}