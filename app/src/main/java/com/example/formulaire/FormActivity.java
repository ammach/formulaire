package com.example.formulaire;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import dao.Personne;
import dao.PersonneDao;
import utils.ImageUtil;

public class FormActivity extends Activity {

	ImageView profil;
	EditText editNom;
	EditText editPrenom;
	EditText editAge;
	EditText editTravail;
	
	String nom="";
	String prenom="";
	String age="";
	String travail="";
	Uri uri=Uri.parse("null");
	
	PersonneDao personneDao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form);
		
		profil=(ImageView) findViewById(R.id.profil);
		editNom=(EditText) findViewById(R.id.editNom);
		editPrenom=(EditText) findViewById(R.id.editPrenom);
		editAge=(EditText) findViewById(R.id.editAge);
		editTravail=(EditText) findViewById(R.id.editTravail);
		
		
		
	}

	
	/* PICK IMAGE  */
	public void pickImage(View v) {
		Intent intent=new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, 1);
	}
	
	/* IMAGE PICKED*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==1) {
			if (resultCode==RESULT_OK) {
				this.uri=data.getData();
				profil.setImageURI(this.uri);
			}
		}
	}
	
	/*AJOUTER LA PERSONNE*/
	public void ajouter(View v) {
		nom=editNom.getText().toString();
	    prenom=editPrenom.getText().toString();
		age=editAge.getText().toString();
		travail=editTravail.getText().toString();
		if (nom.equals("") || prenom.equals("")|| editAge.getText().toString().equals("") || travail.equals("")) {
			utils.Message.show(this,"veuillez remplir vos données");
		} else {
			profil.setDrawingCacheEnabled(true);
			Bitmap bitmap=profil.getDrawingCache();
			byte[] imagebyte=ImageUtil.getBytes(bitmap);
			Personne personne=new Personne(nom,prenom,Integer.parseInt(age),travail,imagebyte);
			personneDao=new PersonneDao(this);
			personneDao.addPersonne(personne);
		}
	}
	
	/*VOIR LA LISTE DES PERSONNES*/
	public void voirListe(View v) {
		startActivity(new Intent(this,PersonnesActivity.class));
	}
	
	/* SAVE INSTANCES */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		nom=editNom.getText().toString();
	    prenom=editPrenom.getText().toString();
		travail=editTravail.getText().toString();
		String uri=this.uri.toString();
		
		outState.putString("nom", nom);
		outState.putString("prenom", prenom);
		outState.putString("age", editAge.getText().toString());
		outState.putString("travail", travail);
		outState.putString("uri", uri);
	}
	
	/*RESTORE INSTANCES*/
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		String nom=savedInstanceState.getString("nom");
	    String prenom=savedInstanceState.getString("prenom");
	    String age=savedInstanceState.getString("age");
	    String travail=savedInstanceState.getString("travail");
	    Uri usiSaved=Uri.parse(savedInstanceState.getString("uri"));
	    
	    editNom.setText(nom);
	    editPrenom.setText(prenom);
	    editAge.setText(age);
	    editTravail.setText(travail);
	    
	    if (!(this.uri.equals("null"))) {
			profil.setImageURI(usiSaved);
		}
	    else {
			profil.setImageResource(R.drawable.profil);
		}
	}
	
	
}
