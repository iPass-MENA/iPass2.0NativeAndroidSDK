package com.app.ipassplus.ui.dashboard.fragment

import ScenariosListAdapter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ipassplus.MainActivity
import com.app.ipassplus.R
import com.app.ipassplus.ui.dashboard.model.ScenariosItemModel
import com.app.ipassplus.databinding.FragmentDashboardBinding
import com.app.ipassplus.ui.dashboard.dialog.DualPoiDisclaimerDialog
import com.sdk.ipassplussdk.apis.ResultListener
import com.sdk.ipassplussdk.core.configProperties
import com.sdk.ipassplussdk.core.iPassSDKManger
import com.sdk.ipassplussdk.model.response.transaction_details.DualPoiTransactionResponse
import com.sdk.ipassplussdk.model.response.transaction_details.TransactionDetailResponse

class DashboardFragment : Fragment(), ScenariosListAdapter.OnClickListener {

    private val binding by lazy { FragmentDashboardBinding.inflate(layoutInflater) }
    private lateinit var adapter: ScenariosListAdapter
    //  private val email = "ipassmobsdk@yopmail.com"
//    private val email = "testingonprem123@yopmail.com"
//    private val email = "mrverma91378@gmail.com"
//    private val email = "testonpremcust123@yopmail.com"

//     private val email = "ipassandhar@yopmail.com"
//    private val password = "Admin@123#"

    /*
    private val email = "testcs@yopmail.com"
    private val password = "Admin@12345#"
     */

    /*

     */
    //  private val email = "anmol-rana@csgroupchd.com"
    //  private val password = "Anmol@1234#"

//    private val email = "mobtest123@yopmail.com"
//    private val password = "Admin@123#"
//    private val apptoken = "eyJhbGciOiJIUzI1NiJ9.bW9idGVzdDEyM0B5b3BtYWlsLmNvbXRlc3QgYXBpJ3MgICAzNDMzYWZlZC0zNWZkLTQ3MGMtOTNlYy1lYjBjN2I1Y2VlZjI.2XbfGso6YVZ5xhbM74Ye1NnOzPvKSCv20ceT9GzR3HQ"


//    private val email = "uficotest123@yopmail.com"
//    private val password = "Admin@123#"
//    private val apptoken = "eyJhbGciOiJIUzI1NiJ9.dWZpY290ZXN0MTIzQHlvcG1haWwuY29tdGVzdCB1ZmljbyAgIGM4ZTQ4NTA0LTliZmYtNDgzZi1hMmU4LTRkMzFmNmI1OGI2ZQ.DYS4QNo1oR1xyvh0ZWK5vCx1UazdBR1iEVNcbZl4pPo"


    private val email = "happy@yopmail.com"
    private val password = "Admin@123#"

    //private val apptoken = "eyJhbGciOiJIUzI1NiJ9.aGFwcHlAeW9wbWFpbC5jb21oYXBweSBJUEFTUyAgIGNiZmQ3ZGVhLWU5MzMtNGE0Ny05OTkyLTIyM2Q4NWI0MzU0MA.QryilNfB_D23eBY21p2iPyE9rI_Zba0xXdN2qV7WASk"
    private val apptoken =
        "eyJhbGciOiJIUzI1NiJ9.aGFwcHlAeW9wbWFpbC5jb21oYXBweSBwdW5kaXIgICBjNDZiODVjOC03MzgzLTQ4MGItODM3NC1mZDYzYTU4OGM5NGI.4K-MXMb1f1wIASA0MjtBPxEdKyMPjhDLqTFsdD4Tq1o"
    // private val apptoken = "eyJhbGciOiJIUzI1NiJ9.aGFwcHlAeW9wbWFpbC5jb21IYXBweSBpUGFzcyAgIDYxZTVmYzkzLTVhNmQtNGJhNi1hZjhlLTA3OWJmMmQyMDA5Yw.qUNkZSU_d5mnbnhfQ2L6GA1DNZPBnMBczpF9P-H5qF8"


