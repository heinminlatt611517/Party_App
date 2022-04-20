package com.parallax.partyapp.views.Fragments


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.media.ExifInterface
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.kaopiz.kprogresshud.KProgressHUD
import com.parallax.partyapp.Api.ApiServiceWithFileUpload
import com.parallax.partyapp.Api.WebService
import com.parallax.partyapp.Models.*
import com.parallax.partyapp.R
import com.parallax.partyapp.Utils.*
import com.parallax.partyapp.interfaces.DataListener
import com.parallax.partyapp.interfaces.HomeItems
import com.parallax.partyapp.interfaces.OnListItemClickListener
import com.parallax.partyapp.views.Adapter.HomeAdapter
import com.parallax.partyapp.views.AppFlow.GoLiveActivity
import com.parallax.partyapp.views.AppFlow.MainActivity
import com.parallax.partyapp.views.AppFlow.MediaActivity
import com.parallax.partyapp.views.AppFlow.MediaDetailsActivity
import com.parallax.partyapp.views.RegistrationFlow.ExampleBottomSheetDialog
import com.parallax.partyapp.views.filterUtil.FilterActivity
import createImageFile
import getAddressByLatLng
import getBitmapFromUri
import getMimeType
import hideProgressBar
import kotlinx.android.synthetic.main.fragment_home.view.*
import rotateImage
import showAlertDialog
import showProgressBar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class HomeFragment : androidx.fragment.app.Fragment(),
    ExampleBottomSheetDialog.BottomSheetListener {

    private val PHOTO_EDITOR_REQUEST_CODE = 987

    private lateinit var viewGroup: ViewGroup


    lateinit var currentPhotoUri: Uri

    var currentPhotoFile: File? = null

    val REQUEST_VIDEO_CAPTURE = 1
    val REQUEST_TAKE_PHOTO = 2
    val READ_REQUEST_CODE: Int = 42

    val items = ArrayList<HomeItems>()

    var homeAdapter: HomeAdapter? = null

    var lat = 0.0
    var lng = 0.0

    var responsePosts: ResponsePosts? = null

    var selectedMedia = -1

    val SELECT_CAMERA = 0
    val SELECT_VIDEO = 1


    //Create Broadcast receiver
    private var locationBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            Log.d("lat", intent?.getDoubleExtra(LocationService.INTENT_DOUBLE_LAT, 0.0).toString())
            Log.d("lon", intent?.getDoubleExtra(LocationService.INTENT_DOUBLE_LON, 0.0).toString())
            Log.d(
                "accuracy",
                intent?.getFloatExtra(LocationService.INTENT_FLOAT_ACCURACY, 0f).toString()
            )

            lat = intent?.getDoubleExtra(LocationService.INTENT_DOUBLE_LAT, 0.0)!!
            lng = intent.getDoubleExtra(LocationService.INTENT_DOUBLE_LON, 0.0)

            RuntimeData.latLng2 = LatLng2(lat.toString(), lng.toString())

            getPosts()

            context?.unregisterReceiver(this)

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewGroup = inflater.inflate(R.layout.fragment_home, container, false) as ViewGroup

        homeAdapter = HomeAdapter(activity, items, object : OnListItemClickListener<HomeItems> {
            override fun onListItemClick(obj: HomeItems) {
                if (obj is ResponsePosts.Response.Post) {
                    Intent(context, MediaDetailsActivity::class.java).apply {
                        putExtra(MediaDetailsActivity.KEY_ITEM, Gson().toJson(obj))
                        startActivity(this)
                    }
                }
            }
        })

        viewGroup.tv_nopost.hide()
        viewGroup.tv_clear_filter.hide()

        viewGroup.recyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                context,
                androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
                false
            )
        viewGroup.recyclerView.adapter = homeAdapter


        initClickListeners()

        return viewGroup
    }

    override fun onResume() {
        super.onResume()

        responsePosts?.let {
            processResponse(it, true)
        }

        getPosts()

        context?.registerReceiver(
            locationBroadcastReceiver,
            IntentFilter(LocationService.BROADCAST_ACTION)
        )
        context?.startService(Intent(context, LocationService::class.java))

        if (RuntimeData.isComeFromFilter) {
            viewGroup.recyclerView.layoutManager?.scrollToPosition(0)
            RuntimeData.isComeFromFilter = false
        }
    }

    override fun onDestroy() {
        try {
            context?.unregisterReceiver(locationBroadcastReceiver)
        } catch (e: Exception) {
        }
        super.onDestroy()
    }

    override fun onCameraClicked() {
        when (selectedMedia) {
            SELECT_CAMERA -> dispatchTakePictureIntent()
            SELECT_VIDEO -> dispatchTakeVideoIntent()
        }
    }

    override fun onFileClicked() {
        performFileSearch()

    }

    private fun initClickListeners() {

        viewGroup.iv_open_camera.setOnClickListener {

            Toast.makeText(requireContext(),"Sorry something went wrong",Toast.LENGTH_LONG).show()


//            selectedMedia = SELECT_CAMERA
//            var bottomSheet = ExampleBottomSheetDialog()
//            bottomSheet.mListener = this
//            bottomSheet.show(childFragmentManager, "exampleBottomSheet")


        }

        viewGroup.iv_open_video.setOnClickListener {


            Toast.makeText(requireContext(),"Sorry something went wrong",Toast.LENGTH_LONG).show()

//            selectedMedia = SELECT_VIDEO
//            var bottomsheet = ExampleBottomSheetDialog()
//            bottomsheet.mListener = this
//            bottomsheet.show(childFragmentManager, "exampleBottomSheet")
//

        }


        viewGroup.swipeRefreshLayout.setOnRefreshListener {
            getPosts()
        }


//        viewGroup.iv_open_video.setOnClickListener {
//            dispatchTakeVideoIntent()
////            performFileSearch()
//        }
//
//        viewGroup.iv_open_camera.setOnClickListener {
//            dispatchTakePictureIntent()
//        }

        viewGroup.ic_live.setOnClickListener {

            Toast.makeText(requireContext(),"Sorry something went wrong",Toast.LENGTH_LONG).show()


//            val locationAddress = getAddressByLatLng(
//                context!!,
//                LatLng(
//                    RuntimeData.latLng2?.lat?.toDouble()!!,
//                    RuntimeData.latLng2?.lng?.toDouble()!!
//                )
//            )
//
//            val bodyModel = CreatePostBodyModel()
//            bodyModel.post_text = "LIVE"
//            bodyModel.location = "" + locationAddress
//            bodyModel.latitude = RuntimeData.latLng2?.lat
//            bodyModel.longitudes = RuntimeData.latLng2?.lng
//            bodyModel.fun_count = 0.0
//            bodyModel.crowd_count = 0.0
//            bodyModel.type = MEDIA_TYPE_LIVE
//
//
//            ApiServiceWithFileUpload(activity as Activity).callUploadApi(bodyModel,
//                object : ApiServiceWithFileUpload.UploadListener {
//                    override fun onProgress() {
//
//                    }
//
//                    override fun onCompleted(bodyAsString: String) {
//
//
//                        if (bodyAsString != null) {
//                            val responseCreatePost =
//                                Gson().fromJson(bodyAsString, ResponseCreatePost::class.java)
//
//                            if (responseCreatePost.meta.status == 200) {
//
//                                Intent(context, GoLiveActivity::class.java).let {
//                                    it.putExtra(
//                                        GoLiveActivity.KEY_POST_ID,
//                                        responseCreatePost.response.post.id
//                                    )
//                                    it.putExtra(
//                                        GoLiveActivity.KEY_SESSION_ID,
//                                        responseCreatePost.response.post.opentok_session
//                                    )
//                                    it.putExtra(
//                                        GoLiveActivity.KEY_TOKEN,
//                                        responseCreatePost.response.post.opentok_token
//                                    )
//                                    startActivity(it)
//                                }
//
//                            }
//                        }
//
//                    }
//
//                    override fun onError(message: String) {
//
//                    }
//                })
//
//


            //// previous comment
////            startActivity(Intent(context, GoLiveActivity::class.java))
////                        dispatchTakeVideoIntent()
        }

        viewGroup.tv_clear_filter.setOnClickListener {
            clearFilter()
        }

    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            val photoFile: File? = try {
                createImageFile(context!!)
            } catch (ex: IOException) {
                // Error occurred while creating the File
                null
            }

            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    context!!,
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

    private fun dispatchTakeVideoIntent() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also {
            it.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30)
            startActivityForResult(it, REQUEST_VIDEO_CAPTURE)
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
            type = "*/*"
        }

        startActivityForResult(intent, READ_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {

        if (requestCode == FilterActivity.REQUEST_FILTER_PHOTO && resultCode == RESULT_OK) {

            try {

                bitmapWriteToFile(FilterActivity.resultBitmap, createModelAndGoMediaActivity)
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }

        //Capture Image
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            val imageUri = currentPhotoUri
//
//            val createPostBodyModel = CreatePostBodyModel()
//            createPostBodyModel.type = MEDIA_TYPE_PHOTO
//            createPostBodyModel.location = getString(R.string.dhaka)
//            createPostBodyModel.post_files.add(imageUri.toString())


            val ei = ExifInterface(currentPhotoFile?.absolutePath)
            val orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            var rotatedBitmap: Bitmap? = null
            when (orientation) {

                ExifInterface.ORIENTATION_ROTATE_90 -> {
                    rotatedBitmap = rotateImage(getBitmapFromUri(context!!, currentPhotoUri), 90f)
                    bitmapWriteToFile(rotatedBitmap, goFilterActivity)
                }


                ExifInterface.ORIENTATION_ROTATE_180 -> {
                    rotatedBitmap = rotateImage(getBitmapFromUri(context!!, currentPhotoUri), 180f)
                    bitmapWriteToFile(rotatedBitmap, goFilterActivity)
                }


                ExifInterface.ORIENTATION_ROTATE_270 -> {
                    rotatedBitmap = rotateImage(getBitmapFromUri(context!!, currentPhotoUri), 270f)
                    bitmapWriteToFile(rotatedBitmap, goFilterActivity)
                }


                ExifInterface.ORIENTATION_NORMAL -> {
//                    rotatedBitmap = getBitmapFromUri(context!!, currentPhotoUri);
                    goFilterActivity()
                }

                ExifInterface.ORIENTATION_UNDEFINED -> {
                    goFilterActivity()
                }
            }


//            Intent(context, FilterActivity::class.java).let {
//                RuntimeData.currentPhotoUri = currentPhotoUri
//                startActivityForResult(it, FilterActivity.REQUEST_FILTER_PHOTO)
//            }

        }

        // Capture Video
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            val videoUri: Uri = resultIntent?.data!!

            val createPostBodyModel = CreatePostBodyModel()
            createPostBodyModel.type = MEDIA_TYPE_VIDEO
            createPostBodyModel.location = getString(R.string.dhaka)
            createPostBodyModel.video_file = videoUri.toString()

            Intent(context, MediaActivity::class.java).let {
                it.putExtra(MediaActivity.KEY_BODY_CREATE_POST, Gson().toJson(createPostBodyModel))
                startActivity(it)
            }
        }

        // Pick file from storage
        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
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

                if (mediaType == -1) {
                    showAlertDialog(activity as Activity, getString(R.string.invalid_file))
                    return
                }

                if (selectedMedia == SELECT_VIDEO && mediaType == MEDIA_TYPE_PHOTO) {
                    showAlertDialog(activity as Activity, getString(R.string.invalid_video))
                    return
                }

                if (selectedMedia == SELECT_CAMERA && mediaType == MEDIA_TYPE_VIDEO) {
                    showAlertDialog(activity as Activity, getString(R.string.invaild_image))
                    return
                }

                when (selectedMedia) {
                    SELECT_CAMERA -> {
//                        val createPostBodyModel = CreatePostBodyModel()
//                        createPostBodyModel.type = mediaType
//                        createPostBodyModel.location = "Dhaka"
//                        createPostBodyModel.post_files.add(uri.toString())
//
//                        Intent(context, MediaActivity::class.java).let {
//                            it.putExtra(
//                                MediaActivity.KEY_BODY_CREATE_POST,
//                                Gson().toJson(createPostBodyModel)
//                            )
//                            startActivity(it)
//                        }

                        val photoFile: File? = try {
                            createImageFile(context!!)
                        } catch (ex: IOException) {
                            // Error occurred while creating the File
                            null
                        }

                        photoFile?.also {
                            val photoURI: Uri = FileProvider.getUriForFile(
                                context!!,
                                "com.parallax.partyapp.fileprovider",
                                it
                            )

                            FileOutputStream(photoFile).use { out ->
                                getBitmapFromUri(context!!, uri).compress(
                                    Bitmap.CompressFormat.PNG,
                                    100,
                                    out
                                ) // bmp is your Bitmap instance
                                // PNG is a lossless format, the compression factor (100) is ignored
                            }

                            currentPhotoFile = photoFile

                            currentPhotoUri = uri



                            Intent(context, FilterActivity::class.java).let {
                                FilterActivity.currentPhotoUri = photoURI
                                startActivityForResult(it, FilterActivity.REQUEST_FILTER_PHOTO)
                            }
                        }
                    }
                    SELECT_VIDEO -> {
                        val createPostBodyModel = CreatePostBodyModel()
                        createPostBodyModel.type = mediaType
                        createPostBodyModel.location = "Dhaka"
                        createPostBodyModel.video_file = uri.toString()

                        Intent(context, MediaActivity::class.java).let {
                            it.putExtra(
                                MediaActivity.KEY_BODY_CREATE_POST,
                                Gson().toJson(createPostBodyModel)
                            )
                            startActivity(it)
                        }
                    }
                }


            }
        }
    }

    var createModelAndGoMediaActivity = {
        if (currentPhotoFile != null) {

            val imageUri = currentPhotoUri

            val createPostBodyModel = CreatePostBodyModel()
            createPostBodyModel.type = MEDIA_TYPE_PHOTO
            createPostBodyModel.location = getString(com.parallax.partyapp.R.string.dhaka)
            createPostBodyModel.post_files.add(imageUri.toString())

            Intent(context, MediaActivity::class.java).let {
                it.putExtra(
                    MediaActivity.KEY_BODY_CREATE_POST,
                    Gson().toJson(createPostBodyModel)
                )
                startActivity(it)
            }

        }
    }

    var goFilterActivity = {
        Intent(context, FilterActivity::class.java).let {
            FilterActivity.currentPhotoUri = currentPhotoUri
            startActivityForResult(it, FilterActivity.REQUEST_FILTER_PHOTO)
        }
    }


    private fun bitmapWriteToFile(bitmapToRotate: Bitmap, goDestinationActivity: () -> Unit) {

        val kProgressHUD = KProgressHUD.create(activity)
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

            activity?.runOnUiThread {
                kProgressHUD?.dismiss()
                goDestinationActivity()
            }

        }
    }


    private fun getPosts() {

        val postsBody = PostsBody(lat.toString(), lng.toString())

        WebService.getPosts(postsBody, object : DataListener<ResponsePosts> {
            override fun onStartCall() {
                if (responsePosts == null) showProgressBar(activity as Activity)
            }

            override fun onResponse(responseObj: ResponsePosts?) {
                hideProgressBar()
                if (viewGroup.swipeRefreshLayout.isRefreshing) viewGroup.swipeRefreshLayout.isRefreshing =
                    false

                responseObj?.let {
                    if (it.meta.status == 200) {
                        processResponse(it, false)

                    } else {
                        showAlertDialog(activity as Activity, getString(R.string.posts_not_found))
                    }
                }
            }

            override fun onError(errorMessage: String) {
                if (viewGroup.swipeRefreshLayout.isRefreshing) viewGroup.swipeRefreshLayout.isRefreshing =
                    false
                hideProgressBar()
            }
        })
    }

    private fun processResponse(it: ResponsePosts, fromCache: Boolean) {

        responsePosts = it
        if (this.isVisible) (activity as MainActivity).responsePosts.value = it

        items.clear()

        if (it.response.add_lists.isNotEmpty()) {

            if (!fromCache) {
                for (i in 0 until it.response.post.size) {
                    val post = it.response.post[i]
                    if (post.type.toInt() == MEDIA_TYPE_PHOTO) {
                        val firstImgeObj = ResponsePosts.Response.Post.Image().apply {
                            reference_url = post.reference_url
                        }
                        it.response.post[i].images.add(0, firstImgeObj)
                    }
                }
            }

            HomeAdapter.addsList = it.response.add_lists

            items.add(object : HomeItems {
                override fun getItemType(): Int {
                    return HomeAdapter.HOME_ITEM_TYPE_ADS
                }
            })
        }

        if (it.response.post.isNotEmpty()) {
            viewGroup.tv_nopost.hide()
            items.addAll(it.response.post)
        } else {
            viewGroup.tv_nopost.show()
        }

        if (it.response.set_filter) {
            viewGroup.tv_clear_filter.show()
        } else {
            viewGroup.tv_clear_filter.hide()
        }

        homeAdapter?.notifyDataSetChanged()
    }


    private fun clearFilter() {
        val postFilter = PostFilter()
        postFilter.most_recent = 0
        postFilter.following = 0
        postFilter.nearest = 0
        postFilter.liked = 0
        postFilter.comments = 0
        postFilter.crowded = 0
        postFilter.quiet = 0
        postFilter.fun_limit = 0
        postFilter.last_twenty_four = 0
        postFilter.last_forty_eight = 0

        WebService.setFilter(postFilter, object : DataListener<ResponseSetFilter> {
            override fun onStartCall() {
                showProgressBar(activity as MainActivity)
            }

            override fun onResponse(responseObj: ResponseSetFilter?) {
                hideProgressBar()

                Log.d("post count", responseObj?.response?.filter_posts.toString())

                getPosts()

            }

            override fun onError(errorMessage: String) {
                hideProgressBar()
            }
        })
    }

}
