package com.example.dormitally;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AddRoomPage extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private EditText roomNum, roomPrice, roomDescription;
    private Spinner spinnerRoomType, spinnerRoomStatus, spinnerRoomCapacity;
    private Button addRoomBtn, cancelBtn;
    private ImageView imgPreview;
    private FrameLayout uploadBtn;

    private Uri imageUri = null;
    private String imagePath = null;

    private String rmNumber = "", rmType = "", rmStatus = "",
            rmPrice = "", rmCapacity = "", rmDescription = "";

    private int roomId = -1;
    private boolean isEditing = false;
    TextView tvUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room_page);

        roomNum = findViewById(R.id.etRoomNumber);
        roomPrice = findViewById(R.id.etRoomPrice);
        roomDescription = findViewById(R.id.etDescription);
        spinnerRoomType = findViewById(R.id.roomTypeSpinner);
        spinnerRoomStatus = findViewById(R.id.roomStatusSpinner);
        spinnerRoomCapacity = findViewById(R.id.roomCapacitySpinner);

        addRoomBtn = findViewById(R.id.btnSave);
        cancelBtn = findViewById(R.id.btnCancel);
        uploadBtn = findViewById(R.id.frameImage);
        imgPreview = findViewById(R.id.imgPreview);
        tvUpload = findViewById(R.id.tvUpload);
        spinnerRoomType.setOnItemSelectedListener(this);
        spinnerRoomStatus.setOnItemSelectedListener(this);
        spinnerRoomCapacity.setOnItemSelectedListener(this);

        addRoomBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);

        uploadBtn.setOnClickListener(v -> {
            Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickIntent, 100);
        });


        // LOAD EDIT MODE DATA
        Bundle b = getIntent().getExtras();
        if (b != null) {

            isEditing = true;
            roomId = b.getInt("roomId");

            roomNum.setText(b.getString("roomNumber"));
            roomDescription.setText(b.getString("roomDescription"));
            roomPrice.setText(b.getString("roomPrice"));

            rmType = b.getString("roomType");
            rmStatus = b.getString("roomStatus");
            rmCapacity = b.getString("roomCapacity");

            spinnerRoomType.setSelection(((ArrayAdapter) spinnerRoomType.getAdapter()).getPosition(rmType));
            spinnerRoomStatus.setSelection(((ArrayAdapter) spinnerRoomStatus.getAdapter()).getPosition(rmStatus));
            spinnerRoomCapacity.setSelection(((ArrayAdapter) spinnerRoomCapacity.getAdapter()).getPosition(rmCapacity));

            // Load existing image
            imagePath = b.getString("roomImagePath");
            if (imagePath != null && !imagePath.isEmpty()) {
                imageUri = Uri.parse(imagePath);
                imgPreview.setImageURI(imageUri);
                imgPreview.setVisibility(View.VISIBLE);
                tvUpload.setVisibility(View.INVISIBLE);
            }

            addRoomBtn.setText("Update Room");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri sourceUri = data.getData();

            try (InputStream inputStream = getContentResolver().openInputStream(sourceUri)) {
                File file = new File(getFilesDir(), System.currentTimeMillis() + ".jpg");
                try (OutputStream outputStream = new FileOutputStream(file)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                imageUri = Uri.fromFile(file);
                imgPreview.setImageURI(imageUri);
                imgPreview.setVisibility(View.VISIBLE);
                tvUpload.setVisibility(View.INVISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnSave) {

            rmNumber = roomNum.getText().toString().trim();
            rmPrice = roomPrice.getText().toString().trim();
            rmDescription = roomDescription.getText().toString().trim();
            imagePath = imageUri.toString();
            if (rmNumber.isEmpty() || rmType.isEmpty() || rmStatus.isEmpty()
                    || rmPrice.isEmpty() || rmCapacity.isEmpty() || rmDescription.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (imagePath == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent result = new Intent();

            if (isEditing) {
                result.putExtra("roomId", roomId);
            }

            result.putExtra("roomNumber", rmNumber);
            result.putExtra("roomType", rmType);
            result.putExtra("roomStatus", rmStatus);
            result.putExtra("roomPrice", rmPrice);
            result.putExtra("roomCapacity", rmCapacity);
            result.putExtra("roomDescription", rmDescription);
            result.putExtra("roomImagePath", imageUri.toString());

            setResult(Activity.RESULT_OK, result);
            finish();
        }
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.roomTypeSpinner)
            rmType = parent.getItemAtPosition(position).toString();

        else if (parent.getId() == R.id.roomStatusSpinner)
            rmStatus = parent.getItemAtPosition(position).toString();

        else if (parent.getId() == R.id.roomCapacitySpinner)
            rmCapacity = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
