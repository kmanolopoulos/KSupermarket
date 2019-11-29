package com.kmanolopoulos.KSupermarket;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		DisplayMessage();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void DisplayMessage()
	{
		String message = "";
		String versionName;
		
		try 
		{
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			message = getResources().getString(R.string.app_name) + " v" + versionName + 
					  "\n" + getResources().getString(R.string.about_text);
		} 
		catch (NameNotFoundException e) 
		{
			e.printStackTrace();
			message = getResources().getString(R.string.app_name) + 
					  "\n" + getResources().getString(R.string.about_text);
		}
		
		((TextView)findViewById(R.id.CaptionText)).setText(message);
	}
	
	public void ExitChoice(View v) 
	{
	    finish();
	}
}
