package com.benildosilva.cursoandroidfirebase.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {

    private Context context;
    private SharedPreferences preferencias;
    private String NOME_ARQUIVO = "App.preferencias";
    private  int  MODE =0;
    private SharedPreferences.Editor editor;
    private final String EMAIL_USUARIO_LOGADO = "email_usuario_logado";
    private final String SENHA_USUARIO_LOGADO = "senha_usuario_logado";
    public Preferencias (Context contextoParametro){
        context = contextoParametro;
        preferencias = context.getSharedPreferences(NOME_ARQUIVO,MODE);
        editor = preferencias.edit();
    }

    public void salvarUsuarioPreferencia(String email, String senha){

        editor.putString(EMAIL_USUARIO_LOGADO,email);
        editor.putString(EMAIL_USUARIO_LOGADO, senha);
        editor.commit();

    }
    public String getEmail_Usuario_logado(){
        return preferencias.getString(EMAIL_USUARIO_LOGADO,null);
    }
    public String getSenha_usuario_logado(){
        return preferencias.getString(SENHA_USUARIO_LOGADO,null);
    }
}
