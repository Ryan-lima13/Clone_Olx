package Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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
import Helper.RecyclerItemClickListener;
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
    private String filtroEstado = "";
    private  String filtroCategoria = "";
    private  boolean filtrandoPorEstado = false;

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

        // filtrar por regiao
        buttonRegiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               AlertDialog.Builder dialogEstado = new AlertDialog.Builder(TelaPrincipal.this);
               dialogEstado.setTitle("Selecione o estado desejado");

                // configurar spinner
                View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                // configuar spinner estados
                Spinner spinnerEstado = viewSpinner.findViewById(R.id.spinnerFiltro);

                String[] estados = getResources().getStringArray(R.array.estados);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        TelaPrincipal.this, android.R.layout.simple_spinner_item,
                        estados

                );
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEstado.setAdapter(arrayAdapter);
                dialogEstado.setView(viewSpinner);
               dialogEstado.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                       filtroEstado = spinnerEstado.getSelectedItem().toString();
                       recuperarAnunciosPorEstado();
                       filtrandoPorEstado = true;


                   }
               });
               dialogEstado.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                   }
               });
               AlertDialog dialog = dialogEstado.create();
               dialog.show();


            }
        });

        // filtro por categoria
        buttonCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(filtrandoPorEstado == true){
                    AlertDialog.Builder dialogEstado = new AlertDialog.Builder(TelaPrincipal.this);
                    dialogEstado.setTitle("Selecione a categoria desejado");

                    // configurar spinner
                    View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                    // configuar spinner categorias
                    Spinner spinnerCategoria = viewSpinner.findViewById(R.id.spinnerFiltro);

                    String[] categoria = getResources().getStringArray(R.array.categoria);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            TelaPrincipal.this, android.R.layout.simple_spinner_item,
                            categoria

                    );
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategoria.setAdapter(arrayAdapter);
                    dialogEstado.setView(viewSpinner);
                    dialogEstado.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            filtroCategoria =spinnerCategoria.getSelectedItem().toString();
                            recuperarAnunciosPorCategoria();


                        }
                    });
                    dialogEstado.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog dialog = dialogEstado.create();
                    dialog.show();

                }else {
                    Toast.makeText(TelaPrincipal.this, "Escolha primeiro uma região", Toast.LENGTH_SHORT).show();
                }


            }
        });


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
    public  void recuperarAnunciosPorEstado(){
        // configurar nó por estado
        alertDialog = new SpotsDialog.Builder().setContext(this)
                .setMessage("recuperando Anúncios")
                .setCancelable(false)
                .build();
        alertDialog.show();

        anunciosPublicosRef = ConfiguracaoFirebase.getDatabaseReference().child("anuncios")
                .child(filtroEstado);

        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaAnuncios.clear();
                for (DataSnapshot categorias: snapshot.getChildren()){
                    for (DataSnapshot anuncios:categorias.getChildren()){
                        Anuncio anuncio = anuncios.getValue(Anuncio.class);
                        listaAnuncios.add(anuncio);



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
    public  void recuperarAnunciosPorCategoria(){
        // configurar nó por estado
        alertDialog = new SpotsDialog.Builder().setContext(this)
                .setMessage("recuperando Anúncios")
                .setCancelable(false)
                .build();
        alertDialog.show();

        anunciosPublicosRef = ConfiguracaoFirebase.getDatabaseReference().child("anuncios")
                .child(filtroEstado)
                .child(filtroCategoria);

        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaAnuncios.clear();
                for (DataSnapshot anuncios:snapshot.getChildren()){
                    Anuncio anuncio = anuncios.getValue(Anuncio.class);
                    listaAnuncios.add(anuncio);

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