/*
 * Developed by Md Rayhan Uddin (rayhanuddin55@gmail.com)
 */

package com.parallax.partyapp.Api

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import com.parallax.partyapp.Data.SPreferences
import com.parallax.partyapp.Models.CreatePostBodyModel
import com.parallax.partyapp.Models.SetProfileBodyModel
import com.parallax.partyapp.Utils.API_BASE_URL
import net.gotev.uploadservice.*

class ApiServiceWithFileUpload(val activity: Activity) {

    private var progressDialog: ProgressDialog? = null


    fun callUpdateProfileApi(
        setProfileBodyModel: SetProfileBodyModel,
        uploadListener: UploadListener
    ) {
        val multipartUploadRequest = getMultipartUploadRequest("set-profle")


//        name:atik update
//        gender:male
//        phone:123456789
//        bio:test
//        date_of_birth:05/13/2010
//        image

        multipartUploadRequest.addParameter("name", setProfileBodyModel.name)
        multipartUploadRequest.addParameter("gender", setProfileBodyModel.gender)
        multipartUploadRequest.addParameter("phone", setProfileBodyModel.phone)
        multipartUploadRequest.addParameter("bio", setProfileBodyModel.bio)
        multipartUploadRequest.addParameter("date_of_birth", setProfileBodyModel.date_of_birth)

        setProfileBodyModel.image?.let {
            multipartUploadRequest.addFileToUpload(
                setProfileBodyModel.image!!.absolutePath,
                "image"
            )
        }

        uploadFile(activity, multipartUploadRequest, uploadListener)
    }

    fun callUploadApi(createPostBodyModel: CreatePostBodyModel, uploadListener: UploadListener) {
        val multipartUploadRequest = getMultipartUploadRequest("create-post")


        val location = when {
            createPostBodyModel.location?.isNullOrBlank()!! -> ""
            createPostBodyModel.location == "null" -> ""
            else -> createPostBodyModel.location
        }

        val lat = when {
            createPostBodyModel.latitude?.isNullOrBlank()!! -> ""
            createPostBodyModel.latitude == "null" -> ""
            else -> createPostBodyModel.latitude
        }

        val lng = when {
            createPostBodyModel.longitudes?.isNullOrBlank()!! -> ""
            createPostBodyModel.longitudes == "null" -> ""
            else -> createPostBodyModel.longitudes
        }

        val feel = if (createPostBodyModel.feel != null) {
            "Feeling ${createPostBodyModel.feel}"
        } else {
            ""
        }

        multipartUploadRequest.addParameter("type", createPostBodyModel.type.toString())
        multipartUploadRequest.addParameter("location", location)
        multipartUploadRequest.addParameter("post_text", createPostBodyModel.post_text)
        multipartUploadRequest.addParameter("latitude", lat)
        multipartUploadRequest.addParameter("longitudes", lng)
        multipartUploadRequest.addParameter("feel", feel)
        multipartUploadRequest.addParameter("activity", createPostBodyModel.activity ?: "")
        multipartUploadRequest.addParameter(
            "tag_friends",
            createPostBodyModel.tag_friends.toString()
        )
        multipartUploadRequest.addParameter(
            "crowd_count",
            createPostBodyModel.crowd_count.toString()
        )
        multipartUploadRequest.addParameter("fun_count", createPostBodyModel.fun_count.toString())

        createPostBodyModel.post_files?.let {
            for (i in 0 until it.size) {
                multipartUploadRequest.addFileToUpload(it[i], "post_file[$i]")
            }
        }

        createPostBodyModel.video_file?.let {
            multipartUploadRequest.addFileToUpload(createPostBodyModel.video_file, "post_file[0]")
        }

        uploadFile(activity, multipartUploadRequest, uploadListener)
    }

    private fun getMultipartUploadRequest(apiPostFix: String): MultipartUploadRequest {
        // starting from 3.1+, you can also use content:// URI string instead of absolute fileUri
        return MultipartUploadRequest(activity.applicationContext, API_BASE_URL + apiPostFix)
            .setNotificationConfig(UploadNotificationConfig())
            .setMaxRetries(0)
    }

    private fun uploadFile(
        activity: Activity,
        multipartUploadRequest: MultipartUploadRequest,
        uploadListener: UploadListener
    ) {
        progressDialog = ProgressDialog(activity)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog!!.setCancelable(false)

        if (SPreferences.islanguageBurmese()) {
            progressDialog!!.setTitle("-ေခတၱေစာင့္ပါ")
        } else {
            progressDialog!!.setTitle("Please wait")
        }



        progressDialog!!.setProgressNumberFormat(null)

        progressDialog!!.progress = 0
        progressDialog!!.isIndeterminate = false

        try {
            multipartUploadRequest
                .setDelegate(object : UploadStatusDelegate {
                    override fun onProgress(context: Context, uploadInfo: UploadInfo) {
                        Log.d("uploadServiceStatus", "" + uploadInfo.uploadedBytes)

                        uploadListener.onProgress()

                        if (uploadInfo.uploadedBytes > 0) {
                            val progressPercent =
                                uploadInfo.uploadedBytes * 100 / uploadInfo.totalBytes
                            progressDialog!!.progress = progressPercent.toInt()
                            progressDialog!!.isIndeterminate = false
                        }
                    }

                    override fun onError(
                        context: Context,
                        uploadInfo: UploadInfo,
                        serverResponse: ServerResponse?,
                        exception: Exception?
                    ) {
                        if (progressDialog!!.isShowing) {
                            progressDialog!!.dismiss()
                        }

                        if (serverResponse != null) {

                            uploadListener.onError("Connection timeout : ${serverResponse.httpCode}")

                            Log.d("uploadServiceStatus", "error " + serverResponse.bodyAsString)
                            Log.d("uploadServiceStatus", "error " + serverResponse.httpCode)
                        }

                        if (exception != null) {

                            uploadListener.onError("Upload error :  ${exception.localizedMessage}")

                            Log.d("uploadServiceStatus", "error " + exception.message)
                            Log.d("uploadServiceStatus", "error " + exception.localizedMessage)
                        }
                    }

                    override fun onCompleted(
                        context: Context,
                        uploadInfo: UploadInfo,
                        serverResponse: ServerResponse
                    ) {
                        Log.d("uploadServiceStatus", "complete")
                        Log.d("uploadServiceStatus", "" + serverResponse.bodyAsString)

                        uploadListener.onCompleted(serverResponse.bodyAsString)

                        if (progressDialog!!.isShowing) {
                            progressDialog!!.dismiss()
                        }


                    }

                    override fun onCancelled(context: Context, uploadInfo: UploadInfo) {
                        Log.d("uploadServiceStatus", "canceled")

                        uploadListener.onError("Canceled")

                        if (progressDialog!!.isShowing) {
                            progressDialog!!.dismiss()
                        }
                    }
                })
                .startUpload()

            progressDialog!!.show()

        } catch (exc: Exception) {
            Log.e("AndroidUploadService", exc.message, exc)
        }
    }

    interface UploadListener {
        fun onProgress()
        fun onCompleted(bodyAsString: String)
        fun onError(message: String)
    }
}