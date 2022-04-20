package com.parallax.partyapp.views.RegistrationFlow

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.widget.PopupMenu
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.parallax.partyapp.Api.ApiServiceWithFileUpload
import com.parallax.partyapp.Data.SPreferences
import com.parallax.partyapp.Models.SetProfileBodyModel
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.ToastUtils
import com.parallax.partyapp.views.AppFlow.MainActivity
import com.parallax.partyapp.views.BaseActivity
import com.vincent.filepicker.ToastUtil
import hideKeyboard
import kotlinx.android.synthetic.main.activity_edite_profle_screen.*
import loadImage
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import showAlertDialog
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class EditeProfleScreenActivity : BaseActivity(), ExampleBottomSheetDialog.BottomSheetListener {

    companion object {
        val KEY_COME_FROM_REGISTRATION = "KEY_COME_FROM_REGISTRATION"
    }

    val REQUEST_IMAGE_CAPTURE = 1
    private val READ_REQUEST_CODE: Int = 42
    var imageUri: File? = null

    var dateTime: String? = null

    val setProfileBodyModel = SetProfileBodyModel()

    var objUser = SPreferences.getUserProfileObj()

    var isComeFromRegistration = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edite_profle_screen)

        isComeFromRegistration = intent?.getBooleanExtra(KEY_COME_FROM_REGISTRATION, false)!!


        objUser?.let {
            et_name.setText(objUser?.response?.user_data?.name)
            et_gender.text = objUser?.response?.user_data?.gender
            et_phone.setText(objUser?.response?.user_data?.phone)
            et_dob.text = objUser?.response?.user_data?.date_of_birth
            et_bio.setText(objUser?.response?.user_data?.bio)

            loadImage(applicationContext, objUser?.response?.user_data?.image_url, iv_profile, true)
        }


        et_gender.setOnClickListener {
            val popupMenu = PopupMenu(this, et_gender)
            popupMenu.menu.add(getString(R.string.male))
            popupMenu.menu.add(getString(R.string.female))
            popupMenu.show()

            popupMenu.setOnMenuItemClickListener {
                et_gender.text = it.title
                true
            }
        }

        et_dob.setOnClickListener { datePicker() }

        iv_profile.setOnClickListener {
            val bottomSheet = ExampleBottomSheetDialog()
            bottomSheet.mListener = this
            bottomSheet.show(supportFragmentManager, "exampleBottomSheet")
        }

        btnNext.setOnClickListener {

            when {
                et_name.text.toString().isNullOrBlank() ->
                    ToastUtils.getInstance(applicationContext)?.showToast(getString(R.string.please_enter_your_name))
                et_gender.text.toString().isNullOrBlank() ->
                    ToastUtils.getInstance(applicationContext)?.showToast(getString(R.string.please_enter_your_gender))
                et_phone.text.toString().isNullOrBlank() ->
                    ToastUtils.getInstance(applicationContext)?.showToast(getString(R.string.please_enter_your_phone_number))
                et_dob.text.toString().isNullOrBlank() ->
                    ToastUtils.getInstance(applicationContext)?.showToast(getString(R.string.please_enter_your_date_of_birth))
                et_bio.text.toString().isNullOrBlank() ->
                    ToastUtils.getInstance(applicationContext)?.showToast(getString(R.string.please_enter_your_bio))
                else -> {
                    setProfileBodyModel.name = et_name.text.toString()
                    setProfileBodyModel.gender = et_gender.text.toString()
                    setProfileBodyModel.phone = et_phone.text.toString()
                    setProfileBodyModel.date_of_birth = et_dob.text.toString()
                    setProfileBodyModel.bio = et_bio.text.toString()

                    setProfileBodyModel.image = imageUri

                    ApiServiceWithFileUpload(this).callUpdateProfileApi(
                        setProfileBodyModel,
                        object : ApiServiceWithFileUpload.UploadListener {
                            override fun onProgress() {

                            }

                            override fun onCompleted(bodyAsString: String) {
                                if (isComeFromRegistration) {
                                    startActivity(
                                        Intent(
                                            applicationContext,
                                            CongratulationsActivity::class.java
                                        )
                                    )
                                } else {
                                    showAlertDialog(
                                        this@EditeProfleScreenActivity,
                                        getString(R.string.profile_update_successfully)
                                    ) {
                                        Intent(applicationContext, MainActivity::class.java).also {
                                            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            startActivity(it)
                                        }
                                    }
                                }
                            }

                            override fun onError(message: String) {

                            }
                        })
                }
            }


        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(this)
        return super.dispatchTouchEvent(ev)
    }

    override fun onCameraClicked() {
        dispatchTakePictureIntent()
    }

    override fun onFileClicked() {
        performFileSearch()
    }


    private fun datePicker() {
        // Get Current Date
        val c = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                //                05/13/2010
                dateTime = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                et_dob.text = dateTime
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.show()
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }


    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    private fun performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            // Filter to only show results that can be "opened", such as a
            // file (as opposed to a list of contacts or timezones)
            addCategory(Intent.CATEGORY_OPENABLE)

            // Filter to show only images, using the image MIME data type.
            // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
            // To search for all documents available via installed storage providers,
            // it would be "*/*".
            type = "image/*"
        }

        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageUri = bitmapToFile(imageBitmap)

            imageUri?.also { uri ->
                Glide.with(this@EditeProfleScreenActivity)
                    .load(imageUri)
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(resources.getDrawable(R.drawable.ic_no_image))
                    .error(resources.getDrawable(R.drawable.ic_no_image))
                    .into(iv_profile)
            }
        }

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().


            val uri: Uri?
            if (data != null) {
                uri = data.data
                Log.i("imageUri", "Uri: " + uri!!.toString())


                var file: File? = null
                try {
                    file = createImageFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                var fOut: FileOutputStream? = null
                try {
                    fOut = FileOutputStream(file!!)

                    getBitmapFromUri(uri).compress(Bitmap.CompressFormat.PNG, 100, fOut)
                    fOut.flush()
                    fOut.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                imageUri = file


            }

            data?.data?.also { uri ->
                Glide.with(this@EditeProfleScreenActivity)
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(resources.getDrawable(R.drawable.ic_no_image))
                    .error(resources.getDrawable(R.drawable.ic_no_image))
                    .into(iv_profile)
            }
        }
    }

    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val parcelFileDescriptor: ParcelFileDescriptor =
            contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val externalFilesDir =
            applicationContext!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            externalFilesDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        val mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun bitmapToFile(bitmap: Bitmap): File {
        // Get the context wrapper
        val wrapper = ContextWrapper(applicationContext)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Return the saved bitmap uri
//        return Uri.parse(file.absolutePath)
        return file
    }

    @AfterPermissionGranted(1234)
    private fun requestPermissions() {
        val perms =
            arrayOf(Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            performFileSearch()
        } else {
            EasyPermissions.requestPermissions(this, "Please give all permissions", 1234, *perms)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
