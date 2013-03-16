package br.com.vilialex.balancopessoal.ui;

import java.util.ArrayList;

import br.com.vilialex.R;
import br.com.vilialex.balancopessoal.adapter.AdapterListView;
import br.com.vilialex.balancopessoal.adapter.ItemListView;
import br.com.vilialex.balancopessoal.io.db.BalancoPessoalDBUtil;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;


public class BalancoPessoalTelaPrincipal extends Activity {

	
	
	private ListView listView;
    private AdapterListView adapterListView;
    private ArrayList<ItemListView> itens;
    private BalancoPessoalDBUtil databaseUtil = new BalancoPessoalDBUtil(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tela_principal);

		final TextView tv = (TextView)findViewById(R.id.txtEmailExtra);//EMAIL DE LOGIN DO USUÁRIO
		//---------------RECUPERA DADOS INTENT ANTERIOR-------------------//
		Bundle extra = getIntent().getExtras();
		tv.setText(extra.getString("email"));
		//---------------FIM RECUPERA DADOS INTENT ANTERIOR---------------//
		
		//-------------CRIA TABELAS SE CASO ELAS NÃO EXISTAM--------------//
		databaseUtil.criaAbreBanco();
		//-------------CRIA TABELAS SE CASO ELAS NÃO EXISTAM--------------//
		
		//CRIA TRANSAÇÕES TESTE=====================================================================================
		
		String[] sql = new String[2];
		//TABELA MOEDA
		sql[0] = "INSERT INTO [transacao] ([id_user],[id_sub],[id_cart],[id_moeda],[tipo],[valor],[data],[compensado],[desc_tran]) VALUES (1,1,1,1,1,120,'2013-02-10',0,'Nada');";
		sql[0] = "INSERT INTO [transacao] ([id_user],[id_sub],[id_cart],[id_moeda],[tipo],[valor],[data],[compensado],[desc_tran]) VALUES (1,1,1,1,1,114,'2013-02-10',0,'Nada');";
		
		databaseUtil.abreBanco(this, BalancoPessoalDBUtil.NOME_BANCO);
		databaseUtil.executaSQL(sql);
		databaseUtil.fechaBanco();

		//FIM CRIA TRANSAÇÕES TESTE=================================================================================
		
		
		//----------------------REFERENCIA LISTVIEW-----------------------//
		//Pega a referencia do ListView
        listView = (ListView) findViewById(R.id.list);
        createListView();
        //--------------------FIM REFERENCIA LISTVIEW---------------------//
	}

	
	
	
	//TODO: ANALISAR PARA ONDE ESSE CÓDIGO VAI, NÃO FICA LEGAL AQUI.
	private void createListView() {
        //Criamos nossa lista que preenchera o ListView
        itens = new ArrayList<ItemListView>();
        BalancoPessoalDBUtil dbu = new BalancoPessoalDBUtil(this, BalancoPessoalDBUtil.NOME_BANCO, "");
		String[] resposta =  dbu.consultaTeste("transacao", new String[] {"valor"}, null , null);

        int x = resposta.length-1;
		for (int i = 0; i < x; i++) {
			if (i == 0) {
				ItemListView item1 = new ItemListView(resposta[i],R.drawable.ic_launcher, 35);
				itens.add(item1);
			}else{
				ItemListView item1 = new ItemListView(resposta[i],R.drawable.maismenu, 89);
				itens.add(item1);
			}
        }
        
        /*ItemListView item1 = new ItemListView("Vili", R.drawable.icon_64x64);
        ItemListView item2 = new ItemListView("Icone", R.drawable.ic_launcher);
        ItemListView item3 = new ItemListView("Google", R.drawable.icon_64x64);
        ItemListView item4 = new ItemListView("Icone de novo", R.drawable.ic_launcher);

        itens.add(item1);
        itens.add(item2);
        itens.add(item3);
        itens.add(item4);*/

        //Cria o adapter
        adapterListView = new AdapterListView(this, itens);

        //Define o Adapter
        listView.setAdapter(adapterListView);
        //Cor quando a lista é selecionada para rolagem.
        listView.setCacheColorHint(Color.TRANSPARENT);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {//--------------MENU ----------------//
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tela_principal, menu);
		return true;
	}
}
