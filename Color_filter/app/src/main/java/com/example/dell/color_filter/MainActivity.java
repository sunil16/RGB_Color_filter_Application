package com.example.dell.color_filter;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

    ImageView imageView,vi1;
    SeekBar Bar1, Bar2, Bar3;
    Spinner redSpinner, greenSpinner, blueSpinner;
    TextView colorInfo;
    Button button;
    Button button1;
    private static int RESULT_LOAD_IMAGE = 1;

    String[] optColor  = { "Red", "Green", "Blue"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.iv);
        vi1 = (ImageView)findViewById(R.id.iv1);

        Bar1 = (SeekBar)findViewById(R.id.bar1);
        Bar2 = (SeekBar)findViewById(R.id.bar2);
        Bar3 = (SeekBar)findViewById(R.id.bar3);

        button=(Button)findViewById(R.id.cambtn);
        button1=(Button)findViewById(R.id.imgbtn);

        redSpinner = (Spinner)findViewById(R.id.ropt);
        greenSpinner = (Spinner)findViewById(R.id.gopt);
        blueSpinner = (Spinner)findViewById(R.id.bopt);

        colorInfo = (TextView)findViewById(R.id.colorinfo);

        ArrayAdapter<String> redArrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                optColor);
        redArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        redSpinner.setAdapter(redArrayAdapter);
        redSpinner.setSelection(0);

        ArrayAdapter<String> greenArrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                optColor);
        greenArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        greenSpinner.setAdapter(greenArrayAdapter);
        greenSpinner.setSelection(1);

        ArrayAdapter<String> blueArrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                optColor);
        blueArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blueSpinner.setAdapter(blueArrayAdapter);
        blueSpinner.setSelection(2);

        Bar1.setOnSeekBarChangeListener(colorBarChangeListener);
        Bar2.setOnSeekBarChangeListener(colorBarChangeListener);
        Bar3.setOnSeekBarChangeListener(colorBarChangeListener);

        redSpinner.setOnItemSelectedListener(colorSpinnerSelectedListener);
        greenSpinner.setOnItemSelectedListener(colorSpinnerSelectedListener);
        blueSpinner.setOnItemSelectedListener(colorSpinnerSelectedListener);

        setColorFilter(imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }

        });

    }

    OnSeekBarChangeListener colorBarChangeListener
            = new OnSeekBarChangeListener(){

        @Override
        public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
            setColorFilter(imageView);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    OnItemSelectedListener colorSpinnerSelectedListener
            = new OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            setColorFilter(imageView);

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    };

    private void setColorFilter(ImageView iv){
        float value1 = ((float)Bar1.getProgress())/255;
        float value2 = ((float)Bar2.getProgress())/255;
        float value3 = ((float)Bar3.getProgress())/255;

        int redColorSource = redSpinner.getSelectedItemPosition();
        int greenColorSource = greenSpinner.getSelectedItemPosition();
        int blueColorSource = blueSpinner.getSelectedItemPosition();

        float a, b, c, f, g, h, k, l, m;
        a = b = c = f = g = h = k = l = m = 0;

        String colorCombination = "";

        colorCombination += "RED = ";
        switch(redColorSource){
            case 0: a = value1;
                colorCombination += "red x " + String.valueOf(value1) +"\n";
                break;
            case 1: b = value1;
                colorCombination += "green x " + String.valueOf(value1) +"\n";
                break;
            case 2: c = value1;
                colorCombination += "blue x " + String.valueOf(value1) +"\n";
                break;
        }

        colorCombination += "GREEN = ";
        switch(greenColorSource){
            case 0: f = value2;
                colorCombination += "red x " + String.valueOf(value2) +"\n";
                break;
            case 1: g = value2;
                colorCombination += "green x " + String.valueOf(value2) +"\n";
                break;
            case 2: h = value2;
                colorCombination += "blue x " + String.valueOf(value2) +"\n";
                break;
        }

        colorCombination += "BLUE = ";
        switch(blueColorSource){
            case 0: k = value3;
                colorCombination += "red x " + String.valueOf(value3) +"\n";
                break;
            case 1: l = value3;
                colorCombination += "green x " + String.valueOf(value3) +"\n";
                break;
            case 2: m = value3;
                colorCombination += "blue x " + String.valueOf(value3) +"\n";
                break;
        }

        float[] colorMatrix = {
                a, b, c, 0, 0, //red
                f, g, h, 0, 0, //green
                k, l, m, 0, 0, //blue
                0, 0, 0, 1, 0  //alpha
        };

        colorInfo.setText(colorCombination);

        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);

        iv.setColorFilter(colorFilter);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.iv1);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            ImageView imageView1 = (ImageView) findViewById(R.id.iv);
            imageView1.setImageBitmap(BitmapFactory.decodeFile(picturePath));


        }


    }

}
