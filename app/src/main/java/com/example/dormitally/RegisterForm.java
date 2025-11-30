package com.example.dormitally;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterForm extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView backBtn;
    LinearLayout uploadBox;
    ImageView uploadedImage, uploadIcon;
    TextView uploadTitle, uploadSubtitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_form);
        backBtn = findViewById(R.id.backBtn);
        uploadBox = findViewById(R.id.uploadBox);
        uploadBox = findViewById(R.id.uploadBox);
        uploadedImage = findViewById(R.id.uploadedImage);
        uploadIcon = findViewById(R.id.uploadIcon);
        uploadTitle = findViewById(R.id.uploadTitle);
        uploadSubtitle = findViewById(R.id.uploadSubtitle);

        uploadBox.setOnClickListener(v -> openGallery());
        backBtn.setOnClickListener(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            // Display the uploaded image
            uploadedImage.setImageURI(imageUri);
            uploadedImage.setVisibility(View.VISIBLE);

            // Hide upload UI
            uploadIcon.setVisibility(View.GONE);
            uploadTitle.setVisibility(View.GONE);
            uploadSubtitle.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.backBtn){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }
}