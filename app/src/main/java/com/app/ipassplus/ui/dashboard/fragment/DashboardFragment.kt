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

    private val email = "mobtest123@yopmail.com"
    private val password = "Admin@123#"

 //      private val email = "idvtest123@yopmail.com"
 //      private val password = "Admin@123#"

//    private val apptoken = "eyJhbGciOiJIUzI1NiJ9.dGVzdGluZ29ucHJlbTEyM0B5b3BtYWlsLmNvbWlwYXNzIG9ucHJlbSAgIDE0MGM1NTliLWRhNWUtNDA0ZC05MzQwLTUzOThjZGQwMDZhNg.ck8friY7SZ4DQhow-_3G3RO-fy_rHNsEbrydZ97ucsw"
//    private val apptoken = "eyJhbGciOiJIUzI1NiJ9.dGVzdG9ucHJlbWN1c3QxMjNAeW9wbWFpbC5jb21pcGFzcyBvbnByZW0gICAxYjAzMGViMC1hNTRmLTQyMWMtYmJhNi01MmRlMWJiODdmOTc.bCAu983NmiSNhG-oO7LNnZM7smhj9FRMPBjQV6HJHOE"
//    private val apptoken = "eyJhbGciOiJIUzI1NiJ9.aXBhc3NhbmRoYXJAeW9wbWFpbC5jb21tb2JpbGUgdGVhbSAgIDFhYzlkYzYyLWFjZmUtNDEwOC04Y2Q2LTExY2I0OTA5NDFmMw.jTDHn4B6yOaPGpK0y2G2vvSxTcybaV7icfGkGltIelo"
 //   private val apptoken = "eyJhbGciOiJIUzI1NiJ9.dGVzdGNzQHlvcG1haWwuY29tVGVzdCBDcyAgIDgzN2I4OWRlLTU1ZGEtNGVjNy05NTBmLTE5NDQwY2RjZWYyZA.nV10BYNyMIu5u7kBwU3kMkr4wwi4JwHBeABU5sl_F4Y"
 //   private val apptoken = "eyJhbGciOiJIUzI1NiJ9.YW5tb2wtcmFuYUBjc2dyb3VwY2hkLmNvbUFubW9sIFJhbmEgICBiNmFjZGUxOC1iMzQzLTQzNDQtYTg0NC1kNWM0ZWFjYWQ4ZjA.mq9Kb4JTQtwBf91f7yOYV2KuhQ9BK0GvQezTSA4VvOU"
    private val apptoken = "eyJhbGciOiJIUzI1NiJ9.bW9idGVzdDEyM0B5b3BtYWlsLmNvbXRlc3QgYXBpJ3MgICAzNDMzYWZlZC0zNWZkLTQ3MGMtOTNlYy1lYjBjN2I1Y2VlZjI.2XbfGso6YVZ5xhbM74Ye1NnOzPvKSCv20ceT9GzR3HQ"
 //   private val apptoken = "eyJhbGciOiJIUzI1NiJ9.aWR2dGVzdDEyM0B5b3BtYWlsLmNvbUFqYXkga3VtYXIgICA3ZmIxNTVlNS1kMGJkLTQ1YjYtOTI5NC1iNjNiODI1ZmZmMjE.QsHumaJxYjO4ufrGyGSgWo168fk5UV3qfCxsULyGsZc"
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