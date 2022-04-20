package com.parallax.partyapp.views.AppFlow

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.ExifInterface
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.MediaController
import android.widget.Toast
import com.google.gson.Gson
import com.kaopiz.kprogresshud.KProgressHUD
import com.parallax.partyapp.Api.ApiServiceWithFileUpload
import com.parallax.partyapp.Models.CreatePostBodyModel
import com.parallax.partyapp.Models.ResponseCreatePost
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.MEDIA_TYPE_PHOTO
import com.parallax.partyapp.Utils.MEDIA_TYPE_VIDEO
import com.parallax.partyapp.Utils.RuntimeData
import com.parallax.partyapp.Utils.ToastUtils
import com.parallax.partyapp.views.Adapter.MultiImageAdapter
import com.parallax.partyapp.views.BaseActivity
import com.parallax.partyapp.views.RegistrationFlow.ExampleBottomSheetDialog
import com.parallax.partyapp.views.filterUtil.FilterActivity
import com.vincent.filepicker.ToastUtil
import createImageFile
import getBitmapFromUri
import getMimeType
import hideKeyboard
import kotlinx.android.synthetic.main.activity_maida.*
import rotateImage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MediaActivity : BaseActivity(), ExampleBottomSheetDialog.BottomSheetListener {
    override fun onCameraClicked() {
        dispatchTakePictureIntent()
    }

    override fun onFileClicked() {
        performFileSearch()
    }

    companion object {
        val KEY_BODY_CREATE_POST = "KEY_BODY_CREATE_POST"

        var postFeeligs: String? = null
        var postActivity: String? = null
    }

    private val PHOTO_EDITOR_REQUEST_CODE = 987

    private lateinit var multiImageAdapter: MultiImageAdapter
    val REQUEST_TAKE_PHOTO = 2
    val READ_REQUEST_CODE: Int = 42

    var currentPhotoUri: Uri? = null
    var currentPhotoFile: File? = null

    var objBodyCreatePost: CreatePostBodyModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maida)

        if (objBodyCreatePost == null) {
            objBodyCreatePost =
                Gson().fromJson(
                    intent?.getStringExtra(KEY_BODY_CREATE_POST),
                    CreatePostBodyModel::class.java
                )
        }

        RuntimeData.latLng = null


        if (objBodyCreatePost?.type == MEDIA_TYPE_PHOTO) {
            recycler_view.visibility = View.VISIBLE
            iv_post_image.visibility = View.GONE
            vv_post_vedio.visibility = View.GONE

            multiImageAdapter = MultiImageAdapter(this, objBodyCreatePost?.post_files!!)

            recycler_view.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(
                    applicationContext,
                    androidx.recyclerview.widget.RecyclerView.HORIZONTAL,
                    false
                )
            recycler_view.adapter = multiImageAdapter

//            iv_post_image.setImageURI(Uri.parse(objBodyCreatePost?.post_file))
        } else if (objBodyCreatePost?.type == MEDIA_TYPE_VIDEO) {
            recycler_view.visibility = View.GONE
            iv_post_image.visibility = View.GONE
            vv_post_vedio.visibility = View.VISIBLE

            iv_add_image.visibility = View.GONE

            vv_post_vedio.setVideoPath(objBodyCreatePost?.video_file)

            val mediaController = MediaController(this@MediaActivity)
            mediaController.setAnchorView(vv_post_vedio)
            vv_post_vedio.setMediaController(mediaController)

            vv_post_vedio.seekTo(1000)
        }

        iv_add_image.setOnClickListener {
            var bottomSheet = ExampleBottomSheetDialog()
            bottomSheet.mListener = this
            bottomSheet.show(supportFragmentManager, "exampleBottomSheet")
        }


        initClickListeners()

    }

    override fun onResume() {
        super.onResume()

        RuntimeData.latLng?.let {
            tv_tag_location.text = RuntimeData.latLng?.address
        }

        if (TagFriendsActivity.userIdForTag.isNotEmpty()) {
//            tv_tag_friend.text = "${TagFriendsActivity.userIdForTag.size} peoples"
            tv_tag_friend.text =
                getString(R.string.xx_people, TagFriendsActivity.userIdForTag.size.toString())
        }

        if (!postFeeligs.isNullOrBlank() && !postActivity.isNullOrBlank()) {
            tv_feel_activity.text = "$postFeeligs, $postActivity"
        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(this)
        return super.dispatchTouchEvent(ev)
    }


    override fun onDestroy() {
        super.onDestroy()

        TagFriendsActivity.userIdForTag.clear()
        postFeeligs = null
        postActivity = null
    }

    private fun initClickListeners() {

//        iv_add_image.setOnClickListener {
//            performFileSearch()
//        }

        btn_upload.setOnClickListener {

            if (ratingBarCrowed.rating.toDouble() == 0.toDouble()) {
                ToastUtils.getInstance(applicationContext)?.showToast(getString(R.string.please_rate_the_crowed))
                return@setOnClickListener
            }

            if (ratingBarFun.rating.toDouble() == 0.toDouble()) {
                ToastUtils.getInstance(applicationContext)?.showToast(getString(R.string.please_rate_the_fun))
                return@setOnClickListener
            }

            if (RuntimeData.latLng == null) {
                ToastUtils.getInstance(applicationContext)?.showToast(getString(R.string.please_tag_a_location))
                return@setOnClickListener
            }

            if (!et_text.text.toString().isNullOrBlank()) {
                objBodyCreatePost?.post_text = et_text.text.toString()
            }

            objBodyCreatePost?.crowd_count = ratingBarCrowed.rating.toDouble()
            objBodyCreatePost?.fun_count = ratingBarFun.rating.toDouble()
            objBodyCreatePost?.tag_friends = Gson().toJson(TagFriendsActivity.userIdForTag)

            if (!postFeeligs.isNullOrBlank() && !postActivity.isNullOrBlank()) {
                objBodyCreatePost?.feel = postFeeligs
                objBodyCreatePost?.activity = postActivity
            }

            objBodyCreatePost?.location = RuntimeData.latLng?.address.toString()
            objBodyCreatePost?.latitude = RuntimeData.latLng?.lat.toString()
            objBodyCreatePost?.longitudes = RuntimeData.latLng?.lng.toString()

            ApiServiceWithFileUpload(this).callUploadApi(objBodyCreatePost!!,
                object : ApiServiceWithFileUpload.UploadListener {
                    override fun onProgress() {

                    }

                    override fun onCompleted(bodyAsString: String) {

                        if (bodyAsString != null) {
                            val responseCreatePost =
                                Gson().fromJson(bodyAsString, ResponseCreatePost::class.java)

                            if (responseCreatePost.meta.status == 200) {

                                val dialog = Dialog(this@MediaActivity)
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                dialog.setCancelable(false)
                                dialog.setContentView(R.layout.dialog_post_successfull)


                                val tvOk = dialog.findViewById<Button>(R.id.btn_ok)

                                tvOk.setOnClickListener {
                                    dialog.dismiss()

                                    Intent(applicationContext, MainActivity::class.java).let {
                                        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        startActivity(it)
                                    }
                                }


                                dialog.show()
                            }
                        }

                    }

                    override fun onError(message: String) {

                    }
                })
        }


        btnBack.setOnClickListener {
            onBackPressed()
        }

        layoutFeeling.setOnClickListener {
            startActivity(Intent(applicationContext, HowAreYouFeelingActivity::class.java))
        }

        layoutTagFriend.setOnClickListener {
            startActivity(Intent(applicationContext, TagFriendsActivity::class.java))
        }

        layoutLocation.setOnClickListener {
            startActivity(Intent(applicationContext, LocationActivity::class.java))
        }
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            val photoFile: File? = try {
                createImageFile(applicationContext!!)
            } catch (ex: IOException) {
                // Error occurred while creating the File
                null
            }

            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    applicationContext!!,
                    "com.parallax.partyapp.fileprovider",
                    it
                )
                currentPhotoFile = it
                currentPhotoUri = photoURI
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }


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


    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {

        if (requestCode == FilterActivity.REQUEST_FILTER_PHOTO && resultCode == Activity.RESULT_OK) {

            try {

                bitmapWriteToFile(FilterActivity.resultBitmap, addPhotoAndNotifyAdapter)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


        //Capture Image
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            val imageUri = currentPhotoUri


            val ei = ExifInterface(currentPhotoFile?.absolutePath)
            val orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            var rotatedBitmap: Bitmap? = null
            when (orientation) {

                ExifInterface.ORIENTATION_ROTATE_90 -> {
                    rotatedBitmap =
                        rotateImage(getBitmapFromUri(applicationContext!!, currentPhotoUri!!), 90f)
                    bitmapWriteToFile(rotatedBitmap, goFilterActivity)
                }


                ExifInterface.ORIENTATION_ROTATE_180 -> {
                    rotatedBitmap =
                        rotateImage(getBitmapFromUri(applicationContext!!, currentPhotoUri!!), 180f)
                    bitmapWriteToFile(rotatedBitmap, goFilterActivity)
                }


                ExifInterface.ORIENTATION_ROTATE_270 -> {
                    rotatedBitmap =
                        rotateImage(getBitmapFromUri(applicationContext!!, currentPhotoUri!!), 270f)
                    bitmapWriteToFile(rotatedBitmap, goFilterActivity)
                }


                ExifInterface.ORIENTATION_NORMAL -> {
//                    rotatedBitmap = getBitmapFromUri(applicationContext!!, currentPhotoUri!!);
                    goFilterActivity()
                }

                ExifInterface.ORIENTATION_UNDEFINED -> {
                    goFilterActivity()
                }
            }

        }

        // Pick file from storage
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultIntent.getData().
            resultIntent?.data?.also { uri ->
                Log.i("onActivityResult", "Uri: $uri")

                val mimiType = getMimeType(uri)

                val mediaType = if (mimiType == "jpg" || mimiType == "jpeg" || mimiType == "png") {
                    MEDIA_TYPE_PHOTO
                } else if (mimiType == "mp4") {
                    MEDIA_TYPE_VIDEO
                } else {
                    -1
                }


                if (mediaType == MEDIA_TYPE_VIDEO) {

                    objBodyCreatePost?.post_files?.add(uri.toString())
                    multiImageAdapter?.notifyDataSetChanged()

                } else if (mediaType == MEDIA_TYPE_PHOTO) {
                    val photoFile: File? = try {
                        createImageFile(applicationContext!!)
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        null
                    }

                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            applicationContext!!,
                            "com.parallax.partyapp.fileprovider",
                            it
                        )

                        FileOutputStream(photoFile).use { out ->
                            getBitmapFromUri(applicationContext!!, uri).compress(
                                Bitmap.CompressFormat.PNG,
                                100,
                                out
                            ) // bmp is your Bitmap instance
                            // PNG is a lossless format, the compression factor (100) is ignored
                        }

                        currentPhotoFile = photoFile

                        currentPhotoUri = uri

                        Intent(applicationContext, FilterActivity::class.java).let {
                            FilterActivity.currentPhotoUri = photoURI
                            startActivityForResult(it, FilterActivity.REQUEST_FILTER_PHOTO)

                        }
                    }


                }
            }
        }
    }

    var addPhotoAndNotifyAdapter = {
        if (currentPhotoFile != null) {
            val imageUri = currentPhotoUri
            objBodyCreatePost?.post_files?.add(imageUri.toString())
            multiImageAdapter.notifyDataSetChanged()
        }
    }

    var goFilterActivity = {
        Intent(applicationContext, FilterActivity::class.java).let {
            FilterActivity.currentPhotoUri = currentPhotoUri!!
            startActivityForResult(it, FilterActivity.REQUEST_FILTER_PHOTO)
        }
    }

    private fun bitmapWriteToFile(bitmapToRotate: Bitmap, goDestinationActivity: () -> Unit) {

        val kProgressHUD = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setAnimationSpeed(1)
            .setDimAmount(0.5f)

        kProgressHUD?.show()

        AsyncTask.execute {
            FileOutputStream(currentPhotoFile).use { out ->
                bitmapToRotate?.compress(
                    Bitmap.CompressFormat.PNG,
                    100,
                    out
                ) // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            }

            runOnUiThread {
                kProgressHUD?.dismiss()
                goDestinationActivity()
            }

        }


    }
}
