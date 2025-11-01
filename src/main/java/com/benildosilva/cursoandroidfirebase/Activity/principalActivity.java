package com.benildosilva.cursoandroidfirebase.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.benildosilva.cursoandroidfirebase.R;
import com.google.firebase.auth.FirebaseAuth;

public class principalActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);
        autenticacao = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add_usuario){
            abrirTelaCadUsuario();

        }else if (id == R.id.action_sair_admin){
            delogarUsuario();

        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirTelaCadUsuario(){
        Intent intent = new Intent(principalActivity.this, CadastroUsuario.class);
        startActivity(intent);
    }
    private void  delogarUsuario(){
        autenticacao.signOut();
        Intent intent = new Intent(principalActivity.this,MainActivity.class);
        startActivity(intent);
        finish();

    }
}