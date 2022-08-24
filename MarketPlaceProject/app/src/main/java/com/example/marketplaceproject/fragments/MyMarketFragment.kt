package com.example.marketplaceproject.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyMarketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyMarketFragment : Fragment(), MarketAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView

    //private lateinit var adapter: TimelineAdapter
    private lateinit var adapter: MarketAdapter
    private lateinit var listViewModel: TimelineViewModel
    private lateinit var ownerProducts: List<Product>
    private lateinit var addProductButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //lekerem a letezo viewmodelt
        val factory = TimelineViewModelFactory(Repository())
        listViewModel =
            ViewModelProvider(requireActivity(), factory).get(TimelineViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_market, container, false)
        view?.apply {
            setupRecyclerView(this)
            addProductButton = this.findViewById(R.id.add_product_circle_button)
            listViewModel.editOwnerProduct = false
            initializeListeners(this)
            //listViewModel.getOwnerProducts(TokenApplication.username)
        }

        //Tesztelesre hasznaltam manyit es kiadta mind3 termeket,szoval ezzel a modszerrel mukodik
        //tulajdonkeppen a Timelimeban kapott products listat hasznalom fel itt, ahol csak filterezem a lista tartalmat
        //Az API fuggvenyem valami okbol kifolyolag nem akart mukodni, 500 Internal Server Errort kaptam

        ownerProducts =
            listViewModel.products.value!!.filter { it.username.equals(TokenApplication.username) }

        listViewModel.products.observe(viewLifecycleOwner) {
            //adapter.setData(listViewModel.products.value as ArrayList<Product>)
            ownerProducts =
                listViewModel.products.value!!.filter { it.username.equals(TokenApplication.username) }
            adapter.setData(ownerProducts as ArrayList<Product>)
            adapter.notifyDataSetChanged()
        }

        listViewModel.deletedProductID.observe(viewLifecycleOwner){
            //hogyha a deleteProductID erteke modosul, akkor az azt fogja jelenteni, hogy
            //termek sikeresen torlodott

            if(listViewModel.modosultDeletedProductID){
                listViewModel.modosultDeletedProductID = false

                //frissitem a lista tartalmat
                listViewModel.getProducts()

                Toast.makeText(context, "Product has been successfully deleted!", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    private fun initializeListeners(view: View) {
        addProductButton.setOnClickListener {
            //hogyha a User rakattint az Add buttonra, akkor megjeleniti az Add Product ablak
            findNavController().navigate(R.id.action_myMarketFragment_to_addProductFragment)
        }
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
        val selectedProduct = ownerProducts[position]
        //meg kell kapjam  a termeknek a poziciojat a nagylistabol,
        // amelynek a product_id-ja megegyezik a kivalasztott termek
        // product_id-val(es ez a backend szerint egyedi kulcs,
        // szoval csak 1 elemet kene visszateritsen)

        listViewModel.adapterCurrentPosition =
            listViewModel.products.value!!.indexOf(selectedProduct)
        findNavController().navigate(R.id.action_myMarketFragment_to_ownerProductDetailsFragment)
    }

    override fun onDeleteClick(position: Int) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Delete Product")
        alertDialog.setMessage("Are you sure you want to delete this product?")

        alertDialog.setPositiveButton("Yes") { _, _ ->
            //Hogyha a User a Yes gombra nyom, akkor a kivalasztott termek ki fog torlodni!
            val selectedProduct = ownerProducts[position]
            listViewModel.adapterCurrentPosition = listViewModel.products.value!!.indexOf(selectedProduct)
            listViewModel.deleteProduct()
        }

        alertDialog.setNegativeButton("No") { _, _ ->
        }

        alertDialog.show()
    }

    override fun onOrderNowClick(position: Int) {
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyMarketFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyMarketFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}