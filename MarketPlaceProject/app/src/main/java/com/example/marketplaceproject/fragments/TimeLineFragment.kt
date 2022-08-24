package com.example.marketplaceproject.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplaceproject.MainActivity
import com.example.marketplaceproject.R
import com.example.marketplaceproject.TokenApplication
import com.example.marketplaceproject.adapters.MarketAdapter
import com.example.marketplaceproject.adapters.TimelineAdapter
import com.example.marketplaceproject.models.Product
import com.example.marketplaceproject.repository.Repository
import com.example.marketplaceproject.viewModels.timeline.TimelineViewModel
import com.example.marketplaceproject.viewModels.timeline.TimelineViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimeLineFragment : Fragment(), MarketAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var timelineViewModel: TimelineViewModel
    private lateinit var recyclerView: RecyclerView

    //private lateinit var adapter: TimelineAdapter
    private lateinit var adapter: MarketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //letrehozom a timeline viewmodeljet
        val factory = TimelineViewModelFactory(Repository())
        timelineViewModel =
            ViewModelProvider(requireActivity(), factory).get(TimelineViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_timeline, container, false)

        view?.apply {
            //inicializalasok
            setupRecyclerView(view)
        }

        timelineViewModel.getProducts()

        timelineViewModel.products.observe(viewLifecycleOwner) {
            adapter.setData(timelineViewModel.products.value as ArrayList<Product>)
            adapter.notifyDataSetChanged()
        }

        timelineViewModel.deletedProductID.observe(viewLifecycleOwner){
            //hogyha a deleteProductID erteke modosul, akkor az azt fogja jelenteni, hogy
            //termek sikeresen torlodott

            if(timelineViewModel.modosultDeletedProductID){
                timelineViewModel.modosultDeletedProductID = false

                //frissitem a lista tartalmat
                timelineViewModel.getProducts()

                Toast.makeText(context, "Product has been successfully deleted!", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    private fun setupRecyclerView(view: View) {
        //adapter = TimelineAdapter(ArrayList<Product>(), this.requireContext(),this)
        //adapter = MarketAdapter(ArrayList<Product>(), this.requireContext(), this)
        adapter = MarketAdapter(ArrayList<Product>(), this.requireContext(), activity as MainActivity,this)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)
    }

    override fun onDetailsClick(position: Int) {
        timelineViewModel.adapterCurrentPosition = position

        if (timelineViewModel.products.value!![position].username == TokenApplication.username) {
            //hogyha a sajat termekunkre kattintunk,akkor megjeleniti ennek detail-jei,amit tud modositani
            findNavController().navigate(R.id.action_timelineFragment_to_ownerProductDetailsFragment)
        } else {
            //hogyha mas user termekere kattintunk, akkor annak detailjei fognak megjeleni,amit akar meg is tudunk venni
            findNavController().navigate(R.id.action_timelineFragment_to_productDetailsCustomerFragment)
        }
    }

    override fun onDeleteClick(position: Int) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Delete Product")
        alertDialog.setMessage("Are you sure you want to delete this product?")

        alertDialog.setPositiveButton("Yes") { _, _ ->
            //Hogyha a User a Yes gombra nyom, akkor a kivalasztott termek ki fog torlodni!
            timelineViewModel.adapterCurrentPosition = position
            timelineViewModel.deleteProduct()
        }

        alertDialog.setNegativeButton("No") { _, _ ->
        }

        alertDialog.show()
    }

    override fun onOrderNowClick(position: Int) {
        timelineViewModel.adapterCurrentPosition = position
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TimeLineFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}