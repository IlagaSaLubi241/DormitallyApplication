package com.example.dormitally;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageConverter {

    private Context context;

    public ImageConverter(Context context) {
        this.context = context;
    }

    // Convert Uri to byte[]
    public byte[] uriToBytes(Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            return buffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Convert Uri to Base64 String
    public String uriToBase64(Uri uri) {
        byte[] bytes = uriToBytes(uri);
        if (bytes != null) {
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        }
        return null;
    }
    public Uri bytesToUri(Context context, byte[] bytes) {
        try {
            File tempFile = File.createTempFile("room_image_", ".jpg", context.getCacheDir());
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(bytes);
            fos.close();

            Uri uri = androidx.core.content.FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".fileprovider",
                    tempFile
            );
            return uri;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
