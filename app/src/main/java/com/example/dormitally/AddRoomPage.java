package com.example.dormitally;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class AddRoomPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private EditText roomNum, roomPrice, roomDescription;
    private Spinner spinnerRoomType, spinnerRoomStatus, spinnerRoomCapacity;
    private Button addRoomBtn, cancelBtn;
    private ImageView imgPreview;
    private FrameLayout uploadBtn;

    private Uri imageUri = null;
    private byte[] rmImage = null;

    private String rmNumber = "", rmType = "", rmStatus = "", rmPrice = "", rmCapacity = "", rmDescription = "";

    private int roomId = -1;   // For update mode
    private boolean isEditing = false;

    private static final int PICK_IMAGE = 101;

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

        spinnerRoomType.setOnItemSelectedListener(this);
        spinnerRoomStatus.setOnItemSelectedListener(this);
        spinnerRoomCapacity.setOnItemSelectedListener(this);
        addRoomBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);

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

            byte[] imageBytes = b.getByteArray("roomImage");
            if (imageBytes != null) {
                rmImage = imageBytes;
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                imgPreview.setImageBitmap(bitmap);
                imgPreview.setVisibility(View.VISIBLE);
            }

            addRoomBtn.setText("Update Room");
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imgPreview.setImageBitmap(bitmap);
                imgPreview.setVisibility(View.VISIBLE);

                ImageConverter converter = new ImageConverter(this);
                rmImage = converter.uriToBytes(imageUri);

            } catch (IOException e) {
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

            if (rmNumber.isEmpty() || rmType.isEmpty() || rmStatus.isEmpty()
                    || rmPrice.isEmpty() || rmCapacity.isEmpty() || rmDescription.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (rmImage == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent data = new Intent();

            if (isEditing) data.putExtra("roomId", roomId);

            data.putExtra("roomNumber", rmNumber);
            data.putExtra("roomType", rmType);
            data.putExtra("roomStatus", rmStatus);
            data.putExtra("roomPrice", rmPrice);
            data.putExtra("roomCapacity", rmCapacity);
            data.putExtra("roomDescription", rmDescription);
            data.putExtra("roomImage", rmImage);

            setResult(Activity.RESULT_OK, data);
            finish();

        } else if (v.getId() == R.id.frameImage) {
            openGallery();

        } else if (v.getId() == R.id.btnCancel) {
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
