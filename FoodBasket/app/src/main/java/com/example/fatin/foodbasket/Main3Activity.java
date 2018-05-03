package com.example.fatin.foodbasket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main3Activity extends AppCompatActivity {


    private EditText name;
    private EditText password;
    private Button login;
    private Button register;
    private Button forgot;
    //  private TextView Info;
    private int counter = 5;
    FirebaseDatabase fBase;
    DatabaseReference dataRef;

    String _user_email ="";
    String _pasword="";
    String val;

    ProgressDialog progressDialog;

    //authentication variable
    FirebaseAuth firebaseAuth = null;
    FirebaseAuth.AuthStateListener authStateListener;

    String TAG = "MAIN_TEST";

   // static ArrayList<String> email_ =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        HideKeyBoard.hideKeyPad(findViewById(R.id.home), Main3Activity.this);
        name = (EditText) findViewById(R.id.etName);
        password = (EditText) findViewById(R.id.etPassword);
        login = (Button) findViewById(R.id.btnLogin);
        register = (Button) findViewById(R.id.btnReg);

        //instantiate firebaseauth
        firebaseAuth = FirebaseAuth.getInstance();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebase_auth) {
                if (firebase_auth.getCurrentUser() != null) {
                    if(!firebase_auth.getCurrentUser().isEmailVerified()) {
                        //firebase_auth.signOut();

                        //Toast.makeText(Main3Activity.this,"account may not have been verified", Toast.LENGTH_LONG).show();

                    }else {
                        Intent intent = new Intent(Main3Activity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    }
                    else {
                    //firebase_auth.getCurrentUser().reload();
                    firebase_auth.signOut();
                    //finish();

                }

            }
        };

        forgot= (Button)findViewById(R.id.btnForgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main6Activity();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _user_email = name.getText().toString().trim();
                _pasword = password.getText().toString();
                String ret;
                if (_user_email.contains("@")){
                    loginUserWithEmail(_user_email,_pasword);

                }else {
                    loginUserWithUserName(_user_email, _pasword);
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();

            }
        });

    }

    private void verifiedEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }

    private void loginUserWithEmail(String uemail, String pword) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("login in progress...");
        progressDialog.show();
        if (ValidateFieldInput.fieldsNotEmpty(uemail, pword)) {
            firebaseAuth.signInWithEmailAndPassword(uemail, pword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        password.setText("");
                        progressDialog.dismiss();
                        inValidLogin();
                    } else {
                        //checkIfEmailVerified();
                        progressDialog.dismiss();


                        //  Intent intent = new Intent(Main3Activity.this,MainActivity.class);
                        //  startActivity(intent);
                    }

                }
            });
        } else {
            password.setText("");
            progressDialog.dismiss();
            inValidLogin();
        }
        if(firebaseAuth.getCurrentUser() != null && !firebaseAuth.getCurrentUser().isEmailVerified()) {
            //login.setVisibility(View.GONE);
            firebaseAuth.getCurrentUser().reload();
            Toast.makeText(Main3Activity.this,"account may not have been verified", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();


        }
    }

    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.reload();

        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            finish();
            Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            FirebaseAuth.getInstance().signOut();

            //restart this activity

        }
    }

    private void loginUserWithUserName(final String user_email, final String pword) {
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("login in progress...");
        final DatabaseReference user = FirebaseDatabase.getInstance().getReference("users").child(user_email);
        //Log.d(TAG, "loginUserWithUserName: "+user.getKey());
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String retVal=dataSnapshot.child("email").getValue().toString();
                    if (ValidateFieldInput.fieldsNotEmpty(retVal, pword)) {
                        progressDialog.show();
                        firebaseAuth.signInWithEmailAndPassword(retVal, pword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    password.setText("");
                                    progressDialog.dismiss();
                                    inValidLogin();
                                } else {
                                    //checkIfEmailVerified();
                                    progressDialog.dismiss();
                                    //  Intent intent = new Intent(Main3Activity.this,MainActivity.class);
                                    //  startActivity(intent);
                                }

                            }
                        });
                    } else {
                        password.setText("");
                        progressDialog.dismiss();
                        inValidLogin();
                    }
                }catch (Exception ex){
                    password.setText("");
                    Toast.makeText(Main3Activity.this, "The entered user name doesn't exist", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(firebaseAuth.getCurrentUser() != null && !firebaseAuth.getCurrentUser().isEmailVerified()) {

            //login.setVisibility(View.GONE);
            firebaseAuth.getCurrentUser().reload();
            Toast.makeText(Main3Activity.this,"account may not have been verified", Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();


        }

        }


    @Override
    protected void onStart() {
        super.onStart();
            firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
       firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void inValidLogin() {
        Toast.makeText(Main3Activity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
    }


    public void signup() {
        Intent intent = new Intent(Main3Activity.this, Register.class);
        startActivity(intent);
    }
    public void main6Activity(){
        Intent intent = new Intent(Main3Activity.this, ResetPassWord.class);
        startActivity(intent);
    }

}
