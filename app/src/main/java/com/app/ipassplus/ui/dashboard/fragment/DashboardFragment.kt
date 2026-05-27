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
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ipassplus.MainActivity
import com.app.ipassplus.R
import com.app.ipassplus.ui.dashboard.model.ScenariosItemModel
import com.app.ipassplus.databinding.FragmentDashboardBinding
import com.sdk.ipassplussdk.apis.ResultListener
import com.sdk.ipassplussdk.core.configProperties
import com.sdk.ipassplussdk.core.iPassSDKManger
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


    private val email = "uficotest123@yopmail.com"
    private val password = "Admin@123#"
    private val apptoken = "eyJhbGciOiJIUzI1NiJ9.dWZpY290ZXN0MTIzQHlvcG1haWwuY29tdGVzdCB1ZmljbyAgIGM4ZTQ4NTA0LTliZmYtNDgzZi1hMmU4LTRkMzFmNmI1OGI2ZQ.DYS4QNo1oR1xyvh0ZWK5vCx1UazdBR1iEVNcbZl4pPo"



    /*
    private val email = "localadmin@yopmail.com"
    private val password = "Admin@123#"
    private val apptoken = "eyJhbGciOiJIUzI1NiJ9.bG9jYWxhZG1pbkB5b3BtYWlsLmNvbWxvY2FsIGFkbWluICAgMzAzYjllYTgtNGNhZC00ZWY5LTgzZWItNjRmYjYxYzVjYzNi.3O4jeH15phcbBXTSoG3YPKvn33v6JNhIcS13c71CEgI"
     */



    val phoneNumber = "7894563210"
    private var flowId = "10032"
 // val flowId = "10016"
    val socialMediaEmail = "ipassmobisdk@yopmail.com"
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

//        configProperties.needHologramDetection(true)
        /*
        val scenarioList = arrayListOf(
            ScenariosItemModel(R.drawable.full2,"Full Processing",getString(R.string.processing_scenario_for_obtaining_all_document_data),true),
            ScenariosItemModel(R.drawable.bankkk, "Bank Card",getString(R.string.procesing_scenario_for_obtaning_bank_card_data),false),
            ScenariosItemModel(R.drawable.mrz, "MRZ",getString(R.string.procesing_scenario_for_obtaning_mrz_n_data),false),
            ScenariosItemModel(R.drawable.barcode, "Barcode",getString(R.string.procesing_scenario_for_obtaning_barcode_ndata),false),
            ScenariosItemModel(R.drawable.visual, "Visual OCR",getString(R.string.procesing_scenario_for_obtaning_nvisaul_zone_ocr_results),false)
        )
         */

        // new updates
        val scenarioList = arrayListOf(
            ScenariosItemModel(R.drawable.ic_full_processing,"Full Processing",getString(R.string.description_flow_full_processing),true),
            ScenariosItemModel(R.drawable.ic_idv_liveness_aml, "IDV + Liveness + AML",getString(R.string.description_flow_user_document_authenticity_liveness_face_matching_aml),false),
            ScenariosItemModel(R.drawable.ic_id_verification_aml, "IDV + AML",getString(R.string.description_flow_document_authenticity_aml),false),
            ScenariosItemModel(R.drawable.ic_id_verification, "IDV + Liveness",getString(R.string.description_flow_document_authenticity_user_liveness_face_matching),false),
            ScenariosItemModel(R.drawable.ic_id_verification, "IDV",getString(R.string.description_flow_document_authenticity),false)
        )

        adapter = ScenariosListAdapter(scenarioList,requireContext())
        adapter.setOnClickListener(this)

        binding.rvitems.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@DashboardFragment.adapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onScenarioPickClick(position: Int, model: ScenariosItemModel) {
        //  new updates
        //** send the flow id in api on basis of selected position
        if (position == 0){
            //**  Full Processing (10031)
            flowId = "10031"
        }else  if (position == 1){
            //**  Id verification + Liveness + AML (10032)
            flowId = "10032"
        }else  if (position == 2){
            //** Id verification + AML (10015)
            flowId = "10015"
        } else  if (position == 3){
            //**  Id verification + Liveness (10011)
            flowId = "10011"
        }else  if (position == 4){
            //**  Id verification  (10016)
            flowId = "10016"
        }else{

        }

        Log.e("call","flowId::  "+flowId + "    "+"position"+" "+position)

        iPassSDKManger.startScanningProcess(
            context = requireContext(),
            email = email,
            userToken = MainActivity.userToken,
            appToken = apptoken,
            socialMediaEmail = socialMediaEmail,
            phoneNumber = phoneNumber,
            flowId =  flowId,
            bindingView = binding.root as ViewGroup
        ) {
            status, message ->
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
        iPassSDKManger.getDocumentScannerData(requireContext(), apptoken, object : ResultListener<TransactionDetailResponse> {
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
}