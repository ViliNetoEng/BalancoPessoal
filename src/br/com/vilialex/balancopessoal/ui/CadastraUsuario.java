package br.com.vilialex.balancopessoal.ui;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import br.com.vilialex.R;
import br.com.vilialex.balancopessoal.io.db.BalancoPessoalDBUtil;
import br.com.vilialex.balancopessoal.util.BalancoPessoalUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.ContentValues;


public class CadastraUsuario extends Activity{
	
	private BalancoPessoalDBUtil databaseUtil = new BalancoPessoalDBUtil(this);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadastra_usuario);
		
		//Referencia do cadastra_usuario.xml
		final EditText emailCadastraView = (EditText) findViewById(R.id.emailCadastro);
		final EditText passCadastraView = (EditText) findViewById(R.id.passwordCadastro);
		
		//PEGA DADOS DE INTENT ANTERIOR
		Bundle extras = getIntent().getExtras();
		String email = extras.getString("email");
		String senha = extras.getString("senha");
		emailCadastraView.setText(email);
		passCadastraView.setText(senha);
		
		
	}

	//Botão na UI
	public void registerOk(View v) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		final EditText emailCadastraView = (EditText) findViewById(R.id.emailCadastro);
		final EditText passCadastraView = (EditText) findViewById(R.id.passwordCadastro);
		final EditText passRepeteView = (EditText) findViewById(R.id.repetePass);
		final EditText lembreteView = (EditText) findViewById(R.id.lembreteCadastro);
		
		if(passRepeteView.getText().toString().equals(passCadastraView.getText().toString())){//--------SE SENHAS CONFEREM-------//
			// Checa por senha válida//----------------SENHA--------------//
			if (passRepeteView.getText().toString().length() < 4) {
				passRepeteView.setError(getString(R.string.error_invalid_password));

			}// -------------------FIM VERIFICAÇÃO SENHA-------------------//
			else {
				
				databaseUtil.abreBanco(this, BalancoPessoalDBUtil.NOME_BANCO);
				String shaSenhaString = new BalancoPessoalUtil().SHA1(passCadastraView.getText().toString());// CONVERTE SENHA PARA SHA1
				ContentValues cv = new ContentValues();
				cv.put("email", emailCadastraView.getText().toString());
				cv.put("senha", shaSenhaString);
				cv.put("lembrete", lembreteView.getText().toString());
				databaseUtil.insereDados(cv, "usuario");
				databaseUtil.fechaBanco();
				finish();
			}
		}
		
		else{//CASO SENHAS NÃO CONFIRAM
			Toast.makeText(this, R.string.wrong_pass, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}
	
}
