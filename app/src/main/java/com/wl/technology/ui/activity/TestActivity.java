package com.wl.technology.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin  on 2017/5/26 11:07.
 */

public class TestActivity extends AppCompatActivity {

    private List<String> list = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//requestWindowFeature(Window.FEATURE_NO_TITLE)
        list.add("asd");
        list.add("xcv");
        list.add("qwe");
        String path = "";
        File file = new File(path);
        try {
            InputStream fis = new FileInputStream(file);
            OutputStream fos = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int length = 0;
            try {
                while ((length = fis.read(bytes)) != -1) {
                    fos.write(bytes, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
