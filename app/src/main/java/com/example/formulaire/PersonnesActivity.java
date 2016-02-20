package com.example.formulaire;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import java.util.ArrayList;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import dao.Personne;
import dao.PersonneDao;
import utils.ImageUtil;
import utils.Message;

@SuppressLint("NewApi")
public class PersonnesActivity extends ActionBarActivity implements OnItemLongClickListener {

	class MyAdapter extends BaseAdapter implements Filterable{

		CustomFiler customFilter;
		ArrayList<Personne> personnes;
		ArrayList<Personne>copyPersonnes;
		
		public MyAdapter(ArrayList<Personne> personnes) {
			this.personnes = personnes;
			this.copyPersonnes=personnes;
		}

		@Override
		public int getCount() {
			return personnes.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView==null) {
				LayoutInflater inflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView=inflater.inflate(R.layout.row, null);
			}
			TextView txt=(TextView) convertView.findViewById(R.id.txtRow);
			img=(ImageView) convertView.findViewById(R.id.imgRow);
			
			String nom=personnes.get(position).getNom();
			String prenom=personnes.get(position).getPrenom();
            byte[]imagebyte=personnes.get(position).getImage();
			Bitmap bitmap=ImageUtil.getImage(imagebyte);
			
			txt.setText(prenom+" "+nom);	
			img.setImageBitmap(bitmap);			
			return convertView;
		}

		@Override
		public Filter getFilter() {
			if (customFilter==null) {
				customFilter=new CustomFiler();
			}
			return customFilter;
		}
		
		class CustomFiler extends Filter{

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filters=new FilterResults();
				ArrayList<Personne> param=new ArrayList<Personne>();
				if (constraint!=null && constraint.length()>0) {
					for (int i = 0; i < copyPersonnes.size(); i++) {
						if (copyPersonnes.get(i).getNom().contains(constraint)) {
							param.add(copyPersonnes.get(i));
						}
					}
					filters.count=param.size();
					filters.values=param;
				}
				else {
					filters.count=copyPersonnes.size();
					filters.values=copyPersonnes;
				}
				return filters;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				personnes=(ArrayList<Personne>) results.values;
				notifyDataSetChanged();
			}
			
		}
		
	}
	
	
	ImageView img;
	ListView listview;
	android.widget.SearchView searchview;
	MyAdapter adapter;
	ArrayList<Personne> personnes;
	PersonneDao personneDao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personnes);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		listview=(ListView) findViewById(R.id.listview);
		searchview=(android.widget.SearchView) findViewById(R.id.search);
		
		personneDao=new PersonneDao(this);
        personnes=new ArrayList<Personne>();
        personnes=personneDao.listPersonnes();
		
		adapter=new MyAdapter(personnes);
		listview.setAdapter(adapter);
		searchview.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				adapter.getFilter().filter(newText);
				return false;
			}
		}); 
		
		listview.setOnItemLongClickListener(this);
		
	}
	

	
	/* LONG CLICK ON ITEM*/
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		final int pos=position;
		AlertDialog.Builder alert=new AlertDialog.Builder(this);
		alert.setMessage("Voulez vous vraiment supprimer cette personne?");
		alert.setPositiveButton("oui",new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				long id=personneDao.getIdByNomPrenom(personnes.get(pos).getNom(), personnes.get(pos).getPrenom());
				personneDao.deletePersonne(getApplicationContext(), id);
				personnes.remove(pos);
				adapter.notifyDataSetChanged();
			}
		});
		alert.setNegativeButton("non", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
		
			}
		});
		alert.show();
		return false;
	}

	
}
