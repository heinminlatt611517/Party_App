package com.parallax.partyapp.views.filterUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.parallax.partyapp.R;
import com.parallax.partyapp.views.BaseActivity;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailCallback;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.io.IOException;
import java.util.List;

public class FilterActivity extends BaseActivity implements ThumbnailCallback {

    static public int REQUEST_FILTER_PHOTO = 124;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private ImageView placeHolderImageView;
    private RecyclerView rcvThumbnails;
    private ImageView btnRotate;
    private TextView tvDone;
    private ImageView ivBack;


    Bitmap thumbImage = null;

    public static Uri currentPhotoUri = null;

    public static Bitmap resultBitmap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        tvDone = findViewById(R.id.tv_done);
        ivBack = findViewById(R.id.iv_back);
        btnRotate = findViewById(R.id.btn_rotate);
        rcvThumbnails = findViewById(R.id.thumbnails);
        placeHolderImageView = findViewById(R.id.place_holder_imageview);

        try {
            thumbImage = MediaStore.Images.Media.getBitmap(getContentResolver(), currentPhotoUri);
        } catch (IOException e) {
            e.printStackTrace();
        }


        loadImages();


        btnRotate.setOnClickListener(v -> {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            thumbImage = Bitmap.createBitmap(thumbImage, 0, 0, thumbImage.getWidth(), thumbImage.getHeight(), matrix, true);

            loadImages();
        });

        tvDone.setOnClickListener(v -> {
            setResult(Activity.RESULT_OK);
            finish();
        });

        ivBack.setOnClickListener(v -> {
            finish();
        });

    }

    private void loadImages() {
        placeHolderImageView.setImageBitmap(thumbImage);
        resultBitmap = thumbImage;

        ThumbnailsManager.clearThumbs();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        rcvThumbnails.setLayoutManager(layoutManager);
        rcvThumbnails.setHasFixedSize(true);

        List<Filter> filters = FilterPack.getFilterPack(this);

        for (Filter filter : filters) {
            ThumbnailItem item = new ThumbnailItem();
            item.image = thumbImage;
            item.filter = filter;
            item.filterName = filter.getName();
            ThumbnailsManager.addThumb(item);
        }

        List<ThumbnailItem> thumbs = ThumbnailsManager.processThumbs(getApplicationContext());

        ThumbnailsAdapter adapter = new ThumbnailsAdapter(thumbs, this);
        rcvThumbnails.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentPhotoUri = null;
        ThumbnailsManager.clearThumbs();
    }

    @Override
    public void onThumbnailClick(Filter filter) {
        Bitmap copiedBitmap = thumbImage.copy(Bitmap.Config.ARGB_8888, true);
        resultBitmap = filter.processFilter(copiedBitmap);
        placeHolderImageView.setImageBitmap(resultBitmap);
    }
}
