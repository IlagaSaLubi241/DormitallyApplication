package com.example.dormitally;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dormitally.adminfragment.Rooms;

import java.io.IOException;

public class AddRoomPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    EditText roomNum,  roomPrice, roomDescription;
    Spinner spinnerRoomStatus,spinnerRoomCapacity,spinnerRoomType;
    Button addRoomBtn;
    ImageView imgPreview;
    FrameLayout uploadBtn;
    Uri imageUri = null;   // store selected image
    String rmNumber;
    String rmType;
    String rmStatus;
    String rmPrice;
    String rmCapacity;
    String rmDescription;
    byte[] rmImage = null;
    private static final int PICK_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room_page);

        // Bind Views
        roomNum = findViewById(R.id.etRoomNumber);
        spinnerRoomType = findViewById(R.id.roomTypeSpinner);
        spinnerRoomStatus = findViewById(R.id.roomStatusSpinner);
        roomPrice = findViewById(R.id.etRoomPrice);
        spinnerRoomCapacity = findViewById(R.id.roomCapacitySpinner);
        roomDescription = findViewById(R.id.etDescription);
        addRoomBtn = findViewById(R.id.btnSave);
        uploadBtn = findViewById(R.id.frameImage);
        imgPreview = findViewById(R.id.imgPreview);

        // Upload Image Listener
        uploadBtn.setOnClickListener(this);
        // Add Room Button Listener
        addRoomBtn.setOnClickListener(this);
        //Spinner
        spinnerRoomType.setOnItemSelectedListener(this);
        spinnerRoomStatus.setOnItemSelectedListener(this);
        spinnerRoomCapacity.setOnItemSelectedListener(this);
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
                imgPreview.setVisibility(ImageView.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ------------------------- SAVE TO DATABASE ------------------------

    private void saveRoom() {

        rmNumber = roomNum.getText().toString().trim();
        rmPrice = roomPrice.getText().toString().trim();
        rmDescription = roomDescription.getText().toString().trim();

        // Convert image
        ImageConverter converter = new ImageConverter(this);
        if (imageUri != null) {
            rmImage = converter.uriToBytes(imageUri);
        }

        // Validate fields
        if (rmNumber.isEmpty() || rmType.isEmpty() || rmStatus.isEmpty() || rmPrice.isEmpty() || rmCapacity.isEmpty() || rmDescription.isEmpty() || rmImage == null) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Room Object
        Room room;
        room = new Room(rmNumber,rmType,rmStatus,Double.parseDouble(rmPrice),Integer.parseInt(rmCapacity),rmDescription,rmImage);

        // Insert into Database
        DBHelper db = new DBHelper(this);
        long result = db.addRoom(room);

        if (result != -1) {
            Toast.makeText(this, "Room added successfully!", Toast.LENGTH_SHORT).show();
            finish(); // go back to fragment
        } else {
            Toast.makeText(this, "Failed to add room.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int selected = parent.getId();
        if(selected == R.id.roomTypeSpinner){
            rmType = parent.getItemAtPosition(position).toString();
        } else if (selected == R.id.roomStatusSpinner) {
            rmStatus = parent.getItemAtPosition(position).toString();
        } else if (selected == R.id.roomCapacitySpinner){
            rmCapacity = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSave) {
            Intent intent = new Intent(this, Rooms.class);
            saveRoom();
            startActivity(intent);
        } else if (id == R.id.frameImage) {
            openGallery();
        }
    }
}
