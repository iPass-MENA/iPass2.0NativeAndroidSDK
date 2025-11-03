package com.app.ipassplus
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.app.ipassplus.Utils.Constants.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
import com.app.ipassplus.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.ipassplus.ui.base.BaseActivity
import com.sdk.ipassplussdk.apis.ResultListener
import com.sdk.ipassplussdk.core.Consumption
import com.sdk.ipassplussdk.core.DataBaseDownloading
import com.sdk.ipassplussdk.core.configProperties
import com.sdk.ipassplussdk.core.iPassSDKManger
import com.sdk.ipassplussdk.enums.DatabaseType
import com.sdk.ipassplussdk.model.response.authentication.AuthenticationResponse
import com.sdk.ipassplussdk.model.response.consumption.CustomerAccessResponse
import com.sdk.ipassplussdk.resultCallbacks.InitializeDatabaseCompletion

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var progressDialog: AlertDialog
    private lateinit var splitInstallManager: SplitInstallManager
    private val coreModule = "document_reader_sdk"
//    private val email = "testingonprem123@yopmail.com"
//    private val email = "mrverma91378@gmail.com"
//private val email = "testonpremcust123@yopmail.com"

    // previous

//   private val email = "ipassandhar@yopmail.com"
//   private val password = "Admin@123#"


    /*
     private val email = "testcs@yopmail.com"
    private val password = "Admin@12345#"
     */

 //   private val email = "anmol-rana@csgroupchd.com"
 //   private val password = "Anmol@1234#"

    private val email = "mobtest123@yopmail.com"
    private val password = "Admin@123#"

//    private val email = "idvtest123@yopmail.com"
//    private val password = "Admin@123#"

    companion object {
        var userToken = ""
    }


    private fun findNavController():NavController? {
        val navHostFragment = (this as? MainActivity)?.supportFragmentManager?.findFragmentById(R.id.fragment_container) as? NavHostFragment
        return navHostFragment?.navController
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        splitInstallManager = SplitInstallManagerFactory.create(this)
//        loadModule(coreModule)

//        progressDialog = showProgressDialog(this@MainActivity, "Initializing")
//        iPassSDKManger.baseUrl = "http://192.168.14.122/node/api/v1/ipass/"



        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 3. Handle insets: push BottomNavigationView above gesture nav bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationView) { _, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            binding.bottomNavigationView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = systemBarsInsets.bottom
            }

            // Add top padding to avoid status bar overlap in fragment container
            binding.fragmentContainer.setPadding(
                0,
                systemBarsInsets.top,
                0,
                0
            )

            insets
        }


        initNavigation()

//      dynamicDb()
        preProcessedDb()

       // data()
       // transactionData()
    }
//http://192.168.14.20/
    @RequiresApi(Build.VERSION_CODES.O)
    private fun preProcessedDb() {
        DataBaseDownloading.initializePreProcessedDb(this, serverUrl = "", dbName = DatabaseType.FULL_DB, completion = object: InitializeDatabaseCompletion {
            override fun onProgressChanged(progress: Int) {
//                progressDialog.setTitle("Downloading database $progress%")
                Log.e("onProgressChanged", "$progress")
            }

            override fun onCompleted(
                status: Boolean,
                message: String?
            ) {
//                progressDialog.hide()
                Log.e("onCompleted", message!!)
//               Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                if (status) getToken()
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dynamicDb() {

        DataBaseDownloading.initializeDynamicDb(this, serverUrl = "http://192.168.14.20/", completion = object: InitializeDatabaseCompletion {
            override fun onProgressChanged(progress: Int) {
//                progressDialog.setTitle("Downloading database $progress%")
                Log.e("onProgressChanged", "$progress")
            }

            override fun onCompleted(
                status: Boolean,
                message: String?
            ) {
//                progressDialog.hide()
                Log.e("onCompleted", message!!)
//               Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                if (status) getToken()
            }

        })

    }


    private fun loadModule(name: String) {
        val request = SplitInstallRequest.newBuilder()
            .addModule(name)
            .build()

        splitInstallManager.startInstall(request)
            .addOnFailureListener {
                Log.e("splitInstallManager", it.message!!)
            }
    }
    
        fun showProgressDialog(context: Context, msg: String): AlertDialog {
        val dialog = MaterialAlertDialogBuilder(context, R.style.Theme_MyApp_Dialog_Alert)
        dialog.background = ResourcesCompat.getDrawable(context.resources, R.drawable.rounded, context.theme)
        dialog.setTitle(msg)

        // Inflate a custom layout for the dialog
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.simple_dialog, null)
        dialog.setView(dialogView)

        dialog.setCancelable(false)

        return dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getToken() {
        configProperties.needHologramDetection(false)
        iPassSDKManger.UserOnboardingProcess(this@MainActivity, email, password, completion = object : ResultListener<AuthenticationResponse> {
            override fun onSuccess(response: AuthenticationResponse?) {
                val authToken = response?.user?.token!!
                Log.e("sdfsdf", authToken)
                Companion.userToken = authToken
            }

            override fun onError(exception: String) {
                Toast.makeText(this@MainActivity, exception, Toast.LENGTH_SHORT).show()
            }
        })
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(
            binding.bottomNavigationView,
            navController
        )

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.label?.equals("DashboardFragment")!!) {
                binding.bottomNavigationView.visibility = View.GONE
            }
            else binding.bottomNavigationView.visibility = View.GONE
        }

            binding.bottomNavigationView.setOnNavigationItemReselectedListener {
                when(it.itemId) {
                    R.id.dashboardFragment -> {
                        recognizeImage()
                    }
                    R.id.cameraFragment -> {
//                        iPassSDKManger.startScanningProcess(this)
                    }
                    R.id.menuFragment -> {
                    }
                }
            }

            binding.bottomNavigationView.setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.dashboardFragment -> {
                        findNavController()?.navigate(R.id.dashboardFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.cameraFragment -> {
//                        iPassSDKManger.startScanningProcess(this)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menuFragment -> {
                        findNavController()?.navigate(R.id.menuFragment)
                        return@setOnItemSelectedListener true
                    }
                    else -> {return@setOnItemSelectedListener false}
                }
            }
    }

    fun recognizeImage() {
        val readImagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(this, readImagePermission)
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(readImagePermission),
                PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        } else
            createImageBrowsingRequest()
    }

    private fun createImageBrowsingRequest() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        imageBrowsingIntentLauncher.launch(Intent.createChooser(intent, "Select Picture"))
    }
    @SuppressLint("WrongConstant")
    @Transient
    val imageBrowsingIntentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.let { intent ->
                val imageUris = ArrayList<Uri>()
                if (intent.clipData == null) {
                    intent.data?.let { uri ->
                        imageUris.add(uri)
                    }
                } else {
                    intent.clipData?.let { clipData ->
                        for (i in 0 until clipData.itemCount) {
                            imageUris.add(clipData.getItemAt(i).uri)
                        }
                    }
                }
            }
        }
    }
}


