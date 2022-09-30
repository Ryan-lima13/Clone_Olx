package Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rlds.cloneolx.databinding.ActivityMainBinding;

import Helper.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.editEmail.getText().toString();
                String senha = binding.editSenha.getText().toString();
                if( email.isEmpty()|| senha.isEmpty()){
                    Toast.makeText(MainActivity.this, "Preencha todos os campos!",Toast.LENGTH_SHORT).show();

                }else {
                    // verificar estado do switch
                    if(binding.switchAcessar.isChecked()){
                        // cadastar usuario
                        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
                        autenticacao.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if( task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Usuario cadastrado com sucesso!",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(MainActivity.this, "Erro ao  cadastrar usuario!",Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

                    }else {
                        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
                        autenticacao.signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(MainActivity.this, TelaPrincipal.class);
                                    startActivity(intent);
                                    Toast.makeText(MainActivity.this, "usuario logado",Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(MainActivity.this, "Erro ao fazer login!",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                    }

                }
            }
        });


    }
}