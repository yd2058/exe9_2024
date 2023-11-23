package com.example.exe9_2024;
/**
 * @author		Yiftah David yd2058@bs.amalnet.k12.il
 * @version	    1.1
 * @since		19/11/2023
 * this activity saves strings using external storage
 */
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity implements View.OnCreateContextMenuListener {
    private static final int REQUEST_CODE_PERMISSION = 1;
    private final String FILENAME = "ext.txt";


    TextView tVt;
    EditText eti;
    boolean accflg = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tVt = findViewById(R.id.tvt);
        eti = findViewById(R.id.eti);
        if(isExternalStorageAvailable()){
            if(!checkPermission()){requestPermission();}
            if(!checkPermission()){accflg = false;}
            else{tVt.setText(read());}
        }
        else{Toast.makeText(this, "No external memory available", Toast.LENGTH_LONG).show();}

    }
    public boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission to access external storage granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission to access external storage NOT granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * creates the context menu.
     * <p>
     *
     * @param	menu Description	refers to the current context menu.
     * @return	Description			returns a super action of this function.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    /**
     * reacts to item selection.
     * <p>
     *
     * @param	item Description	refers to the selected menu item.
     * @return	Description			returns true.
     */

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.btnc){
            Intent si = new Intent(this,credits.class);
            startActivity(si);
        }
        return true;
    }

    /**
     * saves text to external file using the save function and updates the screen in accordance.
     * <p>
     *
     * @param view refers to the current activity
     */


    public void saver(View view) {
        if (accflg) {
            save(false);
            tVt.setText(tVt.getText() + " " + eti.getText().toString());
            eti.setText("");
        }
        else{Toast.makeText(this, "Permission to access external storage NOT granted", Toast.LENGTH_LONG).show();}
    }
    /**
     * saves text in edittext into the external file .
     * <p>
     *
     * @param	res Description	refers to if current use resets the external file or not.
     */
    public void save(boolean res){
        try {
            File externalDir = Environment.getExternalStorageDirectory();
            File file = new File(externalDir, FILENAME);
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            if(!res) {writer.write(tVt.getText().toString()+" "+eti.getText().toString());}
            else{writer.write("");}
            writer.close();
        }
        catch(Exception e){
            Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * read text from the external file .
     * <p>
     *
     * @return	returns the text in the file if read was successful, otherwise returns null.
     */
    public String read(){
        try{
            File externalDir = Environment.getExternalStorageDirectory();
            File file = new File(externalDir, FILENAME);
            file.getParentFile().mkdirs();
            FileReader reader = new FileReader(file);
            BufferedReader bR = new BufferedReader(reader);
            StringBuilder sB = new StringBuilder();
            String line = bR.readLine();
            while (line != null) {
                sB.append(line+'\n');
                line = bR.readLine();
            }
            bR.close();
            reader.close();
            return sB.toString();
        }
        catch(Exception e){
            Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    /**
     * resets the external file using the save function.
     * <p>
     *
     * @param	view Description	refers current activity.
     */
    public void res(View view) {
        if(accflg){
            save(true);
            tVt.setText("");
        }
        else{Toast.makeText(this, "Permission to access external storage NOT granted", Toast.LENGTH_LONG).show();}
    }
    /**
     * saves text in edittext into the external file and ends the activity.
     * <p>
     *
     * @param	view Description	refers to current activity.
     */
    public void out(View view) {
        if (accflg) {
            save(false);
            tVt.setText(tVt.getText() + " " + eti.getText().toString());
        }
        finish();
    }
}