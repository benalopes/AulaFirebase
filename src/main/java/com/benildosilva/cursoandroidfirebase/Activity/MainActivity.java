package com.benildosilva.cursoandroidfirebase.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.benildosilva.cursoandroidfirebase.Classes.Usuario;
import com.benildosilva.cursoandroidfirebase.Dao.configuracaoFirebase;
import com.benildosilva.cursoandroidfirebase.R;
import com.benildosilva.cursoandroidfirebase.helper.Preferencias;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private EditText edtEmailLogin;
    private EditText edtSenhaLogin;
    private Button btnLogin;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        edtEmailLogin = (EditText) findViewById(R.id.edtEmail);
        edtSenhaLogin = (EditText) findViewById(R.id.edtSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        if (UsuarioLogado()) {
            Intent intentMinhaConta = new Intent(MainActivity.this, principalActivity.class);
           abrirNovaActivity(intentMinhaConta);
        } else {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!edtEmailLogin.getText().toString().equals("") && (!edtSenhaLogin.getText().toString().equals(""))) {
                        usuario = new Usuario();
                        usuario.setEmail(edtEmailLogin.getText().toString());
                        usuario.setSenha(edtSenhaLogin.getText().toString());

                        validarLogin();
                    } else {
                        Toast.makeText(MainActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
    private void validarLogin(){
        autenticacao = configuracaoFirebase.getFirebaseAuth();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail()
                .toString(),usuario.getSenha().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    AbrirTelaPrincipal();
                    Preferencias preferencias = new Preferencias(MainActivity.this);
                    preferencias.salvarUsuarioPreferencia(usuario.getEmail(),usuario.getSenha());
                    Toast.makeText(MainActivity.this, "Sucesso ao Logar!", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(MainActivity.this, "Usuário ou Senha inválidos! tente novamente", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void AbrirTelaPrincipal(){
        Intent intent = new Intent(MainActivity.this,principalActivity.class);
        finish();
        startActivity(intent);
    }

    public Boolean UsuarioLogado(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            return true;
        }else{
            return false;
        }
    }

    public void abrirNovaActivity(Intent intent){
        startActivity(intent);
    }

}