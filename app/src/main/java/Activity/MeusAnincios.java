package Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rlds.cloneolx.R;
import com.rlds.cloneolx.databinding.ActivityMeusAninciosBinding;

public class MeusAnincios extends AppCompatActivity {
    private ActivityMeusAninciosBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMeusAninciosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.fabCriarAnuncios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MeusAnincios.this, CadastarAnuncios.class);
                startActivity(intent);

            }
        });
    }
}