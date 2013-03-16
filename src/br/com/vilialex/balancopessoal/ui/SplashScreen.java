package br.com.vilialex.balancopessoal.ui;

import br.com.vilialex.R;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;

public class SplashScreen extends Activity implements Runnable {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		Handler h = new Handler();
		h.postDelayed(this, 2000);

	}

	@Override
	public void run() {
		startActivity(new Intent(this,BalancoPessoalLogin.class));
		finish();
	}

}
