package com.tafh.tugasakhir.profile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tafh.tugasakhir.R;

public class ImageDialog extends Activity {
    private ImageView mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_view_fullimage_qrcode);



        mDialog = findViewById(R.id.iv_fullimage_qrcode);
        mDialog.setClickable(true);

        byte[] mBytes = getIntent().getByteArrayExtra("QRCODE");
        //now decode image because from previous activity we got our image in bytes
        Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length);

        mDialog.setImageBitmap(bitmap);

        //finish the activity (dismiss the image dialog) if the user clicks
        //anywhere on the image
        mDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
