package Model;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import Helper.ConfiguracaoFirebase;

public class Anuncio {
    private String idAnuncios;
    private String estado;
    private  String categoria;
    private String titulo;
    private  String valor;
    private  String telefone;
    private String descricao;

    private List<String> fotos;

    public Anuncio() {
        DatabaseReference anuncioRef = ConfiguracaoFirebase.getDatabaseReference()
                .child("meus_anuncios");
        setIdAnuncios(anuncioRef.push().getKey());
    }
    public  void salvar(){
        String idUsuario = ConfiguracaoFirebase.getIdUsuario();

        DatabaseReference anuncioRef = ConfiguracaoFirebase.getDatabaseReference()
                .child("meus_anuncios");
    anuncioRef.child(idUsuario)
            .child(getIdAnuncios())
            .setValue(this);
    salvarAnuncioPublico();
    }
    public  void salvarAnuncioPublico(){

        DatabaseReference anuncioRef = ConfiguracaoFirebase.getDatabaseReference()
                .child("meus_anuncios");
        anuncioRef.child(getEstado())
                .child(getCategoria())
                .child(getIdAnuncios())
                .setValue(this);
    }
    public  void removerAnuncios(){
        String idUsuario = ConfiguracaoFirebase.getIdUsuario();

        DatabaseReference anuncioRef = ConfiguracaoFirebase.getDatabaseReference()
                .child("meus_anuncios")
                .child(idUsuario)
                .child(getIdAnuncios());
        anuncioRef.removeValue();
        removerAnunciosPublicos();

    }
    public  void removerAnunciosPublicos(){
        DatabaseReference anuncioRef = ConfiguracaoFirebase.getDatabaseReference()
                .child("anuncios")
                .child(getEstado())
                .child(getCategoria())
                .child(getIdAnuncios());
        anuncioRef.removeValue();


    }

    public String getIdAnuncios() {
        return idAnuncios;
    }

    public void setIdAnuncios(String idAnuncios) {
        this.idAnuncios = idAnuncios;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }
}
