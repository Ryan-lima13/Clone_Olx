package Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.rlds.cloneolx.R;

import Helper.ConfiguracaoFirebase;

public class TelaPrincipal extends AppCompatActivity {
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.menu_meus_anuncios){
            Intent intent = new Intent(TelaPrincipal.this, MeusAnincios.class);
            startActivity(intent);

        }else if(item.getItemId() == R.id.menu_meus_sair){
            deslogarUsuario();

        }
        return super.onOptionsItemSelected(item);
    }

    private  void deslogarUsuario(){
        autenticacao.signOut();
        Intent intent = new Intent(TelaPrincipal.this, MainActivity.class);
        startActivity(intent);
    }


}