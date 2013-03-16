package br.com.vilialex.balancopessoal.adapter;

import java.util.ArrayList;

import br.com.vilialex.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AdapterListView extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<ItemListView> itens;

    public AdapterListView(Context context, ArrayList<ItemListView> itens) {
        //Itens que preencheram o listview
        this.itens = itens;
        //responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Retorna a quantidade de itens
     *
     * @return
     */
    public int getCount() {
        return itens.size();
    }

    /**
     * Retorna o item de acordo com a posicao dele na tela.
     *
     * @param position
     * @return
     */
    public ItemListView getItem(int position) {
        return itens.get(position);
    }

    /**
     * Sem implementação
     *
     * @param position
     * @return
     */
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        //Pega o item de acordo com a posição.
        ItemListView item = itens.get(position);
        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.item_lista, null);

        //atravez do layout pego pelo LayoutInflater, pegamos cada id relacionado
        //ao item e definimos as informações.
        ((TextView) view.findViewById(R.id.txtGde)).setText(item.getTexto());
        ((ImageView) view.findViewById(R.id.imageView1)).setImageResource(item.getIconeRid());
        ((ProgressBar) view.findViewById(R.id.progressBar)).setProgress(item.getProgresso());

        return view;
    }
}


//--------------------------------------------COMO USAR ESSAS 2 CLASSES------------------------------------------------------//
/*package com.example.listviewteste;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class Main extends Activity implements OnItemClickListener {

	private ListView listView;
    private AdapterListView adapterListView;
    private ArrayList<ItemListView> itens;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Pega a referencia do ListView
        listView = (ListView) findViewById(R.id.list);
        //Define o Listener quando alguem clicar no item.
        listView.setOnItemClickListener(this);
        
        createListView();
		
	}
	
	private void createListView() {
        //Criamos nossa lista que preenchera o ListView
        itens = new ArrayList<ItemListView>();
        ItemListView item1 = new ItemListView("Vili", R.drawable.icon_64x64);
        ItemListView item2 = new ItemListView("Icone", R.drawable.ic_launcher);
        ItemListView item3 = new ItemListView("Google", R.drawable.icon_64x64);
        ItemListView item4 = new ItemListView("Icone de novo", R.drawable.ic_launcher);

        itens.add(item1);
        itens.add(item2);
        itens.add(item3);
        itens.add(item4);

        //Cria o adapter
        adapterListView = new AdapterListView(this, itens);

        //Define o Adapter
        listView.setAdapter(adapterListView);
        //Cor quando a lista é selecionada para rolagem.
        listView.setCacheColorHint(Color.TRANSPARENT);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        //Pega o item que foi selecionado.
        ItemListView item = adapterListView.getItem(arg2);
        //Demostração
        Toast.makeText(this, "Você Clicou em: " + item.getTexto(), Toast.LENGTH_LONG).show();
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	

}
*/

