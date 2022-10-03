package Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rlds.cloneolx.R;
import com.santalu.maskara.widget.MaskEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Helper.ConfiguracaoFirebase;
import Model.Anuncio;
import dmax.dialog.SpotsDialog;

public class CadastarAnuncios extends AppCompatActivity implements View.OnClickListener {
    private EditText campoTitulo, campoDescricao;
    private CurrencyEditText campoValor;
    private MaskEditText campoTelefone;
    private Button buttonCadastrar_anuncios;
    private Spinner campoEstado,campoCategoria;
    private ImageView imageView1, imageView2, imageView3;
    private List<String> listaFotosRecuperada = new ArrayList<>();
    private List<String> listaUrlFotos = new ArrayList<>();
    private StorageReference storage;
    private Anuncio anuncio;
    private AlertDialog alertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastar_anuncios);
        getSupportActionBar().hide();
        inicializarComponentes();
        carregarDadosSpinner();

        storage = ConfiguracaoFirebase.getStorageReference();

        buttonCadastrar_anuncios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String estado = campoEstado.getSelectedItem().toString();
                String categoria = campoCategoria.getSelectedItem().toString();
                String titulo = campoTitulo.getText().toString();
                String valor = String.valueOf(campoValor.getRawValue());
                String telefone = campoTelefone.getText().toString();
                String descricao = campoDescricao.getText().toString();

                if(listaFotosRecuperada.size()!= 0 ){
                    if(!estado.isEmpty()){
                        if(!categoria.isEmpty()){
                            if(!titulo.isEmpty()){
                                if(!valor.isEmpty() && !valor.equals("0")){
                                    if(!telefone.isEmpty()){
                                        if (!descricao.isEmpty()){
                                            salvarAnuncios();

                                        }else {
                                            Toast.makeText(CadastarAnuncios.this, "Preencha a descrição!",Toast.LENGTH_SHORT).show();
                                        }

                                    }else {
                                        Toast.makeText(CadastarAnuncios.this, "Preencha o telefone!",Toast.LENGTH_SHORT).show();
                                    }

                                }else {
                                    Toast.makeText(CadastarAnuncios.this, "Preencha o valor!",Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Toast.makeText(CadastarAnuncios.this, "Escolha um titulo!",Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(CadastarAnuncios.this, "Escolha uma categoria!",Toast.LENGTH_SHORT).show();

                        }

                    }else {
                        Toast.makeText(CadastarAnuncios.this, "Escolha um estado",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(CadastarAnuncios.this, "Escolha uma foto!",Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
    public  void salvarAnuncios(){
        alertDialog = new SpotsDialog.Builder().setContext(this)
                        .setMessage("Salvando Anúncios")
                                .setCancelable(false)
                                        .build();
        alertDialog.show();

        configurarAnucio();
        // salvar imagem no storage
        for (int i =0; i < listaFotosRecuperada.size(); i++ ){
            String urlImagem = listaFotosRecuperada.get(i);
            int tamanhoLista = listaFotosRecuperada.size();
            salvarFotoStorage(urlImagem,tamanhoLista,i);
        }




    }
    private  void salvarFotoStorage(String url, int totalFotos,int contador){
        // criar nó storage
        StorageReference imagemAnuncio = storage.child("imagens")
                .child("anuncios")
                .child(anuncio.getIdAnuncios())
                .child("imagem" + contador);
        // fazer upload do arquivo
        UploadTask uploadTask = imagemAnuncio.putFile(Uri.parse(url));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagemAnuncio.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri url = task.getResult();
                        String urlConvertida = url.toString();
                        listaUrlFotos.add(urlConvertida);
                        if(totalFotos == listaUrlFotos.size()){
                            anuncio.setFotos(listaUrlFotos);
                            anuncio.salvar();
                            alertDialog.dismiss();
                            finish();
                        }


                    }
                });


            }
        });


    }
    private  void configurarAnucio(){
        String estado = campoEstado.getSelectedItem().toString();
        String categoria = campoCategoria.getSelectedItem().toString();
        String titulo = campoTitulo.getText().toString();
        String valor = String.valueOf(campoValor.getRawValue());
        String telefone = campoTelefone.getText().toString();
        String descricao = campoDescricao.getText().toString();
        anuncio = new Anuncio();
        anuncio.setEstado(estado);
        anuncio.setCategoria(categoria);
        anuncio.setTitulo(titulo);
        anuncio.setValor(valor);
        anuncio.setTelefone(telefone);
        anuncio.setDescricao(descricao);
    }
    private void carregarDadosSpinner(){

        String[] estados = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                estados

        );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoEstado.setAdapter(arrayAdapter);

        String[] categorias = getResources().getStringArray(R.array.categoria);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                categorias

        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoCategoria.setAdapter(adapter);





    }
    public  void inicializarComponentes(){
        campoTitulo = findViewById(R.id.editTitulo);
        campoDescricao = findViewById(R.id.editTDescricao);
        campoValor = findViewById(R.id.editValor);
        campoTelefone = findViewById(R.id.editTelefone);
        buttonCadastrar_anuncios = findViewById(R.id.bt_cadsatrar_anuncio);
        campoCategoria = findViewById(R.id.spinner_categoria);
        campoEstado = findViewById(R.id.spinner_estados);
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