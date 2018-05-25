package com.left.gaad.fileiodemocode;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements View.OnClickListener{


    private static final String TAG=MainActivity.class.getSimpleName();

    private static final String FILE_NAME="sample.txt";

    private Context mContext;

    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferences1;

    private Button buttonWriteToFile,buttonReadFromFile;
    private TextView textViewContentFromFile;
    private EditText editTextUserMessage;
    private UserAction recentUserAction;
    enum UserAction{
        READ,WRITE
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext=getApplicationContext();

        buttonReadFromFile=(Button)findViewById(R.id.buttonWriteToFile);
        buttonWriteToFile=(Button)findViewById(R.id.buttonReadFromFile);
        textViewContentFromFile=(TextView)findViewById(R.id.textViewContentFromFile);
        editTextUserMessage=(EditText)findViewById(R.id.editTextUserMessage);

        buttonReadFromFile.setOnClickListener(this);
        buttonWriteToFile.setOnClickListener(this);

        sharedPreferences=getSharedPreferences(getPackageName()+"."+TAG,MODE_PRIVATE);
        sharedPreferences1=getSharedPreferences(TAG,MODE_PRIVATE);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.buttonWriteToFile: writeContentToFile(); break;
            case R.id.buttonReadFromFile: populateTheReadText();break;
            default: break;
        }
    }


    private void writeContentToFile(){
        recentUserAction=UserAction.WRITE;
        String string=editTextUserMessage.getText().toString();

        if(isStringEmpty(string)){
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("SAMPLE_KEY",string);
            editor.commit();

            SharedPreferences.Editor editor2=sharedPreferences1.edit();
            editor2.putString("SAMPLE",string);
            editor2.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        populatTextFromPreviousSession();
    }

    private void populateTheReadText(){
        recentUserAction=UserAction.READ;
        textViewContentFromFile.setText(sharedPreferences.getString("SAMPLE_KEY","String not found"));
    }

    private void populatTextFromPreviousSession(){
        recentUserAction=UserAction.READ;
        textViewContentFromFile.setText("From Previous Session: \n "+sharedPreferences.getString("SAMPLE_KEY","String not found"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(arePermissionsGranted(permissions) && requestCode== EXTERNAL_STORAGE_PERMISSION){
            if(recentUserAction==UserAction.WRITE){
                writeContentToFile();
            }else if(recentUserAction==UserAction.READ){
                populateTheReadText();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_main_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
