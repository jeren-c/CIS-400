package edu.syr.guardian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button mLoginButton;
    TextView mCreateButton;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email_login);
        mPassword = findViewById(R.id.login_pwd);
        fAuth = FirebaseAuth.getInstance();
        mLoginButton = findViewById(R.id.button_login);
        mCreateButton = findViewById(R.id.login_textview_bottom);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim(); //usr trim to format the data
                String pwd = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(pwd)){
                    mPassword.setError("Password is required");
                    return;
                }

                if(pwd.length() < 6){
                    mPassword.setError("Password has to contain at least 6 characters");
                    return;
                }

                // The above checks are used to make sure the input is valid

                //rogressBar.setVisibility(View.VISIBLE);

                //register the user into firebase
                fAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(Login.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
    }



}