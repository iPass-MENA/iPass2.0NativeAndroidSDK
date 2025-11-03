<p align="center">
  <img src="https://github.com/yazanalqasem/iPass2.0NativeAndroidSDK/blob/main/IPassPlusSdk/src/main/res/drawable/ipass_logo.png" />
</p>



[![License](https://img.shields.io/badge/License-Commercial-blue.svg)](LICENSE)
[![iPass](https://img.shields.io/badge/iPass-lightgreen)](https://ipass-mena.com)
![API Doc](https://img.shields.io/badge/API%20doc-v2.16-green)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9-red.svg)
![platform](https://img.shields.io/badge/Platforms-Android-orange)
![eKYC](https://img.shields.io/badge/eKYC-red)
![pod](https://img.shields.io/badge/sdk-v2.16-yellow)

# Table of Contents
- [Overview](#overview)
- [Get Started](#steps-of-using-ipass-android-package)
  - [Integrate Package into the App](#integrate-package-into-the-app)
  - [Permissions](#permissions)
  - [Add NFC Compatibility](#add-nfc-compatibility)
  - [Initialize Database](#initialize-database)
  - [Get User Login Token](#get-user-login-token)
  - [Get Supported Flows](#get-supported-flows)
  - [Document Scanning](#document-scanning)
  - [Get Document Data](#get-document-data)
  - [SDK Properties](#sdk-properties)
  - [Language Localization](#add-multiple-languages-optional)
  - [Configuring Webhook URL in Your iPass Account](configuring-webhook-url-in-your-ipass-account)
- [Support](#support)
- [Licenses](#licenses)
- [Contact](#contact)
- [Copyright](#copyright)


#### Updates in new version
-  Updated user liveness

# Overview
AI-powered identity verification, eKYC, and
transaction monitoring

In today’s digital economy, fraudsters and money
launderers have no place. To avoid fraud and financial
crime, internet firms must know and trust that their
clients are who they say they are – and that they will
remain trustworthy.
<p align="center">
  <img src="https://github.com/yazanalqasem/iPass2.0NativeAndroidSDK/blob/main/IPassPlusSdk/src/main/res/drawable/overview.png" />
</p>
---

## Steps of using iPASS Android Package

To explain how a user can use the iPASS Package in steps, you can outline the process as follows:

## Steps to use iPASS Android Package

### Integrate Package into the App

In this step User Will add the IPass SDK inside the app's gradle file:
```kotlin
    implementation("com.github.yazanalqasem:iPass2.0NativeAndroidSDK:2.16")
    implementation("com.github.yazanalqasem:iPass2.0CoreAndroidSDK:2.16")
```

Add these lines in your settings.gradle file
```kotlin
        dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            google()
            mavenCentral()
            maven {
                url = uri("https://jitpack.io")
            }
            maven {
                url =uri("https://maven.regulaforensics.com/RegulaDocumentReader")
            }
        }
    }
```

Add this snippet to build. gradle file in your project.

```kotlin
        android {
        compileOptions {
            isCoreLibraryDesugaringEnabled = true
                    }
                }
```

-----

# Permissions

### Configure Permissions in manifest file

You need to specify required permissions in manifest file to enable the necessary device features:

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-feature android:name="android.hardware.camera" />
    <uses-feature android:name="android.hardware.camera.autofocus" />
    <uses-permission android:name="android.permission.NFC" />

-----

### Add NFC Compatibility
- Check NFC is enabled in device settings
-----

### Initialize Database and On-Prem Server setup

- iPass supports On-Prem server integration. To use an on-prem server, provide the server URL in the serverUrl parameter.
  - If serverUrl is an empty string, the data will be saved on the iPass server.
  - If serverUrl contains a valid URL, the data will be saved on your on-prem server.

- To start the process user need to download the database. iPass sdk supports two type of database systems.

  - Pre-packaged Database
  - Dynamic Database

### Pre-packaged Database:
This type of database is bundled within the SDK itself. It is a pre-configured and read-only database that comes as part of the app's installation package. Since the database is local to the app, querying this database is generally faster, as it does not involve network latency. This is a custom database designed to meet specific requirements. If you need a custom database tailored to your needs, you can request one by contacting our support team at info@ipass-mena.com.

### Dynamic Database:
This type of database is not included in the initial app package but is instead downloaded from a remote server when the app is launched or when certain conditions are met. The server-side database can be updated independently of the app, allowing for more dynamic content and real-time data management. In this database downloading time depends on the internet speed.

 ----

### Pre-packaged Database Implementation:
```kotlin
        DataBaseDownloading.initialization(this, object: InitializeDatabaseCompletion {
            override fun onProgressChanged(progress: Int) {
                // get progress
            }

            override fun onCompleted(
                status: Boolean,
                message: String?
            ) {
                if (status) {
                    // Show message
                } else {
                    // Show error message
                }
            }

        })
```
- In the onCompleted method, when the status returns true, you can start the next step.
- If there is any error in the process it will return the status false and error message in error(String).
- Replace http://192.168.19.421/ with your actual on-prem server URL if applicable.


- In the Pre-packaged Database, system allows you to choose between three types of databases. Currently this database only allows to scan Jordanian ID cards as well as passports from other countries.

- DataBaseDownloading.availableDataSources.basicJordan
  - This database stores all types of documents for Jordan but only passports for other countries. It does not include authentication checks.

  - DataBaseDownloading.availableDataSources.fullAuthJordan
    - This database stores all types of documents for Jordan but only passports for other countries. It also includes authentication checks.

  - DataBaseDownloading.availableDataSources.fullDb
    - This database stores all types of documents for all countries. It does not include authentication checks.


- For authentication checks, you need to enable hologram option using the following code snippet. By default, this option is disabled:

```kotlin
configProperties.needHologramDetection(value = true)
```


### Dynamic Database Implementation:

 ```kotlin
            DataBaseDownloading.initializeDynamicDb(context = this, serverUrl = "http://192.168.19.421/", completion = object: InitializeDatabaseCompletion {
    override fun onProgressChanged(progress: Int) {
//get progress
    }

    override fun onCompleted(
        status: Boolean,
        message: String?
    ) {
        if (status) {
    //proceed to next step
}
    }

})
```
- Override function onProgressChanged can be used to track the downloading percentage.
- Once the database is downloaded 100% and status returns true in onCompleed method, user can start the next step.
- If there is any error in the process it will return the status false and error message in error(String).
- Replace http://192.168.19.421/ with your actual on-prem server URL if applicable.

-----

### Getting the User Login Token
- Retrieve User Login Token
  - Pass a valid email and password to authenticate the user and obtain the login token.

```kotlin
           iPassSDKManger.UserOnboardingProcess(context = this, email, password, object :
    ResultListener<AuthenticationResponse> {
    override fun onSuccess(response: AuthenticationResponse?) {
        val userToken = response?.user?.token!!
    }

    override fun onError(exception: String) {
// error message
    }
})
```
- Once the user is logged in user token need to save because this will be used in document scanning process

-----

### Get Supported Flows
```kotlin
      val getList:  Array<HashMap<:String, String>> = iPassSDKManger.getWorkFlows()
```
- Get the flowId from the list of supported flows which will be required for scanning process
- Sdk Supported Flows
  - Full Processing(10031)
  - Id verification + Liveness + AML(10032)
  - Id verification + AML(10015)
  - Id verification + Liveness(10011)
- In Full Process(10031), Social media email and phone number is required

### Document Scanning

- User can scan various types of documents.
- Users can scan both the front and back sides of documents, but it totally depends on the document type.
```kotlin
        iPassSDKManger.startScanningProcess(
    requireContext(),
    email,
    userToken,
    apptoken,
    socialMediaEmail,
    phoneNumber,
    workflowId,
    binding.root as ViewGroup) {
        status, message ->
    if (status) {
        getDocData(model.workflow)
    } else {
        Log.e("startScanningProcess", message)
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }
}
```
- usertoken will be the login token
- appToken will be the auth token provided by Admin
- flowId will be the id selected by the user from above step.
- After the scanning process, the response can be obtained from getDocumentScannerData method.
-----

### Get Document Data :

This Method Returns data scanned from Documents.
```kotlin
      iPassSDKManger.getDocumentScannerData(requireContext(), apptoken, object :
      ResultListener<TransactionDetailResponse> {
          override fun onSuccess(response: TransactionDetailResponse?) {
            if (response?.Apistatus!!) {
              val data = response.data!!
            }
          }

          override fun onError(exception: String) {
            Toast.makeText(context, exception, Toast.LENGTH_SHORT).show()
          }
        }) 
```
- onSuccess - "response.data" object will return the required json response
- onError - "exception" will return the error message in String
-----

### SDK Properties

Enable Hologram Detection

 ```kotlin
configProperties.needHologramDetection(true)
```

Disable Hologram Detection

 ```kotlin
configProperties.needHologramDetection(false)
```

- "needHologramDetection" property is used for Authenticity checks like detection of Electronic Device, Optically Variable Ink, Multiple Laser Images, Image Patterns.
- By default hologram detection is disabled. you can enable it by passing the true in hologram detection property.
-----

### Add Multiple Languages (Optional)
Applications supports following langauges
- Arabic
- English
- French
- German
- Kurdish
- Spanish
- Turkish
- Urdu

-----

### Reduce APK Size

To reduce the APK size, follow these steps:

1. In android studio, Select File > New > New Module from the menu bar. In the Create New Module dialog, select Dynamic Feature Module and click Next.

2. On the Configure your new module screen, give your module a name(iPassSdk).

3. On the Configure your new module screen, specify the module title(iPass).

4. Check the Enable on-demand box. Hit Finish and wait for the project to sync.

5. Now add the below mentioned line in the dynamic module's (iPassSdk) build gradle file and sync project.
```kotlin
           implementation("com.github.yazanalqasem:iPass2.0CoreAndroidSDK:2.16")
```
     Note : Remove this line from app's build gradle file

6. Add these lines in your activity
```kotlin
             private var splitInstallManager: SplitInstallManager? = null
    splitInstallManager = SplitInstallManagerFactory.create(this)
    val request = SplitInstallRequest.newBuilder()
                    .addModule(name)
                    .build()
                  splitInstallManager?.startInstall(request)
                  ?.addOnSuccessListener {
          // Packages Installed (Initialise Database Here)
                    }
                  ?.addOnFailureListener {
          // Packages Installation failed!
                  }
```



### Configuring Webhook URL in Your iPass Account

Your iPass account can be set up to send an HTTP POST request with JSON data to a specified URL upon the completion of an onboarding process. This URL can be configured in the account profile section of the iPass web application.

Steps to Configure the Webhook URL:

- Log in to your iPass dashboard.
- Navigate to the "My Profile" section
- Click the "Edit" button, enter your webhook URL in the designated field, and click "Update." Your webhook URL will now be added.

We also provide the webhook signature verification here are the instructions to generate the signature in the node js.

const generateSignature = (uid, secret) => { return crypto.createHmac('sha256', secret) .update(JSON.stringify(data)) .digest('hex'); };

const uid = { uid(uid that will sent you on webhook) }; const secretKey = 'secret-key which we share you securely'; const signature = generateSignature(uid, secretKey);

From our side you can get the signature in the header of webhook.



# Support
Please refer to our [support policy](https://ipass-mena.com/contact/) for more information about Mobile SDK support.


## Licenses
The software contains third-party open source software. For more information, please see [license](LICENSE).


## Contact
If you have any questions regarding our implementation guide please contact iPass-mena Customer Service. The iPass-mena online helpdesk contains a wealth of information regarding our service including demo videos, product descriptions, FAQs and other things that may help to get you started with iPass-mena.

## Copyright
&copy; Copyright (c) 2024 iPass

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.For SDK license key you need to contact on info@ipass-mena.com


