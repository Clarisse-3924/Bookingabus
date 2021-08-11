package com.example.bookingabus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class bookingRemera extends AppCompatActivity {
        DatabaseReference reff;
        EditText Name,Email,Initial,Destination,Date,Time;
        Button Submit;
        Remera book;
    private int mHour, mMinute,mSecond,mYear, mMonth, mDay;
    String format;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_booking_remera);


            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            Name = (EditText) findViewById(R.id.name);
            Email = (EditText) findViewById(R.id.email);
            Initial = (EditText) findViewById(R.id.hotel);
            Date = (EditText) findViewById(R.id.number);
            Destination = (EditText) findViewById(R.id.room);
            Time = (EditText) findViewById(R.id.Number2);
            reff = FirebaseDatabase.getInstance().getReference().child("BookRemera");
            Submit = (Button) findViewById(R.id.button6);
            book = new Remera();
            Date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(bookingRemera.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    String sDate=year+ "-"+(monthOfYear + 1)+"-"+dayOfMonth;
                                    Date.setText(sDate);
                                    //entryDate.setText(year+ "-"+(monthOfYear + 1)+"-"+dayOfMonth);

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                    datePickerDialog.show();
                }
            });

            Time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);
                    mSecond=c.get(Calendar.SECOND);
                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(bookingRemera.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    if (hourOfDay == 0) {

                                        hourOfDay += 12;

                                        format = "AM";
                                    }
                                    else if (hourOfDay == 12) {

                                        format = "PM";

                                    }
                                    else if (hourOfDay > 12) {

                                        hourOfDay -= 12;

                                        format = "PM";

                                    }
                                    else {

                                        format = "AM";
                                    }

                                    if((hourOfDay <= (c.get(Calendar.HOUR_OF_DAY)))&&
                                            (minute <= (c.get(Calendar.MINUTE)))){
                                        Toast.makeText(bookingRemera.this, "You can't pick a previous hour",
                                                Toast.LENGTH_SHORT).show();
                                    }else {
                                        Time.setText(hourOfDay + ":" + minute + ":" + mSecond);
                                    }
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }
            });

            Submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    book.setName(Name.getText().toString().trim());
                    book.setEmail(Email.getText().toString().trim());
                    book.setDate(Date.getText().toString().trim());
                    book.setDistination(Destination.getText().toString().trim());
                    book.setInitial(Initial.getText().toString().trim());
                    book.setTime(Time.getText().toString().trim());


                    String name = Name.getText().toString().trim();
                    String email = Email.getText().toString().trim();
                    String date = Date.getText().toString().trim();
                    String destination = Destination.getText().toString().trim();
                    String time = Time.getText().toString().trim();
                    String initial = Initial.getText().toString().trim();

                    if (TextUtils.isEmpty(name)) {
                        Name.setError("Name required");
                        return;
                    }
                    if (TextUtils.isEmpty(email)) {
                        Email.setError("Email required");
                        return;
                    }
                    if(email.length()!=10){
                        Email.setError("Phone number must be ten");
                        return;
                    }
//                    if (TextUtils.isEmpty(initial)) {
//                        Initial.setError("required");
//                        return;
//                    }

                    if (TextUtils.isEmpty(date)) {
                        Date.setError(" required");
                        return;
                    }
                    if (TextUtils.isEmpty(time)) {
                        Time.setError("required");
                        return;
                    }
//                    if (TextUtils.isEmpty(destination)) {
//                        Destination.setError("required");
//                        return;
//                    }

                    String id = reff.push().getKey();
                    reff.child(id).setValue(book);
                    makePayment();
                }
            });

        }
    private void makePayment(){
        String name = Name.getText().toString().trim();
        String email = Email.getText().toString().trim();
        new RaveUiManager(bookingRemera.this).setAmount(100)
                .setCurrency("RWF")
                .setEmail(email)
                .setfName(name)
                .setNarration("narration")
                .setPublicKey("FLWPUBK_TEST-4fbf2905625d6dbb8db7b241ac45170d-X")
                .setEncryptionKey("FLWSECK_TESTb103e1feecb2")
                .setTxRef("txRef")
                .setPhoneNumber("+250780603984", true)
                .acceptCardPayments(true)
                .acceptRwfMobileMoneyPayments(true)
                .withTheme(R.style.MyCustomTheme)
                .initialize();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "Payment Recieved Successfully ", Toast.LENGTH_SHORT).show();
                Toast.makeText(bookingRemera.this, "Thank you for Booking", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(bookingRemera.this, ContactUsActivity.class);
              startActivity(intent);
                finish();
            }
            else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR ", Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED ", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

