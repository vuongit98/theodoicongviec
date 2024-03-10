package com.theodoilamviec.Account.sheets;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.theodoilamviec.theodoilamviec.R;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static android.app.Activity.RESULT_OK;

public class AttachImageBottomSheetModal extends BottomSheetDialogFragment {

    // Bundle
    Bundle bundle;

    String currentPhotoPath;

    // Permissions
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    // REQUEST CODES
    private static final int REQUEST_STORAGE_PERMISSION_CODE = 1;
    private static final int REQUEST_CAMERA_PERMISSION_CODE = 2;
    private static final int REQUEST_SELECT_IMAGE_CODE = 3;
    public static final int REQUEST_SELECT_IMAGE_FROM_GALLERY_CODE = 4;
    public static final int REQUEST_CAMERA_IMAGE_CODE = 5;
    public static final int REQUEST_SELECT_VIDEO_FROM_GALLERY_CODE = 6;

    public interface OnChooseImageListener {
        void onChooseImageListener(int requestCode, Bitmap bitmap, Uri uri);
    }

    public interface OnChooseVideoListener {
        void onChooseVideoListener(int requestCode, Uri uri);
    }

    OnChooseImageListener onChooseImageListener;
    OnChooseVideoListener onChooseVideoListener;

    public AttachImageBottomSheetModal() {

    }

    ActivityResultLauncher<PickVisualMediaRequest> pickImage =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    try {
                        InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        onChooseImageListener.onChooseImageListener(REQUEST_SELECT_IMAGE_FROM_GALLERY_CODE, bitmap, uri);
                        dismiss();
                    } catch (Exception exception) {
                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    ActivityResultLauncher<PickVisualMediaRequest> pickVideo =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    try {
                        onChooseVideoListener.onChooseVideoListener(REQUEST_SELECT_VIDEO_FROM_GALLERY_CODE, uri);
                        dismiss();
                    } catch (Exception exception) {
                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });
    private ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                if (!isGranted.containsValue(false)) {
                    pickImage.launch(new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build());
                }
            });

    private ActivityResultLauncher<String[]> requestPermissionVideoLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                if (!isGranted.containsValue(false)) {
                    pickVideo.launch(new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.VideoOnly.INSTANCE)
                            .build());
                }
            });

    private ActivityResultLauncher<String[]> requestPermissionTakePhoto =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                if (!isGranted.containsValue(false)) {
                    requestCaptureImage();
                }
            });


    private ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

                        File file = null;

                        try {
                            file = createImageFile();
                        } catch (IOException e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        if (file != null) {
                            Uri uri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".provider", file);
                            Uri selected_image_uri = Uri.parse(uri.toString());

                            if (selected_image_uri != null) {
                                try {
                                    InputStream inputStream = requireContext().getContentResolver().openInputStream(selected_image_uri);
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                                    onChooseImageListener.onChooseImageListener(REQUEST_CAMERA_IMAGE_CODE, bitmap, selected_image_uri);
                                    dismiss();
                                } catch (Exception exception) {
                                    Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }
                }
            });

    // Registers a photo picker activity launcher in single-select mode.

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onChooseImageListener = (OnChooseImageListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onChooseImageListener");
        }

        try {
            onChooseVideoListener = (OnChooseVideoListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onChooseVideoListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attach_image_bottom_sheet_modal, container, false);

        // Bundle
        bundle = new Bundle();

        // take a photo
        LinearLayout takePhoto = view.findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(v -> {

            requestPermissionTakePhoto.launch(new String[]{
                    Manifest.permission.CAMERA
            });
        });

        // select image from gallery
        LinearLayout selectImageFromGallery = view.findViewById(R.id.select_image_from_gallery);
        selectImageFromGallery.setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(new String[]{
                        Manifest.permission.READ_MEDIA_IMAGES,
                });
            } else {
                requestPermissionLauncher.launch(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                });
            }

        });

        // select video from gallery
        LinearLayout selectVideoFromGallery = view.findViewById(R.id.select_video_from_gallery);
        selectVideoFromGallery.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionVideoLauncher.launch(new String[]{
                        Manifest.permission.READ_MEDIA_VIDEO
                });
            } else {
                requestPermissionVideoLauncher.launch(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                });
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * request to open image capture,
     * allow for images only as attachment
     */
    @SuppressLint("QueryPermissionsNeeded")
    private void requestCaptureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mStartForResult.launch(intent);


    }

    /**
     * create image file
     *
     * @return image for image
     * @throws IOException for exception
     */
    private File createImageFile() throws IOException {
        // create image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File image = new File(Environment.getExternalStorageDirectory() + "/" + requireContext().getPackageName() + "/", imageFileName + ".jpg");
        // create directory
        File dir = new File(Environment.getExternalStorageDirectory() + "/" + getContext().getPackageName() + "/");
        if (!dir.exists()) {
            dir.mkdir();
        }

        // save the file
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_SELECT_IMAGE_CODE && resultCode == RESULT_OK) {
//            if (data != null) {
//                Uri selected_image_uri = data.getData();
//
//                if (selected_image_uri != null) {
//                    try {
//                        InputStream inputStream = requireContext().getContentResolver().openInputStream(selected_image_uri);
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//
//                        onChooseImageListener.onChooseImageListener(REQUEST_SELECT_IMAGE_FROM_GALLERY_CODE, bitmap, selected_image_uri);
//                        dismiss();
//                    } catch (Exception exception) {
//                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        } else if (requestCode == REQUEST_CAMERA_IMAGE_CODE && resultCode == RESULT_OK) {
//            if (bundle != null) {
//                Uri selected_image_uri = Uri.parse(bundle.getString(MediaStore.EXTRA_OUTPUT));
//
//                if (selected_image_uri != null) {
//                    try {
//                        InputStream inputStream = requireContext().getContentResolver().openInputStream(selected_image_uri);
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//
//                        onChooseImageListener.onChooseImageListener(REQUEST_CAMERA_IMAGE_CODE, bitmap, selected_image_uri);
//                        dismiss();
//                    } catch (Exception exception) {
//                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        } else if (requestCode == REQUEST_SELECT_VIDEO_FROM_GALLERY_CODE && resultCode == RESULT_OK) {
//            Uri selected_video_uri = data.getData();
//
//            if (selected_video_uri != null) {
//                onChooseVideoListener.onChooseVideoListener(REQUEST_SELECT_VIDEO_FROM_GALLERY_CODE, selected_video_uri);
//                dismiss();
//            }
//        }
//    }

}