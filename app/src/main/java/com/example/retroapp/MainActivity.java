package com.example.retroapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.retroapp.apiinterfaces.Apis;
import com.example.retroapp.pojojojo.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    EditText ename, eemail, ephone, epass;
    Button btn;
    String name, email, phone, pass;
    Retrofit retrofit;
    Apis service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retrofit = new Retrofit.Builder()
                .baseUrl(Apis.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(Apis.class);
        ename = findViewById(R.id.t1);
        eemail = findViewById(R.id.t2);

        epass = findViewById(R.id.t4);
        ephone = findViewById(R.id.t3);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = ename.getText().toString().trim();
                email = eemail.getText().toString().trim();

                pass = epass.getText().toString().trim();
                phone = ephone.getText().toString().trim();
                registerUser();
                Toast.makeText(MainActivity.this, "Info::" + name + "\t" + email + "\n" + pass + "\n" + phone, Toast.LENGTH_SHORT).show();

            }

            private void registerUser() {
                Call<User> call = service.registerUser(name, email, pass, phone);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.body().getResponse().matches("inserted")){
                            User user = response.body();
                            System.out.println("res"+response.body());
                        }
                        else if (response.body().getResponse().matches("exists")){
                            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("User SignUp");
                            builder.setMessage("Email Id already exists:");
                            builder.setCancelable(true);
                            builder.show();
                            User user = response.body();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        System.out.println("res"+t);
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }


}