    /*
    private val email = "testmobile@yopmail.com"
    private val password = "Admin@123#"
    private val apptoken = "eyJhbGciOiJIUzI1NiJ9.dGVzdG1vYmlsZUB5b3BtYWlsLmNvbU1vYmlsZSBzaW5naCAgIDZiYzNmMTlkLTY1YjktNDllZS05MGYyLTYwZWNhNzFmNDE5NA.4zjot7d9d72KYAWjqE-JpIl8oJuuKd-HIS4QzbpFp7I"

     */


    val phoneNumber = "7894563210"
    private var flowId = "10032"

    // val flowId = "10016"
    val socialMediaEmail = "ipassmobisdk@yopmail.com"

    /**
     * Indicates which POI mode is currently selected.
     *
     * SINGLE -> Existing flow (default)
     * DUAL   -> Future implementation
     */
    private enum class PoiMode {
        SINGLE,
        DUAL
    }

    private var selectedPoiMode = PoiMode.SINGLE


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // new updates

        val scenarioList = arrayListOf(
            ScenariosItemModel(
                R.drawable.ic_idv_liveness_aml,
                "IDV + Liveness + AML",
                getString(R.string.description_flow_user_document_authenticity_liveness_face_matching_aml),
                false
            ),
            ScenariosItemModel(
                R.drawable.ic_id_verification_aml,
                "IDV + AML",
                getString(R.string.description_flow_document_authenticity_aml),
                false
            ),
            ScenariosItemModel(
                R.drawable.ic_id_verification,
                "IDV + Liveness",
                getString(R.string.description_flow_document_authenticity_user_liveness_face_matching),
                false
            ),
            ScenariosItemModel(
                R.drawable.ic_id_verification,
                "IDV",
                getString(R.string.description_flow_document_authenticity),
                false
            )
        )

//        val scenarioList = arrayListOf(
//            ScenariosItemModel(R.drawable.ic_full_processing,"Full Processing",getString(R.string.description_flow_full_processing),true),
//            ScenariosItemModel(R.drawable.ic_idv_liveness_aml, "IDV + Liveness + AML",getString(R.string.description_flow_user_document_authenticity_liveness_face_matching_aml),false),
//            ScenariosItemModel(R.drawable.ic_id_verification_aml, "IDV + AML",getString(R.string.description_flow_document_authenticity_aml),false),
//            ScenariosItemModel(R.drawable.ic_id_verification, "IDV + Liveness",getString(R.string.description_flow_document_authenticity_user_liveness_face_matching),false),
//            ScenariosItemModel(R.drawable.ic_id_verification, "IDV",getString(R.string.description_flow_document_authenticity),false)
//        )

        adapter = ScenariosListAdapter(scenarioList, requireContext())
        adapter.setOnClickListener(this)

        binding.rvitems.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@DashboardFragment.adapter
        }

        //setupPoiTabs()
    }


    /**
     * Initializes POI tab selection.
     *
     * Currently:
     * Single POI -> Existing implementation
     * Dual POI -> Placeholder implementation
     */
//    private fun setupPoiTabs() {
//
//        // Get Dual POI access from SDK
//        val dualPoiAccess = iPassSDKManger.isDualPoiAccessEnabled()
//
//        Log.d(
//            "DualPOI",
//            "Access = $dualPoiAccess"
//        )
//
////        Log.d(
////            "DualPOI",
////            "Remaining = ${iPassSDKManger.getDualPoiRemaining()}"
////        )
//
//        // Show / hide Dual POI tab
//        binding.btnDualPoi.visibility =
//            if (dualPoiAccess) {
//                View.VISIBLE
//            } else {
//                View.GONE
//            }
//
//        // Single POI
//        binding.btnSinglePoi.setOnClickListener {
//
//            if (selectedPoiMode != PoiMode.SINGLE) {
//                selectedPoiMode = PoiMode.SINGLE
//                updatePoiSelection()
//            }
//        }
//
//        // Dual POI
//        binding.btnDualPoi.setOnClickListener {
//
//            // Check remaining transaction limit
//            if (!iPassSDKManger.hasDualPoiRemaining()) {
//
//                Toast.makeText(
//                    requireContext(),
//                    "Dual POI limit has been exhausted.",
//                    Toast.LENGTH_SHORT
//                ).show()
//
//                return@setOnClickListener
//            }
//
//            // Select Dual POI
//            if (selectedPoiMode != PoiMode.DUAL) {
//                selectedPoiMode = PoiMode.DUAL
//                updatePoiSelection()
//            }
//        }
//
//        // Default selection
//        selectedPoiMode = PoiMode.SINGLE
//        updatePoiSelection()
//    }

