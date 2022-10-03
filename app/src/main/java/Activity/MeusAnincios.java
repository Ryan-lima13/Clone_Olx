package Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rlds.cloneolx.R;
import com.rlds.cloneolx.databinding.ActivityMeusAninciosBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import Adpter.AdapterAnuncios;
import Helper.ConfiguracaoFirebase;
import Helper.RecyclerItemClickListener;
import Model.Anuncio;
import dmax.dialog.SpotsDialog;

public class MeusAnincios extends AppCompatActivity {
    private ActivityMeusAninciosBinding binding;
    private RecyclerView  recyclerViewAnuncios;
    private List<Anuncio> anuncios = new ArrayList<>();
    private AdapterAnuncios adapterAnuncios;
    private DatabaseReference anucioUuarioRef;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMeusAninciosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        anucioUuarioRef = ConfiguracaoFirebase.getDatabaseReference().child("meus_anuncios").child(ConfiguracaoFirebase.getIdUsuario());
        recyclerViewAnuncios = findViewById(R.id.recyclerViewAnuncios);
        recyclerViewAnuncios.setLayoutManager( new LinearLayoutManager(this));
        recyclerViewAnuncios.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(anuncios,this);
        recyclerViewAnuncios.setAdapter(adapterAnuncios);
        recuperarAnuncios();
        // adicionar evento de clique recyclerView
        recyclerViewAnuncios.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerViewAnuncios,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                Anuncio anuncioSelecionado = anuncios.get(position);
                                anuncioSelecionado.removerAnuncios();
                                adapterAnuncios.notifyDataSetChanged();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                            }
                        }
                )
        );



        binding.fabCriarAnuncios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MeusAnincios.this, CadastarAnuncios.class);
                startActivity(intent);

            }
        });
    }
    public  void recuperarAnuncios(){
        alertDialog = new SpotsDialog.Builder().setContext(this)
                .setMessage("recuperando Anúncios")
                .setCancelable(false)
                .build();
        alertDialog.show();
        anucioUuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                anuncios.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    anuncios.add(ds.getValue(Anuncio.class));


                }
                Collections.reverse(anuncios);
                adapterAnuncios.notifyDataSetChanged();
                alertDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}