package br.com.vilialex.balancopessoal.io.db;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BalancoPessoalDBUtil {
	
	protected SQLiteDatabase db;
	public static String NOME_BANCO = "BalancoPessoal";
	private Context context;
	
	public BalancoPessoalDBUtil (Context ctx){
		this.context = ctx;
	}
	
	public BalancoPessoalDBUtil (Context ctx, String nome_db, String sql){
		db = ctx.openOrCreateDatabase(nome_db, Context.MODE_PRIVATE, null);
		if(!sql.equals("")){//CASO VAZIO NÃO EXECUTA
			db.execSQL(sql);
		}
		/*
		db.execSQL("CREATE table if not exists teste (id integer primary key autoincrement not null, nome text, descricao text)");
		ContentValues values = new ContentValues();
		values.put("nome","Joka");
		values.put("descricao","JokaTeste");
		db.insert("teste", null, values);
		*/
	}
	/**Cria e abre o banco de dados com as tabelas
	 * moeda, categoria, subcategoria, carteira, transacao
	 * */
	public void criaAbreBanco(){
		String[] sql = new String[5];

		//TABELA MOEDA
		sql[0] = "CREATE TABLE IF NOT EXISTS [moeda] ([id_moeda] INTEGER NOT NULL ON CONFLICT ABORT PRIMARY KEY ON CONFLICT ABORT AUTOINCREMENT, [nome_moeda] VARCHAR(64) NOT NULL ON CONFLICT ABORT, [representacao] VARCHAR(10) NOT NULL);";
		//TABELA CATEGORIA
		sql[1] = "CREATE TABLE IF NOT EXISTS [categoria] ([id_cat] INTEGER NOT NULL ON CONFLICT ABORT PRIMARY KEY ON CONFLICT ABORT AUTOINCREMENT,[nome_cat] VARCHAR(64) NOT NULL ON CONFLICT ABORT, [desc_cat] VARCHAR(64) DEFAULT Nothing);";
		//TABELA SUBCATEGORIA
		sql[2] ="CREATE TABLE IF NOT EXISTS [subcategoria] ([id_sub] INTEGER NOT NULL ON CONFLICT ABORT PRIMARY KEY ON CONFLICT ABORT AUTOINCREMENT,[id_cat] INTEGER NOT NULL CONSTRAINT [id_cat] REFERENCES [categoria]([id_cat]) ON DELETE CASCADE, [nome_sub] VARCHAR(64) NOT NULL ON CONFLICT ABORT,  [desc_sub] VARCHAR(64) DEFAULT Nothing);";
		//TABELA CARTEIRA
		sql[3] = "CREATE TABLE IF NOT EXISTS [carteira] ([id_cart] INTEGER NOT NULL ON CONFLICT ABORT PRIMARY KEY ON CONFLICT ABORT AUTOINCREMENT, [id_user] INTEGER NOT NULL CONSTRAINT [id_cart] REFERENCES [usuario]([id]) ON DELETE CASCADE,[nome_cart] VARCHAR(64) NOT NULL ON CONFLICT ABORT,[desc_cart] VARCHAR(64) DEFAULT Nothing,[total] FLOAT(32) NOT NULL DEFAULT 0);";
		//TABELA TRANSACAO
		sql[4] = "CREATE TABLE IF NOT EXISTS [transacao] ([id_tran] INTEGER NOT NULL ON CONFLICT ABORT PRIMARY KEY ON CONFLICT ABORT AUTOINCREMENT,[id_user] INTEGER NOT NULL CONSTRAINT [id_user] REFERENCES [usuario]([id]) ON DELETE CASCADE,[id_sub] INTEGER NOT NULL CONSTRAINT [id_sub] REFERENCES [subcategoria]([id_sub]) ON DELETE SET DEFAULT DEFAULT 0,[id_cart] INTEGER NOT NULL CONSTRAINT [id_cart] REFERENCES [carteira]([id_cart]) ON DELETE CASCADE,[id_moeda] INTEGER NOT NULL CONSTRAINT [id_moeda] REFERENCES [moeda]([id_moeda]) ON DELETE SET DEFAULT DEFAULT 0,[tipo] INTEGER NOT NULL,[valor] FLOAT NOT NULL,[data] DATE NOT NULL,[compensado] INTEGER NOT NULL,[desc_tran] VARCHAR(64) DEFAULT Nothing);";
		
		//EXECUTA COMANDOS
		try {
			abreBanco(context, NOME_BANCO);
			executaSQL(sql);
			fechaBanco();
		}
		catch(Exception e){
			//CASO NAO FUNCIONE O COMMIT DO BANCO
		}
	}
	
	public void insereDados(ContentValues values, String nomeTabela){
		db.insert(nomeTabela, null, values);
	}
	
	public void executaSQL(String[] sql){
		
		int qtdeScripts = sql.length;
		
		db.beginTransaction();
		// Executa cada sql passado como parâmetro
		for (int i = 0; i < qtdeScripts; i++) {
			db.execSQL(sql[i]);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		
	}
	
	public String[] consultaUsuario(String table, String[] columns, String selection, String[] selectionArgs){
		Cursor c = db.query(table, columns, selection, selectionArgs, null, null, null);
		//SE ENCONTROU
		if(c.getCount()>0){
			c.moveToFirst();//VAI PARA O PRIMEIRO ELEMENTO DA QUERY ENCONTRADA
			//VAI TER QUE ADAPTAR PARA CADA ELEMENTO
			String s[] = new String[3];
			s[0] = c.getString(0);//email
			s[1] = c.getString(1);//senha
			s[2] = c.getString(2);//lembrete
			return s;
		}
		return null;
	}
	
	public String[] consultaTeste(String table, String[] columns, String selection, String[] selectionArgs){
		Cursor c = db.query(table, columns, selection, selectionArgs, null, null, null);
		//SE ENCONTROU
		String s[] = new String[c.getCount()];
		int i=0;
		c.moveToFirst();//VAI PARA O PRIMEIRO ELEMENTO DA QUERY ENCONTRADA
		if(c.getCount()>0){
			for (i = 0; i < c.getCount(); i++) {
				// VAI TER QUE ADAPTAR PARA CADA ELEMENTO
				s[i] = c.getString(0);// valor
				c.moveToNext();
			}
		}
		else{
			return null;
		}
		return s;
	}
	
	public void abreBanco(Context ctx, String nomeBanco){
		db = ctx.openOrCreateDatabase(nomeBanco, Context.MODE_PRIVATE, null);	
	}
	
	public void fechaBanco(){
		db.close();
	}
	

}
