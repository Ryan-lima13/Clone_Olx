package Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.rlds.cloneolx.R;
import com.santalu.maskara.widget.MaskEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CadastarAnuncios extends AppCompatActivity implements View.OnClickListener {
    private EditText campoTitulo, campoDescricao;
    private CurrencyEditText campoValor;
    private MaskEditText campoTelefone;
    private ImageView imageView1, imageView2, imageView3;
    private List<String> listaFotosRecuperada = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastar_anuncios);
        getSupportActionBar().hide();
        inicializarComponentes();


    }
    public  void inicializarComponentes(){
        campoTitulo = findViewById(R.id.editTitulo);
        campoDescricao = findViewById(R.id.editTDescricao);
        campoValor = findViewById(R.id.editValor);
        campoTelefone = findViewById(R.id.editTelefone);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);

        Locale locale =new Locale("pt","BR");
        campoValor.setLocale(locale);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.imageView1){
            escolherImagem(1);

        }else if(view.getId() == R.id.imageView2){
            escolherImagem(2);

        }else if(view.getId() == R.id.imageView3){
            escolherImagem(3);

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            // recuperar imagem
            Uri imagemSelecionada = data.getData();
            String caminhoImagem = imagemSelecionada.toString();

            // configurar imagem no imageView
            if(requestCode == 1){

                imageView1.setImageURI(imagemSelecionada);


            }else if(requestCode == 2){

                imageView2.setImageURI(imagemSelecionada);

            }else if(requestCode ==3){

                imageView3.setImageURI(imagemSelecionada);

            }
            listaFotosRecuperada.add(caminhoImagem);


        }
    }

    public  void escolherImagem(int requestCode){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,requestCode);



    }
}