package com.karma.d2d_admin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karma.d2d_admin.R;
import com.karma.d2d_admin.domains.Constants;

import static com.karma.d2d_admin.utilities.Utils.RELEASE_TYPE;

public class LoginActivity extends AppCompatActivity {
    EditText etMail,etPassword;
    String mail,password,curUser,region;
    TextView tvLogin,newAccount;
    private FirebaseAuth cfAuth;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etMail=findViewById(R.id.et_email);
        etPassword=findViewById(R.id.et_password);
        tvLogin=findViewById(R.id.tv_login);
        newAccount=findViewById(R.id.tv_signin);


        cfAuth=FirebaseAuth.getInstance();
        try {

            curUser = cfAuth.getCurrentUser().getUid();
            if (curUser != null) {
                Intent intent = new Intent(LoginActivity.this, BottomBarMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        userRef= FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("User");
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail=etMail.getText().toString();
                password=etPassword.getText().toString();
                if (mail.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter your mail id", Toast.LENGTH_SHORT).show();
                }
                else if (password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
                }else {
                    allowUserToLogin(mail);
                }


            }
        });
    }

    private void allowUserToLogin(final String mail) {
        cfAuth.signInWithEmailAndPassword(this.mail,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            switch (mail){
                                case "admin@karma.com":
                                    region="Main";
                                    break;
                                case "d2dmannar@karma.com":
                                    region="Mannar";
                                    break;
                                case "d2djaffna@karma.com":
                                    region="Jaffna";
                                    break;
                                case "d2dkandy@karma.com":
                                    region="Kandy";
                                    break;
                                case "d2dcolombo@karma.com":
                                    region="Colombo";
                                    break;
                                case "d2dmatale@karma.com":
                                    region="Matale";
                                    break;
                                case "d2dkilinochi@karma.com":
                                    region="Kilinochi";
                                    break;
                                case "d2dmullaitivu@karma.com":
                                    region = "Mullaitivu";
                                    break;
                                case "d2dnuwaraeliya@karma.com":
                                    region = "NuwaraEliya";
                                    break;
                                case "d2dampara@karma.com":
                                    region = "Ampara";
                                    break;
                                case "d2danuradhapura@karma.com":
                                    region = "Anuradhapura";
                                    break;
                                case "d2dbadulla@karma.com":
                                    region = "Badulla";
                                    break;
                                case "d2dbatticaloa@karma.com":
                                    region = "Batticaloa";
                                    break;
                                case "d2dgalle@karma.com":
                                    region = "Galle";
                                    break;
                                case "d2dgampaha@karma.com":
                                    region = "Gampaha";
                                    break;
                                case "d2dhambantota@karma.com":
                                    region = "Hambantota";
                                    break;
                                case "d2dkalutura@karma.com":
                                    region = "Kalutara";
                                    break;
                                case "d2dkegalle@karma.com":
                                    region = "Kegalle";
                                    break;
                                case "d2dkurunegala@karma.com":
                                    region = "Kurunegala";
                                    break;
                                case "d2damatara@karma.com":
                                    region = "Matara";
                                    break;
                                case "d2dmonaragala@karma.com":
                                    region = "Monaragala";
                                    break;
                                case "d2dpolannaruwa@karma.com":
                                    region = "Polannaruwa";
                                    break;
                                case "d2dputtalam@karma.com":
                                    region = "Puttalam";
                                    break;
                                case "d2dratnapura@karma.com":
                                    region = "Ratnapura";
                                    break;
                                case "d2dtrincomalee@karma.com":
                                    region = "Trincomalee";
                                    break;
                                case "d2dvavuniya@karma.com":
                                    region = "Vavuniya";
                                    break;

                            }
                            SharedPreferences.Editor editor=getSharedPreferences("REGION_SELECTOR",MODE_PRIVATE).edit();
                            editor.putString("REGION",region);
                            editor.apply();
                            validateProfile();
                            // SendUserToMainActivity();
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String message=task.getException().getMessage();
                            Toast.makeText(LoginActivity.this, "Error.."+message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void validateProfile() {
        if (cfAuth.getCurrentUser().getUid().equals(Constants.ADMIN_ID)){
            Intent intent=new Intent(LoginActivity.this,BottomBarMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else {
            Intent intent=new Intent(LoginActivity.this,BottomBarMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
