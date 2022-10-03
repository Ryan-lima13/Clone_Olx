package Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rlds.cloneolx.R;
import com.rlds.cloneolx.databinding.ActivityMeusAninciosBinding;

import java.util.ArrayList;
import java.util.List;

import Model.Anuncio;

public class MeusAnincios extends AppCompatActivity {
    private ActivityMeusAninciosBinding binding;
    private RecyclerView  recyclerViewAnuncios;
    private List<Anuncio> anuncios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMeusAninciosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        recyclerViewAnuncios = findViewById(R.id.recyclerViewAnuncios);
        recyclerViewAnuncios.setLayoutManager( new LinearLayoutManager(this));
        recyclerViewAnuncios.setHasFixedSize(true);
        //recyclerViewAnuncios.setAdapter();


        binding.fabCriarAnuncios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MeusAnincios.this, CadastarAnuncios.class);
                startActivity(intent);

            }
        });
    }
}