//    private fun setupPoiTabs() {
//
//
//        // Check Dual POI access from SDK
//        val dualPoiAccess = iPassSDKManger.isDualPoiAccessEnabled()
//
//        // Show/Hide Dual POI tab based on access
//        binding.btnDualPoi.visibility = if (dualPoiAccess) {
//            View.VISIBLE
//        } else {
//            View.GONE
//        }
//
//        binding.btnDualPoi.setOnClickListener {
//
//            if (!iPassSDKManger.hasDualPoiRemaining()) {
//
//                Toast.makeText(
//                    requireContext(),
//                    "Dual POI limit has been exhausted.",
//                    Toast.LENGTH_SHORT
//                ).show()
//
//                return@setOnClickListener
//            }
//
//            if (selectedPoiMode != PoiMode.DUAL) {
//                selectedPoiMode = PoiMode.DUAL
//                updatePoiSelection()
//            }
//        }
//
////        binding.btnDualPoi.setOnClickListener {
////            if (selectedPoiMode != PoiMode.DUAL) {
////                selectedPoiMode = PoiMode.DUAL
////                updatePoiSelection()
////            }
////        }
//
//        if (dualPoiAccess) {
//            binding.btnDualPoi.setOnClickListener {
//                if (selectedPoiMode != PoiMode.DUAL) {
//                    selectedPoiMode = PoiMode.DUAL
//                    updatePoiSelection()
//                }
//            }
//        }
//
//        selectedPoiMode = PoiMode.SINGLE
//        // Single POI selected by default
//        updatePoiSelection()
//    }

    /**
     * Updates the selected state of POI tabs.
     *
     * Only UI state changes here.
     * No business logic is executed.
     */
    /**
     * Updates the POI tab UI.
     *
     * Only one tab can be selected at a time.
     * This method is the single source of truth for the tab state.
     */
    private fun updatePoiSelection() {

        val isSingleSelected = selectedPoiMode == PoiMode.SINGLE

        // -------- Single POI --------
        binding.btnSinglePoi.isSelected = isSingleSelected
        binding.btnSinglePoi.setBackgroundResource(
            if (isSingleSelected)
                R.drawable.bg_poi_selected
            else
                R.drawable.bg_poi_unselected
        )

        binding.btnSinglePoi.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (isSingleSelected)
                    android.R.color.white
                else
                    R.color.black
            )
        )

        // -------- Dual POI --------
        binding.btnDualPoi.isSelected = !isSingleSelected
        binding.btnDualPoi.setBackgroundResource(
            if (!isSingleSelected)
                R.drawable.bg_poi_selected
            else
                R.drawable.bg_poi_unselected
        )

        binding.btnDualPoi.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (!isSingleSelected)
                    android.R.color.white
                else
                    R.color.black
            )
        )

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onScenarioPickClick(position: Int, model: ScenariosItemModel) {

        /**
         * Dual POI
         * Future:
         * 1. Passport Scan
         * 2. Document Front
         * 3. Document Back
         */

//        if (selectedPoiMode == PoiMode.DUAL) {
//
//            showDualPoiDisclaimer(position)
//
//            return
//        }

        //** send the flow id in api on basis of selected position
//
//        if (position == 0){
//            //**  Full Processing (10031)
//            flowId = "10031"
//        }else  if (position == 1){
//            //**  Id verification + Liveness + AML (10032)
//            flowId = "10032"
//        }else  if (position == 2){
//            //** Id verification + AML (10015)
//            flowId = "10015"
//        } else  if (position == 3){
//            //**  Id verification + Liveness (10011)
//            flowId = "10011"
//        }else  if (position == 4){
//            //**  Id verification  (10016)
//            flowId = "10016"
//        }else{
//
//        }

        if (position == 0) {
            //**  Id verification + Liveness + AML (10032)
            flowId = "10032"
        } else if (position == 1) {
            //** Id verification + AML (10015)
            flowId = "10015"
        } else if (position == 2) {
            //**  Id verification + Liveness (10011)
            flowId = "10011"
        } else if (position == 3) {
            //**  Id verification  (10016)
            flowId = "10016"
        } else {

        }


        Log.e("call", "flowId::  " + flowId + "    " + "position" + " " + position)

        iPassSDKManger.startScanningProcess(
            context = requireContext(),
            email = email,
            userToken = MainActivity.userToken,
            appToken = apptoken,
            socialMediaEmail = socialMediaEmail,
            phoneNumber = phoneNumber,
            flowId = flowId,
            bindingView = binding.root as ViewGroup
        ) { status, message ->
            if (status) {
                Log.e("startScanningProcess", message)
                getDocData()
            } else {
                Log.e("startScanningProcess", message)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDocData() {
        iPassSDKManger.getDocumentScannerData(
            requireContext(),
            apptoken,
            object : ResultListener<TransactionDetailResponse> {
                override fun onSuccess(response: TransactionDetailResponse?) {
                    if (response?.Apistatus!!) {
                        Log.e("onSuccess", response.Apimessage!!)
                        Log.e("onSuccess", response.data.toString())
                        // Toast.makeText(context,"Transaction completed successfully.", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("error", response.Apimessage!!)
                        Toast.makeText(context, response.Apimessage, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(exception: String) {
                    Log.e("onSuccess", exception)
                }

            })
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun getDualPoiDocData() {
//        iPassSDKManger.getDualPoiData(requireContext(), apptoken, object : ResultListener<DualPoiTransactionResponse> {
//            override fun onSuccess(response: DualPoiTransactionResponse?) {
//                if (response?.Apistatus!!) {
//                    Log.e("onSuccess", response.Apimessage!!)
//                    Log.e("onSuccess", response.documents.toString())
//                    Log.e("onSuccess", response.toString())
//                    Toast.makeText(context,"Transaction completed successfully.", Toast.LENGTH_SHORT).show()
//                } else {
//                    Log.e("error", response.Apimessage!!)
//                    Toast.makeText(context, response.Apimessage, Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onError(exception: String) {
//                Log.e("onSuccess", exception)
//            }
//
//        })
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDualPoiDisclaimer(position: Int) {

        DualPoiDisclaimerDialog {

            // User tapped Continue

            //startDualPoiFlow(position)

        }.show(parentFragmentManager, "DualPoiDisclaimer")
    }

    /**
     * Starts the Dual POI verification flow.
     *
     * Current flow:
     * Disclaimer
     * ↓
     * Passport Scan
     * ↓
     * ID Document Scan
     * ↓
     * Existing processing
     */
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun startDualPoiFlow(position: Int) {
//        // Determine flowId based on position
//        val selectedFlowId = when (position) {
//            0 -> "10031"
//            1 -> "10032"
//            2 -> "10015"
//            3 -> "10011"
//            4 -> "10016"
//            else -> "10032"
//        }
//
//        Log.e("call", "Dual POI flowId:: $selectedFlowId position: $position")
//
//        // Call the separate Dual POI entry point
//        iPassSDKManger.startDualPoiScanningProcess(
//            context = requireContext(),
//            email = email,
//            userToken = MainActivity.userToken,
//            appToken = apptoken,
//            socialMediaEmail = socialMediaEmail,
//            phoneNumber = phoneNumber,
//            flowId = selectedFlowId,
//            bindingView = binding.root as ViewGroup
//        ) { status, message ->
//            if (status) {
//                Log.e("startDualPoiScanning", message)
//               // getDualPoiDocData()
//                Toast.makeText(context, "Dual POI scanning completed successfully.", Toast.LENGTH_SHORT).show()
//            } else {
//                Log.e("startDualPoiScanning", message)
//                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    fun refreshPoiTabs() {
        if (!isAdded) return

        //setupPoiTabs()
    }

//    override fun onResume() {
//        super.onResume()
//
//        refreshPoiTabs()
//    }
}