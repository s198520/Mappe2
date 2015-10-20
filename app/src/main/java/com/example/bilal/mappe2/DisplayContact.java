package com.example.bilal.mappe2;

/**
 * Created by bilal on 10/16/2015.
 */

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayContact extends Activity {
    private DBHelper mydb ;

    TextView name ;
    TextView phone;
    TextView day;
    TextView year;
    TextView month;
    TextView errorMsg;
    Button button;
    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);
        name = (TextView) findViewById (R.id.editTextName);
        phone = (TextView) findViewById (R.id.editTextPhone);
        day = (TextView) findViewById (R.id.editTextDay);
        year = (TextView) findViewById (R.id.editTextYear);
        month = (TextView) findViewById (R.id.editTextMonth);
        button = (Button) findViewById (R.id.button1);

        name.addTextChangedListener(nameWatcher);
        phone.addTextChangedListener(phoneWatcher);
        day.addTextChangedListener(dayWatcher);
        month.addTextChangedListener(monthWatcher);
        year.addTextChangedListener(yearWatcher);

        errorMsg = (TextView) findViewById(R.id.errorMsg);
        button.setVisibility(View.GONE);

        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            int Value = extras.getInt("id");

            if(Value>0){
                //means this is the view part not the add contact part.
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();

                String nam = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
                String phon = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
                String da = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_DAY));
                String yea = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_YEAR));
                String mont = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_MONTH));

                if (!rs.isClosed())
                {
                    rs.close();
                }
                Button b = (Button)findViewById(R.id.button1);
                b.setVisibility(View.INVISIBLE);

                name.setText((CharSequence)nam);
                name.setFocusable(false);
                name.setClickable(false);

                phone.setText((CharSequence)phon);
                phone.setFocusable(false);
                phone.setClickable(false);

                day.setText((CharSequence)da);
                day.setFocusable(false);
                day.setClickable(false);

                year.setText((CharSequence)yea);
                year.setFocusable(false);
                year.setClickable(false);

                month.setText((CharSequence)mont);
                month.setFocusable(false);
                month.setClickable(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();

        if(extras !=null)
        {
            int Value = extras.getInt("id");
            if(Value>0){
                getMenuInflater().inflate(R.menu.display_contact, menu);
            }

            else{
                getMenuInflater().inflate(R.menu.menu_main, menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch(item.getItemId())
        {
            case R.id.Edit_Contact:
                Button b = (Button)findViewById(R.id.button1);
                b.setVisibility(View.VISIBLE);

                name.setEnabled(true);
                name.setFocusableInTouchMode(true);
                name.setClickable(true);

                phone.setEnabled(true);
                phone.setFocusableInTouchMode(true);
                phone.setClickable(true);

                day.setEnabled(true);
                day.setFocusableInTouchMode(true);
                day.setClickable(true);

                year.setEnabled(true);
                year.setFocusableInTouchMode(true);
                year.setClickable(true);

                month.setEnabled(true);
                month.setFocusableInTouchMode(true);
                month.setClickable(true);

                return true;
            case R.id.Delete_Contact:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteContact)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteContact(id_To_Update);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("Are you sure");
                d.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void run(View view)
    {
        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            int Value = extras.getInt("id");
            if(Value>0){
                if(mydb.updateContact(id_To_Update,name.getText().toString(), phone.getText().toString(), day.getText().toString(), year.getText().toString(), month.getText().toString() )){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                if(mydb.insertContact(name.getText().toString(), phone.getText().toString(), day.getText().toString(), year.getText().toString(), month.getText().toString())){
                    Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }
    }
    private final TextWatcher nameWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            errorMsg.setText("Husk å legg til navn");
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            if (s.length() <= 0) {
                errorMsg.setText("Add your name");
                errorMsg.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
            } else{
                errorMsg.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
            }
        }
    };

    private final TextWatcher phoneWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            errorMsg.setText("Husk å legg til tlf");
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            if (s.length() <= 0) {
                errorMsg.setText("Add your phone number");
                errorMsg.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
            } else if(s.length() >8){
                errorMsg.setText("No more than 8 numbers");
                button.setVisibility(View.GONE);
            } else {
                errorMsg.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
            }
        }
    };

    private final TextWatcher monthWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            if (s.length() <= 0) {
                errorMsg.setText("Add a day");
                errorMsg.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
            } else if(s.length() == 1){
                errorMsg.setText("Add a '0' in front of your day ie:'01'");
                button.setVisibility(View.GONE);
            } else if(s.length() > 2){
                errorMsg.setText("No more than 2 numbers in a day");
                button.setVisibility(View.GONE);
            } else {
                errorMsg.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
            }
        }
    };

    private final TextWatcher dayWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            if (s.length() <= 0) {
                errorMsg.setText("Add a day");
                errorMsg.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
            } else if(s.length() == 1){
                errorMsg.setText("Add a '0' infront of your day ie:'01'");
                button.setVisibility(View.GONE);
            } else if(s.length() > 2){
                errorMsg.setText("No more than 2 numbers in a day");
                button.setVisibility(View.GONE);
            } else {
                errorMsg.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
            }
        }
    };

    private final TextWatcher yearWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            errorMsg.setText("Husk å legg til year");
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            if (s.length() <= 0) {
                errorMsg.setText("Add a day");
                errorMsg.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
            } else if(s.length() < 4){
                errorMsg.setText("Too old");
                button.setVisibility(View.GONE);
            } else if(s.length() > 4){
                errorMsg.setText("Too young");
                button.setVisibility(View.GONE);
            } else {
                errorMsg.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
            }
        }
    };

}
