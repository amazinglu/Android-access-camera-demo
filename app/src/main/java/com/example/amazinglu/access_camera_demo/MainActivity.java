package com.example.amazinglu.access_camera_demo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_TAKE_PICTURE = 203;

    private ImageView imageView;
    private Uri imageUri;

    private String mCurrentPhotoPath;

    /**
     * https://inthecheesefactory.com/blog/how-to-share-access-to-file-with-fileprovider-on-android-nougat/en
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    try {
                        takePhoto();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            File file = new File(imageUri.getPath());
            try {
                InputStream ims = new FileInputStream(file);
                imageView.setImageBitmap(BitmapFactory.decodeStream(ims));
            } catch (FileNotFoundException e) {
                return;
            }

            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(MainActivity.this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if ((requestCode == PermissionUtil.REQ_CODE_READ_EXTERNAL_STORAGE ||
                requestCode == PermissionUtil.REQ_CODE_WRITE_EXTERNAL_STORAGE ||
                requestCode == PermissionUtil.REQ_CODE_CAMERA)
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                takePhoto();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void takePhoto() throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            if (photoFile != null) {
//                Uri photoUri = Uri.fromFile(createImageFile());
                Uri photoUri = FileProvider.getUriForFile(MainActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider", createImageFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQ_CODE_TAKE_PICTURE);
            }
        }
    }

    private File createImageFile() throws IOException {

        // create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private boolean checkPermission() {
        boolean needReadExternalPermission = false;
        boolean needWriteExternalPermission = false;
        boolean needCameraPermission = false;
        if (!PermissionUtil.checkPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            needReadExternalPermission = true;
        }
        if (!PermissionUtil.checkPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            needWriteExternalPermission = true;
        }
        if (!PermissionUtil.checkPermission(getContext(), Manifest.permission.CAMERA)) {
            needCameraPermission = true;
        }

        if (needReadExternalPermission) {
            PermissionUtil.requestReadExternalStoragePermission(getActivity());
        }
        if (needWriteExternalPermission) {
            PermissionUtil.requestWriteExternalStoragePermission(getActivity());
        }
        if (needCameraPermission) {
            PermissionUtil.requestCameraPermission(getActivity());
        }

        return !needCameraPermission && !needReadExternalPermission && !needWriteExternalPermission;
    }

    private Context getContext() {
        return MainActivity.this;
    }

    private MainActivity getActivity() {
        return this;
    }
}
