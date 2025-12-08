package com.example.dormitally;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RegisterForm extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText fullNameEt, ageEt, addressEt, occupationEt, emailEt, contactNumberEt, passwordEt, confirmPasswordEt, intentTextEt;
    Spinner spinnerGender, spinnerCivilStatus;
    CardView roomDetailsCard;
    Button submitBtn;
    ImageView backBtn;

    LinearLayout uploadBox, noRoomContainer;
    ImageView uploadedImage, uploadIcon, roomImage;
    TextView uploadTitle, uploadSubtitle, roomNum, roomPrice, roomCapacity, roomType, pricePer;

    String fullName = "", age = "", address = "", occupation = "", email = "", contactNumber = "";
    String password = "", confirmPassword = "", intent = "", civilStatus = "", gender = "";
    String rNum, rPrice, rCapacity,rType;
    Uri rImageUri;

    // NEW: store the valid ID URI
    Uri validIdUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_form);

        backBtn = findViewById(R.id.backBtn);
        uploadBox = findViewById(R.id.uploadBox);
        uploadedImage = findViewById(R.id.uploadedImage);
        uploadIcon = findViewById(R.id.uploadIcon);
        uploadTitle = findViewById(R.id.uploadTitle);
        uploadSubtitle = findViewById(R.id.uploadSubtitle);

        roomDetailsCard = findViewById(R.id.roomDetailsCard);
        noRoomContainer = findViewById(R.id.noRoomContainer);

        roomNum = findViewById(R.id.roomNum);
        roomPrice = findViewById(R.id.roomPrice);
        roomCapacity = findViewById(R.id.roomCapacity);
        roomType = findViewById(R.id.roomType);
        pricePer = findViewById(R.id.pricePer);
        roomImage = findViewById(R.id.roomImage);

        fullNameEt = findViewById(R.id.fullName);
        ageEt = findViewById(R.id.age);
        addressEt = findViewById(R.id.address);
        occupationEt = findViewById(R.id.occupation);
        emailEt = findViewById(R.id.email);
        contactNumberEt = findViewById(R.id.contactNumber);
        passwordEt = findViewById(R.id.password);
        confirmPasswordEt = findViewById(R.id.confirmPassword);
        intentTextEt = findViewById(R.id.intentText);

        submitBtn = findViewById(R.id.submitBtn);
        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerCivilStatus = findViewById(R.id.spinnerCivilStatus);

        spinnerGender.setOnItemSelectedListener(this);
        spinnerCivilStatus.setOnItemSelectedListener(this);

        submitBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        uploadBox.setOnClickListener(v -> openGallery());
        noRoomContainer.setOnClickListener(v -> openRoomSelectDialog());
        roomDetailsCard.setOnClickListener(v -> openRoomSelectDialog());

        // Load selected room
        int roomId = getIntent().getIntExtra("roomId", -1);
        if (roomId != -1) loadSelectedRoom(roomId);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // -----------------------------------------
    // PICK IMAGE (URI ONLY)
    // -----------------------------------------
    private void openGallery() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickIntent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            Uri sourceUri = data.getData();

            try (InputStream inputStream = getContentResolver().openInputStream(sourceUri)) {

                // Create permanent file inside app storage
                File file = new File(getFilesDir(), System.currentTimeMillis() + ".jpg");

                try (OutputStream outputStream = new FileOutputStream(file)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                // IMPORTANT: Use *copied* file as your permanent URI
                validIdUri = Uri.fromFile(file);   // ✅ FIXED

                // Preview image
                uploadedImage.setImageURI(validIdUri);
                uploadedImage.setVisibility(View.VISIBLE);

                uploadIcon.setVisibility(View.GONE);
                uploadTitle.setVisibility(View.GONE);
                uploadSubtitle.setVisibility(View.GONE);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // -----------------------------------------
    // SUBMIT REGISTRATION
    // -----------------------------------------
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.backBtn) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        if (v.getId() == R.id.submitBtn) {

            rPrice = roomPrice.getText().toString().trim();
            rCapacity = roomCapacity.getText().toString().trim();
            rType = roomType.getText().toString().trim();
            rNum = roomNum.getText().toString().trim();
            fullName = fullNameEt.getText().toString().trim();
            age = ageEt.getText().toString().trim();
            address = addressEt.getText().toString().trim();
            occupation = occupationEt.getText().toString().trim();
            email = emailEt.getText().toString().trim();
            contactNumber = contactNumberEt.getText().toString().trim();
            password = passwordEt.getText().toString().trim();
            confirmPassword = confirmPasswordEt.getText().toString().trim();
            intent = intentTextEt.getText().toString().trim();

            double price = parseDoubleSafe(rPrice);
            if (fullName.isEmpty() || age.isEmpty() || address.isEmpty() ||
                    occupation.isEmpty() || email.isEmpty() || contactNumber.isEmpty() ||
                    password.isEmpty() || confirmPassword.isEmpty() || intent.isEmpty() ||
                    civilStatus.isEmpty() || gender.isEmpty()) {

                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (noRoomContainer.getVisibility() == View.VISIBLE) {
                Toast.makeText(this, "Please choose a room.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (validIdUri == null) {
                Toast.makeText(this, "Please upload your valid ID.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            DBHelper db = new DBHelper(this);

            String today = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ?
                    LocalDate.now().toString() : "";

            Account account = new Account(
                    email,       // username
                    password,
                    "Tenant",
                    contactNumber,
                    email,
                    "Pending",
                    today
            );

            long userId = db.addAccount(account);
            if (userId == -1) {
                Toast.makeText(this, "Account registration failed.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert PersonInfo with URI STRING
            PersonInfo pi = new PersonInfo(
                    (int) userId,
                    fullName,
                    gender,
                    Integer.parseInt(age),
                    civilStatus,
                    intent,
                    validIdUri,
                    address
            );

            db.addPersonInfo(pi);

            // Insert Room Assignment
            RoomAssignment ra = new RoomAssignment(
                    (int) userId,
                    price,
                    rNum
            );

            db.addRoomAssignment(ra);

            Toast.makeText(this, "Registration successful!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    // -----------------------------------------
    // LOAD ROOM (USING URI)
    // -----------------------------------------
    private void loadSelectedRoom(int roomId) {
        DBHelper db = new DBHelper(this);
        Room room = db.getRoomById(roomId);

        if (room != null) {

            noRoomContainer.setVisibility(View.GONE);
            roomDetailsCard.setVisibility(View.VISIBLE);

            roomNum.setText("Room " + room.getRoomNum());
            roomType.setText(room.getRoomType());
            roomCapacity.setText("Capacity: " + room.getRoomCapacity() + " person(s)");
            roomPrice.setText("₱" + room.getRoomPrice());
            pricePer.setText("per month");

             rImageUri = room.getRoomImage();
             roomImage.setImageURI(rImageUri);

        }
    }

    private void openRoomSelectDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.list_room_dialog);
        dialog.getWindow().setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        RecyclerView roomListRV = dialog.findViewById(R.id.roomListRV);

        DBHelper db = new DBHelper(this);
        List<Room> roomList = db.getAvailableRooms();

        SelectRoomAdapter adapter = new SelectRoomAdapter(this, roomList, room -> {
            setSelectedRoom(room);
            dialog.dismiss();
        });

        roomListRV.setLayoutManager(new LinearLayoutManager(this));
        roomListRV.setAdapter(adapter);

        dialog.show();
    }

    private void setSelectedRoom(Room room) {

        noRoomContainer.setVisibility(View.GONE);
        roomDetailsCard.setVisibility(View.VISIBLE);

        roomNum.setText(room.getRoomNum());
        roomType.setText(room.getRoomType());
        roomCapacity.setText("Capacity: " + room.getRoomCapacity() + " person");
        roomPrice.setText("₱" + room.getRoomPrice());

        rImageUri = room.getRoomImage();

        roomImage.setImageURI(rImageUri);
    }

    // SPINNERS
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinnerGender)
            gender = parent.getItemAtPosition(position).toString().trim();
        else if (parent.getId() == R.id.spinnerCivilStatus)
            civilStatus = parent.getItemAtPosition(position).toString().trim();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
    private double parseDoubleSafe(String value) {
        if (value == null || value.trim().isEmpty()) return 0.0;

        // Remove ₱ and commas
        value = value.replace("₱", "").replace(",", "").trim();

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

}
