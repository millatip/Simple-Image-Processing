package com.tif.uin.millatip.myimageprocessing;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.ExecutionException;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int TAKE_PICTURE = 1;

    private Bitmap bitmap=null;
    private ImageView photoImage;
    private TextView result;

    final RxPermissions rxPermissions = new RxPermissions(this);

    Button upload, color, move;
    private AsyncTask<Bitmap, Void, String> kmeansTask;
    private Context context;
    private ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        pb = findViewById(R.id.pb);
        upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxPermissions
                        .request(Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean granted) throws Exception {
                                if (granted) { // Always true pre-M
                                    // I can control the camera now
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, TAKE_PICTURE);
                                } else {
                                    // Oups permission denied
                                }
                            }
                        });

            }
        });
        color = findViewById(R.id.btn_color);
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmap!=null)
                {
                    try {
                        kmeansTask = new KmeansTask();
                        kmeansTask.execute(bitmap);
                        pb.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        photoImage = findViewById(R.id.image);
        result = findViewById(R.id.result);
        move = findViewById(R.id.convo);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap!=null){
                    Intent moveActivityIntent = new Intent(MainActivity.this, EditActivity.class);
                    moveActivityIntent.putExtra("BitmapImage",bitmap );
                    startActivity(moveActivityIntent);
                }else {
                    Toast.makeText(getApplicationContext(),"No Image Seleccted" ,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            photoImage.setImageBitmap(bitmap);
        }
    }

    class KmeansTask extends AsyncTask<Bitmap, Void, String> {

        private Rgb[] mClusters;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {

            Kmeans k = new Kmeans(2500, 10, bitmaps[0]);
            k.initCLusters();
            k.startKmeans();
            mClusters = k.getClusters();
            int[] cnt = k.getCnt();
            ClusterToString cTs = new ClusterToString(mClusters, cnt);
            String string = cTs.getColours();
            return string;
        }

        @Override
        protected void onPostExecute(String s) {
            pb.setVisibility(View.GONE);
            try {
                String string = kmeansTask.get();
                result.setText(string);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }
}
