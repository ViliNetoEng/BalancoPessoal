package br.com.vilialex.balancopessoal.ui;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import br.com.vilialex.R;
import br.com.vilialex.balancopessoal.io.db.BalancoPessoalDBUtil;
import br.com.vilialex.balancopessoal.util.BalancoPessoalUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;



public class BalancoPessoalLogin extends Activity {
	
	//RELATIVO AO SAVE PREFERENCES
	public static final String PREFS_NAME = "dadosUsuario";
	
	// Valores para email e senha serão armazenados nessas variáveis.
	private String mEmail;
	private String mPassword;
	private String NOME_BANCO = "BalancoPessoal";
	

	// -------------------ON CREATE-----------------------//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		// ----------------BANCO DE DADOS-----------------//
		// Comando para iniciar tabela de usuario
		String sql = "CREATE TABLE IF NOT EXISTS `usuario` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `email` VARCHAR( 50 ) NOT NULL , `senha` VARCHAR( 50 ) NOT NULL DEFAULT  '123456', `lembrete` VARCHAR( 100 ) NOT NULL ,UNIQUE (`email`));";
		BalancoPessoalDBUtil dbu = new BalancoPessoalDBUtil(this, NOME_BANCO, sql);
		dbu.fechaBanco();
		// --------------FIM BANCO DE DADOS--------------//

		// -------- Restora preferencias---------//
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String email = settings.getString("email", "");
		String senha = settings.getString("senha", "");
		Boolean ckbox = settings.getBoolean("ckbox", false);
		final EditText emailView = (EditText) findViewById(R.id.email);
		final EditText passView = (EditText) findViewById(R.id.password);
		final CheckBox ckboxView = (CheckBox) findViewById(R.id.ckbox);
		ckboxView.setChecked(ckbox);
		emailView.setText(email);
		passView.setText(senha);
		// -------FIM Restora preferencias-------//

	}

	// -------------------FIM ON CREATE--------------------//

	public void clicaSignin(View v) {
		try {
			
			checaValidaDados();//CHECA OS DADOS
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void checaValidaDados() throws NoSuchAlgorithmException, UnsupportedEncodingException {

		boolean cancel = false;
		View focusView = null;
		
		final EditText emailView = (EditText) findViewById(R.id.email);
		final EditText passView = (EditText) findViewById(R.id.password);
		final CheckBox checkbox = (CheckBox) findViewById(R.id.ckbox);
		
		mEmail = emailView.getText().toString();
		mPassword = passView.getText().toString();

		// Checa por um email válido //-------------EMAIL-------------//
		if (TextUtils.isEmpty(mEmail)) {
			emailView.setError(getString(R.string.error_field_required));
			focusView = emailView;
			cancel = true;

		} else if (!mEmail.contains("@")) {
			emailView.setError(getString(R.string.error_invalid_email));
			focusView = emailView;
			cancel = true;

		}// ------------------FIM VERIFICAÇÃO EMAIL-------------------//

		// Checa por senha válida//----------------SENHA--------------//
		if (TextUtils.isEmpty(mPassword)) {
			passView.setError(getString(R.string.error_field_required));
			focusView = passView;
			cancel = true;

		} else if (mPassword.length() < 4) {
			passView.setError(getString(R.string.error_invalid_password));
			focusView = passView;
			cancel = true;

		}// -------------------FIM VERIFICAÇÃO SENHA-------------------//

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {// --------------------VERIFICA USUARIO--------------------

			// SE USUARIO EXISTE PROCEDE PRA SENHA

			BalancoPessoalDBUtil dbu = new BalancoPessoalDBUtil(this, NOME_BANCO, "");
			String[] resposta =  dbu.consultaUsuario("usuario", new String[] {"email","senha","lembrete"}, "email=\""+emailView.getText().toString()+"\"", null);
			
			if(resposta != null){//EXISTE USUARIO
				String shaSenha;
				BalancoPessoalUtil u = new BalancoPessoalUtil();
				shaSenha = u.SHA1(passView.getText().toString());//CONVERTE SENHA SHA1
				if(resposta[1].equals(shaSenha)){//SE SENHA OK

					if (checkbox.isChecked()) {//-----GRAVA USUARIO E SENHA, OU NÃO
						
						// Nós precisamos de um obj Editor para fazer as mudanças nas preferencias
						// Todos objs pertencem à android.context.Context
						SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putString("email", emailView.getText().toString());
						editor.putString("senha", passView.getText().toString());
						editor.putBoolean("ckbox", true);
						
						// Commit edições!
						editor.commit();
					}
					else{
						SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putString("email", "");
						editor.putString("senha", "");
						editor.putBoolean("ckbox", false);
						
						// Commit edições!
						editor.commit();
					}//-----GRAVA USUARIO E SENHA, OU NÃO
					
					//---------------------CHAMA PROXIMA ACTIVITY-------------------------//
					Intent in = new Intent(this, BalancoPessoalTelaPrincipal.class);
					in.putExtra("email",emailView.getText().toString());
					startActivity(in);
					finish();
					//--------------------FIM, CHAMA PROXIMA ACTIVITY---------------------//
					
				}
				else{//--------------SE SENHA ESTÁ INCORRETA-------------//
					Toast.makeText(this, R.string.wrong_pass, Toast.LENGTH_SHORT).show();
				}
			}
			
			else{// SE NAO, GRAVA NOVO USUARIO COM NOVA SENHA
				//ENVIA DADOS PARA NOVA INTENT
				Intent in = new Intent(this, CadastraUsuario.class);
				in.putExtra("email",emailView.getText().toString());
				in.putExtra("senha",passView.getText().toString());
				startActivity(in);
			}
			dbu.fechaBanco();
			
			

		}// -----------------FINALIZA VERIFICAÇÃO DE USUARIO---------------
	}
	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present. //AUTO GENERATE
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getOrder() == 0){
			// SE APERTOU PARA RECEBER DICA DA SENHA
						final EditText emailView = (EditText) findViewById(R.id.email);
						BalancoPessoalDBUtil dbu = new BalancoPessoalDBUtil(this, NOME_BANCO, "");
						String[] resposta = dbu.consultaUsuario("usuario", new String[] {"email", "senha", "lembrete" }, "email=\""+ emailView.getText().toString() + "\"", null);
						if (resposta != null) {// EXISTE USUARIO
							Toast.makeText(this, resposta[2], Toast.LENGTH_SHORT).show();
						}
						else{
							Toast.makeText(this, R.string.user_not_found, Toast.LENGTH_SHORT).show();
						}
		}
		return true;
	}	
}
