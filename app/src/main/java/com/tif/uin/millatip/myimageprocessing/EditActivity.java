package com.tif.uin.millatip.myimageprocessing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import io.github.kbiakov.codeview.CodeView;
import io.github.kbiakov.codeview.classifier.CodeProcessor;

public class EditActivity extends AppCompatActivity{

    public static final int FLIP_VERTICAL = 1;
    public static final int FLIP_HORIZONTAL = 2;

    public Bitmap bi, edited;
    ImageView iv, ivEdited;
    CodeView codeView;

    View[] views = new View[7];
    int res[] = {R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7};
    HorizontalScrollView horizontalScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        CodeProcessor.init(this);
        Intent intent = getIntent();
        bi = intent.getParcelableExtra("BitmapImage");
        iv = findViewById(R.id.imageBefore);
        ivEdited = findViewById(R.id.imageAfter);

        for (int i = 0; i < res.length; i++) {
            views[i] = findViewById(res[i]);
            views[i].setOnClickListener(new EditButtonListener(i));
        }
        horizontalScrollView = findViewById(R.id.scrollbutton);

        codeView = findViewById(R.id.code_view);
        iv.setImageBitmap(bi);
    }

    public class EditButtonListener implements View.OnClickListener {

        int whichButton;

        public EditButtonListener(int whichButton) {
            this.whichButton = whichButton;
        }

        @Override
        public void onClick(View view) {
            switch (whichButton){
                case 0:
                    rotate(bi, 180);
                    codeView.setCode("public void rotate(Bitmap src, float degree) {\n" +
                            "            // Parameternya adalah Bitmap di atas beserta derajat yang diinginkan\n" +
                            "            // Membuat matrix baru\n" +
                            "            Matrix matrix = new Matrix();\n" +
                            "            // Mengatur derajat rotasi\n" +
                            "            matrix.postRotate(degree);\n" +
                            "\n" +
                            "            //  Memberikan value bitmap baru kepada variabel edited yang telah dirotasi menggunakan Matrix\n" +
                            "            edited =  Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);\n" +
                            "        }", "java");
                    break;
                case 1:
                    flip(bi, 1);
                    codeView.setCode("public void flip(Bitmap src, int type) {\n" +
                            "            // Parameternya adalah Bitmap di atas beserta tipe yang diinginkan \n" +
                            "            // Membuat matrix baru untuk transformasi\n" +
                            "            Matrix matrix = new Matrix();\n" +
                            "            // Apabila vertikal\n" +
                            "            if(type == FLIP_VERTICAL) {\n" +
                            "                // y = y * -1\n" +
                            "                matrix.preScale(1.0f, -1.0f);\n" +
                            "            }\n" +
                            "            // Apabila horizontal\n" +
                            "            else if(type == FLIP_HORIZONTAL) {\n" +
                            "                // x = x * -1\n" +
                            "                matrix.preScale(-1.0f, 1.0f);\n" +
                            "            }\n" +
                            "\n" +
                            "            // Memberikan value bitmap baru kepada variabel edited yang telah ditransformasi menggunakan Matrix\n" +
                            "            edited = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);\n" +
                            "        }", "java");
                    break;
                case 2:
                    smooth(bi, 10);
                    codeView.setCode("public void smooth(Bitmap src, double value) {\n" +
                            "            // Fungsi ini menggunakan Matriks Konvolusi\n" +
                            "            // Matriks yang digunakan adalah \n" +
                            "            // [ 1 | 1 | 1 ]\n" +
                            "            // [ 1 | 5 | 1 ]\n" +
                            "            // [ 1 | 1 | 1 ]\n" +
                            "\n" +
                            "            ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);\n" +
                            "            convMatrix.setAll(1);\n" +
                            "            convMatrix.Matrix[1][1] = value;\n" +
                            "            convMatrix.Factor = value + 8;\n" +
                            "            convMatrix.Offset = 1;\n" +
                            "            edited = ConvolutionMatrix.computeConvolution3x3(src, convMatrix);\n" +
                            "        }", "java");
                    break;
                case 3:
                    sharpen(bi, 20);
                    codeView.setCode("public void sharpen(Bitmap src, double weight) {\n" +
                            "            // Parameternya adalah Bitmap di atas beserta berat sharpen yang diinginkan\n" +
                            "            // Fungsi ini menggunakan Matriks Konvolusi\n" +
                            "\n" +
                            "            double[][] SharpConfig = new double[][] {\n" +
                            "                    { 0 , -2    , 0  },\n" +
                            "                    { -2, weight, -2 },\n" +
                            "                    { 0 , -2    , 0  }\n" +
                            "            };\n" +
                            "            ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);\n" +
                            "            convMatrix.applyConfig(SharpConfig);\n" +
                            "            convMatrix.Factor = weight - 8;\n" +
                            "            edited = ConvolutionMatrix.computeConvolution3x3(src, convMatrix);\n" +
                            "        }", "java");
                    break;
                case 4:
                    applyMeanRemoval(bi);
                    codeView.setCode("public void applyMeanRemoval(Bitmap src) {\n" +
                            "            \n" +
                            "            // Parameternya hanyalah Bitmap di atas\n" +
                            "            // Fungsi ini menggunakan Matriks Konvolusi\n" +
                            "            double[][] MeanRemovalConfig = new double[][] {\n" +
                            "                    { -1 , -1, -1 },\n" +
                            "                    { -1 ,  9, -1 },\n" +
                            "                    { -1 , -1, -1 }\n" +
                            "            };\n" +
                            "            ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);\n" +
                            "            convMatrix.applyConfig(MeanRemovalConfig);\n" +
                            "            convMatrix.Factor = 1;\n" +
                            "            convMatrix.Offset = 0;\n" +
                            "            edited = ConvolutionMatrix.computeConvolution3x3(src, convMatrix);\n" +
                            "        }", "java");
                    break;
                case 5:
                    emboss(bi);
                    codeView.setCode("public void emboss(Bitmap src) {\n" +
                            "            \n" +
                            "            // Parameternya hanyalah Bitmap di atas\n" +
                            "            // Fungsi ini menggunakan Matriks Konvolusi\n" +
                            "            double[][] EmbossConfig = new double[][] {\n" +
                            "                    { -1 ,  0, -1 },\n" +
                            "                    {  0 ,  4,  0 },\n" +
                            "                    { -1 ,  0, -1 }\n" +
                            "            };\n" +
                            "            ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);\n" +
                            "            convMatrix.applyConfig(EmbossConfig);\n" +
                            "            convMatrix.Factor = 1;\n" +
                            "            convMatrix.Offset = 127;\n" +
                            "            edited = ConvolutionMatrix.computeConvolution3x3(src, convMatrix);\n" +
                            "        }", "java");
                    break;
                case 6:
                    engrave(bi);
                    codeView.setCode("        public void engrave(Bitmap src) {\n" +
                            "\n" +
                            "            // Parameternya hanyalah Bitmap di atas\n" +
                            "            // Fungsi ini menggunakan Matriks Konvolusi\n" +
                            "            ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);\n" +
                            "            convMatrix.setAll(0);\n" +
                            "            convMatrix.Matrix[0][0] = -2;\n" +
                            "            convMatrix.Matrix[1][1] = 2;\n" +
                            "            convMatrix.Factor = 1;\n" +
                            "            convMatrix.Offset = 95;\n" +
                            "            edited = ConvolutionMatrix.computeConvolution3x3(src, convMatrix);\n" +
                            "        }", "java");
                    break;

            }
            ivEdited.setImageBitmap(edited);
            //
        }

        public void rotate(Bitmap src, float degree) {
            // Parameternya adalah Bitmap di atas beserta derajat yang diinginkan
            // Membuat matrix baru
            Matrix matrix = new Matrix();
            // Mengatur derajat rotasi
            matrix.postRotate(degree);

            //  Memberikan value bitmap baru kepada variabel edited yang telah dirotasi menggunakan Matrix
            edited =  Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        }

        public void flip(Bitmap src, int type) {
            // Parameternya adalah Bitmap di atas beserta tipe yang diinginkan
            // Membuat matrix baru untuk transformasi
            Matrix matrix = new Matrix();
            // Apabila vertikal
            if(type == FLIP_VERTICAL) {
                // y = y * -1
                matrix.preScale(1.0f, -1.0f);
            }
            // Apabila horizontal
            else if(type == FLIP_HORIZONTAL) {
                // x = x * -1
                matrix.preScale(-1.0f, 1.0f);
            }

            // Memberikan value bitmap baru kepada variabel edited yang telah ditransformasi menggunakan Matrix
            edited = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        }

        public void smooth(Bitmap src, double value) {
            // Parameternya adalah Bitmap di atas beserta value smoothen yang diinginkan
            // Fungsi ini menggunakan Matriks Konvolusi
            // Matriks yang digunakan adalah
            // [ 1 | 1 | 1 ]
            // [ 1 | 5 | 1 ]
            // [ 1 | 1 | 1 ]

            ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
            convMatrix.setAll(1);
            convMatrix.Matrix[1][1] = value;
            convMatrix.Factor = value + 8;
            convMatrix.Offset = 1;
            edited = ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
        }

        public void sharpen(Bitmap src, double weight) {
            // Parameternya adalah Bitmap di atas beserta berat sharpen yang diinginkan
            // Fungsi ini menggunakan Matriks Konvolusi

            double[][] SharpConfig = new double[][] {
                    { 0 , -2    , 0  },
                    { -2, weight, -2 },
                    { 0 , -2    , 0  }
            };
            ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
            convMatrix.applyConfig(SharpConfig);
            convMatrix.Factor = weight - 8;
            edited = ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
        }

        public void applyMeanRemoval(Bitmap src) {

            // Parameternya hanyalah Bitmap di atas
            // Fungsi ini menggunakan Matriks Konvolusi
            double[][] MeanRemovalConfig = new double[][] {
                    { -1 , -1, -1 },
                    { -1 ,  9, -1 },
                    { -1 , -1, -1 }
            };
            ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
            convMatrix.applyConfig(MeanRemovalConfig);
            convMatrix.Factor = 1;
            convMatrix.Offset = 0;
            edited = ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
        }

        public void emboss(Bitmap src) {

            // Parameternya hanyalah Bitmap di atas
            // Fungsi ini menggunakan Matriks Konvolusi
            double[][] EmbossConfig = new double[][] {
                    { -1 ,  0, -1 },
                    {  0 ,  4,  0 },
                    { -1 ,  0, -1 }
            };
            ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
            convMatrix.applyConfig(EmbossConfig);
            convMatrix.Factor = 1;
            convMatrix.Offset = 127;
            edited = ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
        }
        public void engrave(Bitmap src) {

            // Parameternya hanyalah Bitmap di atas
            // Fungsi ini menggunakan Matriks Konvolusi
            ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
            convMatrix.setAll(0);
            convMatrix.Matrix[0][0] = -2;
            convMatrix.Matrix[1][1] = 2;
            convMatrix.Factor = 1;
            convMatrix.Offset = 95;
            edited = ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
        }

    }

}
