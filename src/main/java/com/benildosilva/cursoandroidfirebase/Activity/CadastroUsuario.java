package com.benildosilva.cursoandroidfirebase.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroUsuario extends AppCompatActivity {
    private EditText email;
    private EditText senha1;
    private EditText senha2;
    private EditText nome;
    private RadioButton rbAdmin;
    private RadioButton rbAtend;
    private Button btnCadastrar;
    private Button btnCancelar;
    private FirebaseAuth autenticacao;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro_usuario);


        email = (EditText) findViewById(R.id.edtCadEmail);
        senha1 = (EditText) findViewById(R.id.edtCadSenha1);
        senha2 = (EditText) findViewById(R.id.edtCadSenha2);
        nome = (EditText) findViewById(R.id.edtCadNome);
        rbAdmin = (RadioButton) findViewById(R.id.rbAdmin);
        rbAtend = (RadioButton) findViewById(R.id.rbAtend);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (senha1.getText().toString().equals(senha2.getText().toString())) {
                    usuario = new Usuario();

                    usuario.setEmail(email.getText().toString());
                    usuario.setSenha(senha1.getText().toString());
                    usuario.setNome(nome.getText().toString());

                    if (rbAdmin.isChecked()) {
                        usuario.setTipoUsuario("Administrador");
                    } else if (rbAtend.isChecked()) {
                        usuario.setTipoUsuario("Atendente");
                    }

                    //chamada de método para cadastro de usuários
                    cadastrarUsuario();

                } else {
                    Toast.makeText(CadastroUsuario.this, "As senhas não se correspondem!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cadastrarUsuario() {

        autenticacao = configuracaoFirebase.getFirebaseAuth();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuario.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    insereUsuario(usuario);

                    finish();

                    //deslogar ao adicionar o usuário
                    autenticacao.signOut();

                    //para abrir a nossa tela principal após a re-autenticação
                    abreTelaPrincipal();


                } else {

                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, contendo no mínimo 8 caracteres e que contenha letras e números!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail digitado é invalido, digite um novo e-mail";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse e-mail já está cadastro!";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuario.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private boolean insereUsuario(Usuario usuario) {

        try {

            reference = configuracaoFirebase.getFirebase().child("usuarios");
            reference.push().setValue(usuario);
            Toast.makeText(CadastroUsuario.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
            return true;

        } catch (Exception e) {
            Toast.makeText(CadastroUsuario.this, "Erro ao gravar o usuário!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }

    private void abreTelaPrincipal() {

        autenticacao = configuracaoFirebase.getFirebaseAuth();

        Preferencias preferencias = new Preferencias(CadastroUsuario.this);

        autenticacao.signInWithEmailAndPassword(preferencias.getEmail_Usuario_logado(), preferencias.getSenha_usuario_logado()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Intent intent = new Intent(CadastroUsuario.this, principalActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    Toast.makeText(CadastroUsuario.this, "Falha!", Toast.LENGTH_LONG).show();
                    autenticacao.signOut();
                    Intent intent = new Intent(CadastroUsuario.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }

            }
        });

    }

}

