package Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rlds.cloneolx.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Adpter.AdapterAnuncios;
import Helper.ConfiguracaoFirebase;
import Model.Anuncio;
import dmax.dialog.SpotsDialog;

public class TelaPrincipal extends AppCompatActivity {
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
    private Button buttonCategoria, buttonRegiao;
    private RecyclerView recyclerViewAnunciosPublicos;
    private AdapterAnuncios adapterAnuncios;
    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private DatabaseReference anunciosPublicosRef;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        inicializarComponentes();
        anunciosPublicosRef = ConfiguracaoFirebase.getDatabaseReference().child("anuncios");
        recyclerViewAnunciosPublicos.setLayoutManager( new LinearLayoutManager(this));
        recyclerViewAnunciosPublicos.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(listaAnuncios,this);
        recyclerViewAnunciosPublicos.setAdapter(adapterAnuncios);
        recuperarAnunciosPublicos();


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
    public  void inicializarComponentes(){
        buttonCategoria = findViewById(R.id.buttonCategoria);
        buttonRegiao = findViewById(R.id.buttonRegiao);
        recyclerViewAnunciosPublicos = findViewById(R.id.recyclerViewAnunciosPublicos);

    }
    public  void recuperarAnunciosPublicos(){
        alertDialog = new SpotsDialog.Builder().setContext(this)
                .setMessage("recuperando Anúncios")
                .setCancelable(false)
                .build();
        alertDialog.show();
        listaAnuncios.clear();
        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot estados:snapshot.getChildren()){
                    for (DataSnapshot categorias:estados.getChildren()){
                        for (DataSnapshot anuncios:categorias.getChildren()){
                            Anuncio anuncio = anuncios.getValue(Anuncio.class);
                            listaAnuncios.add(anuncio);



                        }
                    }

                }
                Collections.reverse(listaAnuncios);
                adapterAnuncios.notifyDataSetChanged();
                alertDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}