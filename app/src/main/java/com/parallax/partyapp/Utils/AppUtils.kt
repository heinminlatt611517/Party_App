import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.model.LatLng
import com.kaopiz.kprogresshud.KProgressHUD
import com.parallax.partyapp.Data.SPreferences
import com.parallax.partyapp.PartyApp
import com.parallax.partyapp.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_loading.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


fun hideKeyboard(activity: Activity) {
    val view = activity.findViewById<View>(android.R.id.content)
    try {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    } catch (e: Exception) {

    }

}

fun showKeyboard(activity: Activity, editText: EditText) {
    try {
        editText.requestFocus()
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    } catch (e: Exception) {

    }
}

fun loadImage(context: Context, url: String?, imageView: ImageView, isRound: Boolean = false) {
    Log.d("imageUrl", url + "")
    val glide = Glide.with(context)
        .load(url + "")
        .placeholder(R.drawable.ic_no_image)
        .error(R.drawable.ic_no_image)

    if (isRound) glide.apply(RequestOptions.circleCropTransform())

    glide.into(imageView)
}

fun loadImageHome(url: String?, imageView: ImageView, progressbar: ProgressBar) {

//    Glide.with(context)
//        .load(url + "")
//        .placeholder(R.drawable.ic_no_image)
//        .error(R.drawable.ic_no_image)
//        .listener(object : RequestListener<Drawable> {
//            override fun onLoadFailed(
//                e: GlideException?,
//                model: Any?,
//                target: com.bumptech.glide.request.target.Target<Drawable>?,
//                isFirstResource: Boolean
//            ): Boolean {
//                progressbar.visibility = View.GONE
//                return false
//            }
//
//            override fun onResourceReady(
//                resource: Drawable?,
//                model: Any?,
//                target: com.bumptech.glide.request.target.Target<Drawable>?,
//                dataSource: DataSource?,
//                isFirstResource: Boolean
//            ): Boolean {
//                progressbar.visibility = View.GONE
//                return false
//            }
//
//        })
//        .into(imageView)

    Picasso.get()
        .load(url + "")
        .placeholder(R.drawable.ic_no_image)
        .error(R.drawable.ic_no_image)
//        .fit()
//        .centerCrop()
//        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
        .into(imageView, object : Callback {
            override fun onSuccess() {
                progressbar.visibility = View.GONE
            }

            override fun onError(e: java.lang.Exception?) {
                progressbar.visibility = View.GONE
            }
        })
}


fun loadImageFromFB(context: Context, imageView: ImageView) {
    val url = "http://graph.facebook.com/${SPreferences.getUserFacebookId()}/picture?type=large"

    Log.d("imageUrl", url + "")
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.ic_no_image)
        .error(R.drawable.ic_no_image)
        .into(imageView)
}

fun showAlertDialog(activity: Activity, message: String, onOkButtonClicked: (() -> Unit)? = null) {
    val builder = AlertDialog.Builder(activity)
    builder.setMessage(message)
    builder.setPositiveButton("OK") { dialog, which ->
        onOkButtonClicked?.let { it() }
        dialog.dismiss()
    }
    builder.show()
}

fun getMimeType(uri: Uri): String? {
    val cR = PartyApp.context.contentResolver
    val mime = MimeTypeMap.getSingleton()
    val type = mime.getExtensionFromMimeType(cR.getType(uri))
    return type
}

@Throws(IOException::class)
fun createImageFile(context: Context): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    ).apply {
        // Save a file: path for use with ACTION_VIEW intents
        return this
    }
}

fun getBitmapFromUri(context: Context, uri: Uri): Bitmap {
    return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix();
    matrix.postRotate(angle);
    return Bitmap.createBitmap(
        source, 0, 0, source.getWidth(), source.getHeight(),
        matrix, true
    )
}

private var kProgressHUD: KProgressHUD? = null
private var loadingDialog: Dialog? = null

fun showProgressBar(activity: Activity) {
    hideProgressBar()

    loadingDialog = Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    loadingDialog?.setContentView(R.layout.dialog_loading)
    loadingDialog?.setCancelable(false)

    Glide.with(activity)
        .load(R.drawable.loading)
        .into(loadingDialog?.iv_loading!!)

    loadingDialog?.show()


//    kProgressHUD = KProgressHUD.create(activity)
//        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//        .setCancellable(false)
//        .setAnimationSpeed(1)
//        .setDimAmount(0.5f)
//
//    kProgressHUD?.show()
}

fun hideProgressBar() {
    if (loadingDialog != null)
        loadingDialog?.dismiss()
}

fun getMatColor(context: Context, typeColor: String): Int {
    var returnColor: Int = Color.BLACK
    val arrayId: Int = context.resources.getIdentifier(
        "mdcolor_$typeColor",
        "array",
        context.packageName
    )
    if (arrayId != 0) {
        val colors: TypedArray = context.resources.obtainTypedArray(arrayId)
        val index = (Math.random() * colors.length())
        returnColor = colors.getColor(index.toInt(), Color.BLACK)
        colors.recycle()
    }
    return returnColor
}


fun getAddressByLatLng(context: Context, latLng: LatLng): String {
    var addresses: List<Address> = ArrayList()
    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
    } catch (e: IOException) {
        e.printStackTrace()
    }


    if (addresses.size == 0) {
        return ""
    }

    val address = addresses[0]

    val sb = StringBuilder()
    if (address != null) {
        //            for (int i = 0; i < pickupAdress.getMaxAddressLineIndex(); i++) {
        //                sb.append(pickupAdress.getAddressLine(i) + "\n");
        //            }
        try {
            sb.append(address.getAddressLine(0))
            if (address.locality != null && !sb.toString().toLowerCase().trim { it <= ' ' }
                    .contains(
                        address.locality.toLowerCase().trim { it <= ' ' })
            )
                sb.append(", ").append(address.locality)
        } catch (ignored: Exception) {

        }

        //Toasty.makeText(getContext(), sb.toString() + "," + pickupAdress.getCountryName().toString(), Toasty.TYPE_ERROR);

        Log.d("selected: sb", "" + sb.toString())
//        Log.d("selected: countryName", "" + address.countryName.toString())
//        Log.d("selected: locality", "" + address.locality.toString()) // city name
//        Log.d("selected: featureName", "" + address.featureName.toString())
//        Log.d("selected: adminArea", "" + address.adminArea.toString())
//        Log.d("selected: subAdminArea", "" + address.subAdminArea.toString())
//        Log.d("selected: subLocatity", "" + address.subLocality.toString())
    }

    val markerAddress = sb.toString()

    return markerAddress
}

fun pxToDp(px: Int): Int {
    return (px / Resources.getSystem().displayMetrics.density) as Int
}