package br.com.vilialex.balancopessoal.adapter;

public class ItemListView {

    private String texto;
    private int iconeRid;
    private int progresso;

    public ItemListView() {
    }

    public ItemListView(String texto, int iconeRid, int progresso) {
        this.texto = texto;
        this.iconeRid = iconeRid;
        this.progresso = progresso;
    }

    public int getIconeRid() {
        return iconeRid;
    }

    public void setIconeRid(int iconeRid) {
        this.iconeRid = iconeRid;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public int getProgresso() {
        return progresso;
    }

    public void setProgresso(int progresso) {
        this.progresso = progresso;
    }
    
}