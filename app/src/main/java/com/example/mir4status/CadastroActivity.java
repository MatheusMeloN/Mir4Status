package com.example.mir4status;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText edtname;
    private EditText edtemail;
    private EditText edtconfirmemail;
    private EditText edtpassword;
    private EditText edtconfirmpassword;
    private Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        mAuth = FirebaseAuth.getInstance();
        edtname = findViewById(R.id.edtNameRegister);
        edtemail = findViewById(R.id.edtEmailRegister);
        edtconfirmemail = findViewById(R.id.edtEmailConfirmRegister);
        edtpassword = findViewById(R.id.edtPasswordRegister);
        edtconfirmpassword = findViewById(R.id.edtConfirmPasswordRegister);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel userModel = new UserModel();
                userModel.setName( edtname.getText().toString());
                userModel.setEmail( edtemail.getText().toString());
                userModel.setPassword( edtpassword.getText().toString());
                String registerConfirmaEmail = edtconfirmemail.getText().toString();
                String registerSenha = edtpassword.getText().toString();
                String registerConfirmaSenha = edtconfirmpassword.getText().toString();

                if(!TextUtils.isEmpty(userModel.getName())&&!TextUtils.isEmpty(userModel.getEmail())&&!TextUtils.isEmpty(registerConfirmaEmail)&&!TextUtils.isEmpty(userModel.getPassword())&&
                        !TextUtils.isEmpty(registerConfirmaSenha)&&!TextUtils.isEmpty(registerSenha)){
                    if(userModel.getEmail().equals(registerConfirmaEmail)&&
                            registerSenha.equals(registerConfirmaSenha)){
                        mAuth.createUserWithEmailAndPassword(userModel.getEmail(), registerSenha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    userModel.setId(mAuth.getUid());
                                    userModel.salvar();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CadastroActivity.this);
                                    //define o titulo

                                    //define a mensagem
                                    builder.setMessage("Cadastro Efetuado!");
                                    //Exibe
                                    builder.show();
                                    OpenMainActivity();
                                }else{
                                    String error;
                                    try{
                                        throw task.getException();
                                    }catch (FirebaseAuthWeakPasswordException e){
                                        error = "A senha deve conter no mínimo 6 caracteres";

                                    }catch (FirebaseAuthInvalidCredentialsException e){
                                        error = "E-mail inválido";
                                    }catch (FirebaseAuthUserCollisionException e){
                                        error = "E-mail  já existe";
                                    }catch (Exception e){
                                        error = "Erro ao efetuar o cadastro";
                                        e.printStackTrace();
                                    }
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CadastroActivity.this);
                                    //define o titulo
                                    builder.setTitle("Erro!");
                                    //define a mensagem
                                    builder.setMessage(error);
                                    //Exibe
                                    builder.show();
                                }
                            }
                        });
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(CadastroActivity.this);
                        //define o titulo
                        builder.setTitle("Erro!");
                        //define a mensagem
                        builder.setMessage("E-Mail ou senha não confere");
                        //Exibe
                        builder.show();
                    }
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CadastroActivity.this);
                    //define o titulo
                    builder.setTitle("Erro!");
                    //define a mensagem
                    builder.setMessage("Preencher todos os campos");
                    //Exibe
                    builder.show();
                }
            }
        });
    }

    private void OpenMainActivity() {
        Intent intent =  new Intent(CadastroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}