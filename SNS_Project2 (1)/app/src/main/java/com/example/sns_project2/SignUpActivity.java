package com.example.sns_project2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

//회원가입
public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    ImageView imageView ;
    private final static String TAG="SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoLoginButton).setOnClickListener(onClickListener);

        final Button b = (Button) findViewById(R.id.sex);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), view);
                popup.getMenuInflater().inflate(R.menu.sex, popup.getMenu());
                popup.setOnMenuItemClickListener(
                        new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case R.id.men :
                                        b.setText("남자");
                                        break;

                                    case R.id.girl :
                                        b.setText("여자");
                                        break;
                                }
                                return true;
                            }
                        }
                );
            popup.show();
            }
        });

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.signUpButton:
                    signUp();
                    break;
                case R.id.gotoLoginButton:
                    myStartActivity(LoginActivity.class);
                    break;
            }
        }
    };

    private void signUp() {
        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();
        String passwordCheck = ((EditText)findViewById(R.id.passwordCheckEditText)).getText().toString();
        String name = ((EditText)findViewById(R.id.name)).getText().toString();
        String age = ((EditText)findViewById(R.id.age)).getText().toString();


        if(email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0 && name.length() > 0 && age.length() > 0){
            if(password.equals(passwordCheck)){

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    profileUpdate();
                                    myStartActivity(LoginActivity.class);
                                } else {
                                    if(task.getException() != null){
                                        startToast( "회원가입에 실패했습니다.");
                                        finish();
                                    }
                                }
                            }
                        });
            }else{
                startToast( "비밀번호가 일치하지 않습니다.");
            }
        }
        //이메일 안쳤을 때
        else if(email.length() == 0){
            startToast( "이메일을 입력해주세요.");
        }
        //비밀번호 또는 비밀번호 체크 안쳤을 때
        else if(password.length() == 0 || passwordCheck.length() == 0){
            startToast( "비밀번호를 입력해주세요.");
        }
        //이름 안쳤을 때
        else if(name.length() == 0){
            startToast( "이름을 입력해주세요.");
        }
        //나이 안쳤을 때
        else if(age.length() == 0){
            startToast( "나이를 입력해주세요.");
        }

        /*else {
            startToast( "이메일 또는 비밀번호를 입력해 주세요.");
        }

         */
    }

    private void startToast(String msg){
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
//회원정보 등록
    private void profileUpdate(){
        final String name = ((EditText)findViewById(R.id.name)).getText().toString();
        final String age = ((EditText)findViewById(R.id.age)).getText().toString();
        final String sex = ((Button)findViewById(R.id.sex)).getText().toString();
        final String status = "상태메시지를 입력해주세요";
        if(name.length() > 0 && age.length() > 0 && sex.length() > 1 ){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            Memberinfo memberinfo = new Memberinfo(name, age, sex,status);

            if(user!= null){
                db.collection("user").document(user.getUid()).set(memberinfo.getMemberinfo())

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("회원 등록에 실패하였습니다.");
                                Log.w(TAG, "Error writing document", e);
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("회원 가입 및 등록에 성공하였습니다.");
                            }
                        });
            }

        }else{
            startToast("회원 정보를 입력해주세요.");
        }

    }

}
