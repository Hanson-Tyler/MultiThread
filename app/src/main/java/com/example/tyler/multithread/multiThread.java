package com.example.tyler.multithread;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.example.tyler.multithread.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class multiThread extends ListActivity {
    public String filename;
    List<String> numbers = new ArrayList<String>();
    ProgressBar mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_thread);
        filename = "numbers.txt";
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void onCreateClick(View view) {
        new WriteThread().execute();
    }

    public void onLoadClick(View view) {
        new ReadThread().execute();
    }

    public void onClearClick(View view) {
        numbers.clear();
        ArrayAdapter adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, numbers);
        getListView().setAdapter(adapter);
    }

    public class WriteThread extends AsyncTask<FileOutputStream, Integer, Void> {

        FileOutputStream fos;
        BufferedWriter bw;
        int progressStatus1 = 0;


        @Override
        protected Void doInBackground(FileOutputStream... params) {

            try {
                fos = openFileOutput(filename, MODE_PRIVATE);
                bw = new BufferedWriter(new OutputStreamWriter(fos));
                for (int i = 1; i < 11; i++) {

                    String temp = Integer.toString(i);
                    bw.write(temp);
                    bw.newLine();
                    Thread.sleep(250);
                    publishProgress();
                }
                bw.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressStatus1 += 10;
            mProgress.setProgress(progressStatus1);

        }
    }


    public class ReadThread extends AsyncTask<FileInputStream, Integer, Void> {
        String message;
        FileInputStream fis;
        InputStreamReader isr;
        BufferedReader br;
        int progressStatus2 = 0;

        @Override
        protected Void doInBackground(FileInputStream... params) {
            try {
                fis = openFileInput(filename);
                isr = new InputStreamReader(fis);
                br = new BufferedReader(isr);

                while ((message = br.readLine()) != null) {
                    numbers.add(message);
                    Thread.sleep(250);
                    publishProgress();
                }

                br.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressStatus2 += 10;
            mProgress.setProgress(progressStatus2);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayAdapter adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, numbers);
            getListView().setAdapter(adapter);
        }
    }
}
