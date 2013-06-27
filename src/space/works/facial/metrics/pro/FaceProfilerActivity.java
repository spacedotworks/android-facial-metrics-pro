package space.works.facial.metrics.pro;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;



public class FaceProfilerActivity extends Activity  {
    /** Called when the activity is first created. */

	

	//declare global variables
	private static final int SELECT_PHOTO = 100; //100 is a unique identifier
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 200;

	
	public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
           NetworkInfo[] info = connectivity.getAllNetworkInfo();
           if (info != null) {
              for (int i = 0; i < info.length; i++) {
                 if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                 }
              }
           }
        }
        return false;
	}

		
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
//        new AlertDialog.Builder(FaceProfilerActivity.this)  
//  	  .setTitle( "An apology from the developers" )
//        .setMessage( "Thank you for your patience thus far. We have finally recovered some functionality for the app" +
//        		", although some functions are still wonky. We are currently rebuilding the celebrity face match database" +
//        		" as well. Do bear with us for a little while more. This message will go away once the final fix is in place." )
//      
//        .setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                Log.d( "AlertDialog", "Negative" );
//            }
//        } )
//        .show();
     
      
      }
        
    protected void onResume(){
    	super.onResume();       
    	
    	
    	//CELEB BUTTON
        Button celeb = (Button) findViewById(R.id.celeb);
        celeb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
				//Toast.makeText (getApplicationContext(),"This is a feature for the paid app only!" , Toast. LENGTH_LONG).show();
				
			 	showDialog(1);
				ImageView image = (ImageView) findViewById(R.id.celebi);
				image.setVisibility(View.VISIBLE);
			}
		});	
        
        //WEB IMAGE BUTTON
        ImageView call = (ImageView) findViewById(R.id.call);
        call.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
				if (!isNetworkAvailable(getBaseContext())) {
					Toast t = Toast.makeText(getApplicationContext(),"No network connectivity!",
				                 Toast.LENGTH_LONG);
					t.setGravity(Gravity.CENTER,0,0);
					t.getView().setBackgroundColor(Color.RED);
					t.show();				
				 	}    
				else{
				EditText pp = (EditText) findViewById(R.id.mUrl);			
				String weburl = pp.getText().toString();
				if (weburl.matches("^[Hh][tT][Tt][Pp].*")){
					
				}
				else {String weburl2 = "http://" + weburl;
				weburl = weburl2;}
				
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
             	imm.hideSoftInputFromWindow(pp.getWindowToken(), 0);
				if (weburl.equals("")){
					
					Toast.makeText (getApplicationContext(),"Please enter a valid URL!" , Toast. LENGTH_LONG).show();	
				}
				
				else{
	          	
				justdoit("1", weburl);
				}
			}}
		});	
        
        //CAMERA BUTTON //////////////////////////////////////////
        ImageView cam = (ImageView) findViewById(R.id.cam);
        cam.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {					
			
			Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    	File imagesFolder = new File(Environment.getExternalStorageDirectory(), "FacialMetrics");
	    	imagesFolder.mkdirs(); // <----
	    	String fileName = new SimpleDateFormat("yyyyMMddhhmm'.jpg'").format(new Date());
	    	File takenimage = new File(imagesFolder, fileName);
	    	Uri uriSavedImage = Uri.fromFile(takenimage);
	    	String fullpath = takenimage.getAbsolutePath();
	    	imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
	    	startActivityForResult(imageIntent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	    	//set path to screen
	    	EditText filepath = (EditText) findViewById(R.id.filepath);	
	    	filepath.setText(fullpath);			      
			}
		});	

        //BROWSE BUTTON/////////////////////////////////////////////
        ImageView browse = (ImageView) findViewById(R.id.browse);
        browse.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
				
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, SELECT_PHOTO);  
				
			}
		});	
        
        //UPLOAD BUTTON/////////////////////////////////////////////
        ImageView upload = (ImageView) findViewById(R.id.upload);
        upload.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
				
				
				
				if (!isNetworkAvailable(getBaseContext())) {
					Toast t = Toast.makeText(getApplicationContext(),"No network connectivity!",
			                 Toast.LENGTH_LONG);
				t.setGravity(Gravity.CENTER,0,0);
				t.getView().setBackgroundColor(Color.RED);
				t.show();		
				 	}    
				else{
				
				 EditText filepath = (EditText) findViewById(R.id.filepath);	
				 String mPath = filepath.getText().toString();
				 
				 if (mPath.equals("")){
					 
						Toast.makeText (getApplicationContext(),"Please select an image or take a photo!" , Toast. LENGTH_LONG).show();	
					}
				 else {
		          	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	             	imm.hideSoftInputFromWindow(filepath.getWindowToken(), 0);
				  
//				 Toast.makeText (getApplicationContext(),"Matched" , Toast. LENGTH_LONG).show();
				Bitmap rm = decodeB(mPath);		//run decoder		
//				int num = rm.getHeight() / 350;	
				
				Bitmap smallie;
				if (rm.getHeight() > 400 ) {
					
					double he = (double) rm.getHeight() - 400;
					double ew = (double) he / rm.getHeight();
					double we = (double) ew * rm.getWidth();
					
					int h = (int)he;
					int w = (int)we;
										
					
					Bitmap scaled = Bitmap.createScaledBitmap(rm,rm.getWidth()-w,
							rm.getHeight()-h,true);
					smallie = scaled;
				}
				else {smallie = rm;}
				//write bitmap to file
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				smallie.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
				//you can create a new file name "test.jpg" in sdcard folder.
				File f = new File(Environment.getExternalStorageDirectory() + File.separator +
							"FacialMetrics" + File.separator + "test.jpg");
//				Toast.makeText(getApplicationContext(), f.toString(),
//			            Toast.LENGTH_LONG).show();
				try {
					f.createNewFile();
					
					FileOutputStream fo = new FileOutputStream(f);
					fo.write(bytes.toByteArray());
					String newpath = f.getAbsolutePath();	
					
					justdoit("0", newpath);
				} 			
				catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
				Toast.makeText(getApplicationContext(),"I/O exception",
			            Toast.LENGTH_LONG).show();
				}			
				}}
			}	
			
			
			
			
        });	    
        
        //SAVE BUTTON
        ImageView save = (ImageView) findViewById(R.id.save);
        save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
			
				ScrollView z = (ScrollView) findViewById(R.id.scroll);
				z.setVerticalFadingEdgeEnabled(false);
			    z.setFadingEdgeLength(0);
				//LinearLayout skele = (LinearLayout) findViewById(R.id.skele);
				int totalHeight = z.getChildAt(0).getHeight();
				int totalWidth = z.getChildAt(0).getWidth();
							
	            View u = findViewById(R.id.scroll);
	            DisplayMetrics displayMetrics = FaceProfilerActivity.this.getResources().getDisplayMetrics();
	            int pad = (int)((40 * displayMetrics.density) + 0.5);
	            
	            
	            Bitmap b = null;
	            
	            try{   
	            b = Bitmap.createBitmap(totalWidth+pad,totalHeight+pad,Bitmap.Config.ARGB_8888);
	            Canvas canvas = new Canvas(b);
	            u.draw(canvas);
	            }
	            catch(Exception e){
            	
            	Toast.makeText (getApplicationContext(),"Image too large!" , Toast. LENGTH_LONG).show();
            	return;
	            }
	            


                //Save bitmap
                String extr = Environment.getExternalStorageDirectory().toString() + File.separator + "FacialMetrics";
                String fileName = new SimpleDateFormat("yyyyMMddhhmm'_report.jpg'").format(new Date());
                File myPath = new File(extr, fileName);
                FileOutputStream fos = null;
                
                try {
                    fos = new FileOutputStream(myPath);
                    b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    MediaStore.Images.Media.insertImage(getContentResolver(), b, "Screen", "screen");
                    Toast.makeText (getApplicationContext(),"Result saved at: " + myPath , Toast. LENGTH_LONG).show();
                }catch (FileNotFoundException e) {
                	Toast.makeText (getApplicationContext(),"Unable to save" , Toast. LENGTH_LONG).show();// TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                	Toast.makeText (getApplicationContext(),"Unable to save" , Toast. LENGTH_LONG).show();
                    e.printStackTrace();
                }
                
                               //onDestroy();
			}
		});	
    
	
    
    //SHARE BUTTON
    ImageView share = (ImageView) findViewById(R.id.share);
    share.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {	
			
			ScrollView z = (ScrollView) findViewById(R.id.scroll);
			z.setVerticalFadingEdgeEnabled(false);
		    z.setFadingEdgeLength(0);
			//LinearLayout skele = (LinearLayout) findViewById(R.id.skele);
			int totalHeight = z.getChildAt(0).getHeight();
			int totalWidth = z.getChildAt(0).getWidth();
						
            View u = findViewById(R.id.scroll);
            u.setDrawingCacheEnabled(true);                                                
            
            //calculating 40dp in pixels to account for padding
            DisplayMetrics displayMetrics = FaceProfilerActivity.this.getResources().getDisplayMetrics();
            int pad = (int)((40 * displayMetrics.density) + 0.5);
            //int totalHeight = z.getChildAt(0).getHeight();
            
            Bitmap b = null;
            try{   
            b = Bitmap.createBitmap(totalWidth+pad,totalHeight+pad,Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(b);
            u.draw(canvas);
            }
            catch(Exception e){
        	
        	Toast.makeText (getApplicationContext(),"Image too large!" , Toast. LENGTH_LONG).show();
        	return;
            }
            
            
           
            //Save bitmap
            String extr = Environment.getExternalStorageDirectory().toString() + File.separator + "FacialMetrics";
            //String fileName = new SimpleDateFormat("yyyyMMddhhmm'_report.jpg'").format(new Date());
            String fileName = "share_temp.jpg";
            File myPath = new File(extr, fileName);
            FileOutputStream fos = null;
            
            try {
                fos = new FileOutputStream(myPath);
                b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                MediaStore.Images.Media.insertImage(getContentResolver(), b, "Screen", "screen");
              //  Toast.makeText (getApplicationContext(),"Result saved at: " + myPath , Toast. LENGTH_LONG).show();
            }catch (FileNotFoundException e) {
            	Toast.makeText (getApplicationContext(),"Unable to save" , Toast. LENGTH_LONG).show();// TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
            	Toast.makeText (getApplicationContext(),"Unable to save" , Toast. LENGTH_LONG).show();
                e.printStackTrace();
            }
            
            //Toast.makeText (getApplicationContext(),"file://" + extr + "FacialMetrics" + File.separator + fileName , Toast. LENGTH_LONG).show();
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + extr  + File.separator + fileName));
           
            startActivity(Intent.createChooser(share, "Share Image"));
            //onDestroy();
		}
	});	
}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
               
        switch(requestCode) { 
        case SELECT_PHOTO:
            if(resultCode == RESULT_OK){  
                Uri selectedImage = imageReturnedIntent.getData();
//              String path = selectedImage.toString();
                String path = getRealPathFromURI(selectedImage);
              
                EditText filepath = (EditText) findViewById(R.id.filepath);
                filepath.setText(path);   
            }
        case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
            if (resultCode ==  RESULT_OK){            
            }
        
            
        }
    }
	
    public void toaster(String s){
    	Toast.makeText (getApplicationContext(), s , Toast. LENGTH_LONG).show();
    }
    
    public void toaster(int s){
    	Toast.makeText (getApplicationContext(), s , Toast. LENGTH_LONG).show();
    }
    
    public void justdoit(String mode, String path){
    	
		String iUrl = "http://api.skybiometry.com/fc/faces/recognize.json";	
		new PostPost().execute(iUrl,path,mode);
	
    }
	
//    public Bitmap decodeB(String uri){
//	//	String fullp = getRealPathFromURI(gUri);
//		ExifInterface exif;
//		Bitmap rm = null;
//		try {
//			exif = new ExifInterface(uri);
//			String exifOrientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);			//getOrientation		 
//			BufferedInputStream imageStream = new BufferedInputStream(new FileInputStream(uri));
//			//Toast.makeText (getApplicationContext(),exifOrientation , Toast. LENGTH_LONG).show();
//			//InputStream imageStream = getContentResolver().openInputStream(gUri);
//			Bitmap ym = BitmapFactory.decodeStream(imageStream);
//			//Bitmap rm;
//			if (exifOrientation.equals("6")){
//				Matrix matrix = new Matrix();
//				matrix.postRotate(90);
//				rm = Bitmap.createBitmap(ym, 0, 0, ym.getWidth(), ym.getHeight(), matrix, true);		
//			}
//			else if (exifOrientation.equals("8")){
//				Matrix matrix = new Matrix();
//				matrix.postRotate(270);
//				rm = Bitmap.createBitmap(ym, 0, 0, ym.getWidth(), ym.getHeight(), matrix, true);
//			}
//			else{
//				rm = ym;
//				}			
//			return rm;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			Toast.makeText(getApplicationContext(),"File Not Found!",
//					Toast.LENGTH_LONG).show();
//			onResume();
//			e.printStackTrace();
//			return rm;
//		}								
//    }
//       
    
    public Bitmap decodeB(String uri){
	//	String fullp = getRealPathFromURI(gUri);
		
		Bitmap rm = null;
		try {
			 //File f = new File(SD_CARD_IMAGE_PATH);
		        ExifInterface exif = new ExifInterface(uri);
		        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

		        int angle = 0;

		        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
		            angle = 90;
		        } 
		        else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
		            angle = 180;
		        } 
		        else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
		            angle = 270;
		        }

		        Matrix mat = new Matrix();
		        mat.postRotate(angle);

		        Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(uri), null, null);
		        Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);         		
		        rm = correctBmp;
		        return rm;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(),"File Not Found!",
					Toast.LENGTH_LONG).show();
			onResume();
			e.printStackTrace();
			return rm;
		}								
    }
       
    
    
    
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
        
    class PostPost extends AsyncTask<String,Integer,String> {
    	
    	ProgressDialog dialog = new ProgressDialog(FaceProfilerActivity.this);
    	//ProgressBar dialog = new ProgressBar(FaceProfilerActivity.this);
    	void show() {    	    
    		dialog.setMessage("Loading... This may take a few seconds if your connection is slow.");
    	    dialog.show();
    	}
    	void hide() {
    	    dialog.dismiss();
    	}
    	protected void onPreExecute(){
    		show();	
    	}
		protected void onProgressUpdate(Integer... progress){
			setProgress(progress[0]);	
		}	
		@Override
		protected void onPostExecute(String result){
			//toaster(result);
			hide();
		}
    	protected String doInBackground(String... params){
    		
    		InputStream is = null;
    		String result = "";
    		JSONObject jArray = null;
    		 
    		try{
    			//declare HTTP POST details
    			HttpClient httpclient = new DefaultHttpClient();
    			HttpContext localContext = new BasicHttpContext();
    			HttpPost httppost = new HttpPost(params[0]);
    			//set POST parameters
    			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
    			nameValuePairs.add(new BasicNameValuePair("api_key", "c03aa50e52664cca93b3afd0b16f80e2"));
    			nameValuePairs.add(new BasicNameValuePair("uids", "all@fma"));
    			nameValuePairs.add(new BasicNameValuePair("api_secret", "0c630941bda34278868d0a45f55b619f"));
    			nameValuePairs.add(new BasicNameValuePair("attributes", "all"));
    			nameValuePairs.add(new BasicNameValuePair("detector", "Aggressive"));
    			publishProgress(25);
    			
    			//CONDITIONS
    			if (params[2].equals("1")){			//for web-based image
    				nameValuePairs.add(new BasicNameValuePair("urls", params[1]));
    				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    				HttpResponse response = httpclient.execute(httppost);
    				HttpEntity entity = response.getEntity();
    				is = entity.getContent();
    			}
    			else {			//for local image
    			nameValuePairs.add(new BasicNameValuePair("filename", "temp.jpg"));
    			nameValuePairs.add(new BasicNameValuePair("file", params[1])); 
    			MultipartEntity entity1 = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    				for(int index=0; index < nameValuePairs.size(); index++) {
    		            if(nameValuePairs.get(index).getName().equalsIgnoreCase("file")) {
    		                // If the key equals to "image", we use FileBody to transfer the data
    		                entity1.addPart(nameValuePairs.get(index).getName(), 
    		                		new FileBody(new File (nameValuePairs.get(index).getValue())));
    		                //toaster(nameValuePairs.toString());
    		            } 
    		            else {
    		                // Normal string data
    		                entity1.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
    		                //toaster("NORM");
    		            }
    	        }
    				publishProgress(50);
    				//String mytest = entity1.toString();
    				
    			httppost.setEntity(entity1);
    	        HttpResponse response = httpclient.execute(httppost, localContext);
    			HttpEntity entity = response.getEntity();
    			//String test = "1";
    			is = entity.getContent();
    			}
    		}
    		catch(Exception e){
    			Log.e("log_tag", "Error in connection "+e.toString());
    		}
    		//Convert response to string
    		try{
    			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
    			StringBuilder sb = new StringBuilder();
    			String line = null;
    			while ((line = reader.readLine()) != null) {
    				sb.append(line + "\n");
    			}
    			is.close();
    			result=sb.toString();
    			
    			publishProgress(75);
    		}
    		
    		catch(Exception e){
    			Log.e("log_tag", "Error converting result "+e.toString());
    		}
		
    		//try parse the string to a JSON object
    		try{
    	        	jArray = new JSONObject(result);
    	        	publishProgress(100);
    		}
    		catch(JSONException e){
    			Log.e("log_tag", "Error parsing data "+e.toString());
    		}
    		  		
    		try {
    			JSONArray photos = jArray.getJSONArray("photos");
    			JSONObject tags = photos.getJSONObject(0);
    			JSONArray tags2 = tags.getJSONArray("tags");   
    			JSONObject tags3 = tags2.getJSONObject(0);			
    			//Attributes 
    			JSONObject attr = tags3.getJSONObject("attributes");
    			
    			//recog
    			JSONArray uids = tags3.getJSONArray("uids"); 
    			
    			//String listofnames;
    			String name = "";
    			
    			for (int i=0; i<uids.length();i++){
    				
    			name = name + uids.getJSONObject(i).getString("uid") + " (" +uids.getJSONObject(i).getString("confidence") + ")" + "\n";
    			
    			}
    			
    			final String namer = name;
    			   			
    			//gender
    			final String gender = attr.getJSONObject("gender").getString("value");
    			final String genderC = attr.getJSONObject("gender").getString("confidence");
    			final String Pron; //pronoun
    			final String pron;
    			final String poss;   			
    			if (gender.equals("female")) { Pron = "She"; pron = "she"; poss = "her";}
    			else { Pron = "He"; pron = "he"; poss = "his";}
    						
    			//validation
    			final String face = attr.getJSONObject("face").getString("value");
    			final String faceC = attr.getJSONObject("face").getString("confidence");
    			final String facesure;
    			if (face.equals("true")) {facesure = "";} 
    			else {facesure = "not ";}
    			  			
    			//age
//    			final String age_max = attr.getJSONObject("age_max").getString("value");
//    			final String age_min = attr.getJSONObject("age_min").getString("value");
//    			final String age_est = attr.getJSONObject("age_est").getString("value");
//    			final String age_estC = attr.getJSONObject("age_est").getString("confidence");
// 			
    			//expression
//    			final String mood = attr.getJSONObject("mood").getString("value");
//    			int moodC = attr.getJSONObject("mood").getInt("confidence");
//    			final String moodsure;
//    			if ( moodC > 80 ) { moodsure = "I am very sure that "; }
//    			else if (moodC > 60 ) { moodsure = "I am quite sure that ";}
//    			else if (moodC > 40 ) { moodsure = "I am half sure that ";}
//    			else {moodsure = "I am not sure if ";}
    			final String moodsure = "I think that ";
    			final String smilestate;
    			final String smilesure;
    			final String mood;
    			String smile = attr.getJSONObject("smiling").getString("value");					
    			int smileC = attr.getJSONObject("smiling").getInt("confidence");
    			if ( smileC > 80 ) { smilesure = " is certainly "; }
    			else if (smileC > 60 ) { smilesure = " is quite certainly ";}
    			else if (smileC > 40 ) { smilesure = " seems to be ";}
    			else {smilesure = " is perhaps ";}				
    			String glassval = " ";
    			final String glasssure;
    			String glasses = attr.getJSONObject("glasses").getString("value");
    			if (glasses.equals("false")){glassval=" not ";} 
    			int glassesC = attr.getJSONObject("glasses").getInt("confidence");
    			if ( glassesC > 80 ) { glasssure = " I am very sure " + pron + " is" + glassval + "wearing glasses." ; }
    			else if (glassesC > 60 ) {  glasssure = " I am very sure " + pron + " is" + glassval + "wearing glasses." ;}
    			else if (glassesC > 40 ) { glasssure = "";}
    			else {glasssure = "";}			
    			if (smile.equals("true")) {smilestate = "smiling.";}
    			else {smilestate = "not smiling.";}
    			if (smile.equals("true")) {mood = "happy.";}
    			else {mood = "not very happy";}
    			
    			
  			
    			//facemap    		       
    	       Float elx = (float) tags3.getJSONObject("eye_left").getDouble("x");
    	       Float ely = (float) tags3.getJSONObject("eye_left").getDouble("y");    		  
    	       Float erx = (float) tags3.getJSONObject("eye_right").getDouble("x");
    	       Float ery = (float) tags3.getJSONObject("eye_right").getDouble("y");
//    	       Float mlx = (float) tags3.getJSONObject("mouth_left").getDouble("x");
//    	       Float mly = (float) tags3.getJSONObject("mouth_left").getDouble("y");    		
    	       Float mcx = (float) tags3.getJSONObject("mouth_center").getDouble("x");
    	       Float mcy = (float) tags3.getJSONObject("mouth_center").getDouble("y");    	       
//    	       Float mrx = (float) tags3.getJSONObject("mouth_right").getDouble("x");
//    	       Float mry = (float) tags3.getJSONObject("mouth_right").getDouble("y");
    	       Float nx = (float) tags3.getJSONObject("nose").getDouble("x");
    	       Float ny = (float) tags3.getJSONObject("nose").getDouble("y");
    	       	Float fwidth = (float) tags3.getDouble("width");
	   			Float fheight = (float) tags3.getDouble("height");
	   			Float fx = (float) tags3.getJSONObject("center").getDouble("x");
	   			Float fy = (float) tags3.getJSONObject("center").getDouble("y");
	   			Float roll = (float) tags3.getDouble("roll");
	   			
	   			
	   			////Ratio between eyedistance and eye-nose
	   			Float R1;
	   			if (ery > ely) {R1 = (elx - erx)/ (ny - ery);}
	   			else R1 = (elx - erx)/ (ny -ely);
	   			///Ratio between eyedistance and eye-mouth
	   			Float R2;
	   			if (ery > ely) {R2 = (elx - erx)/ (mcy - ery);}
	   			else R2 = (elx - erx)/ (mcy - ely);
	   			///Ratio between eye-nose and eye-mouth
	   			Float R4;
	   			if (ery > ely) {R4 = (ery - ny) / (ery - mcy);}
	   			else R4 = (ely - ny) / (ely - mcy);
	   			//age regression
	   			double AA = -91.94 + (R1*16.606) - (R2*55.443) + (R4* 303.065);
	   			double BB = -43.6 + -10.89*R2 + 187.29*R4;
    			//age
//    			String age_max ;
//    			String age_min ;
//    			String age_est  ;
////    			final String age_estC = ;
    			double maxa;
    			double mina;
    			double maxb;
    			double minb;
    			
    			if (AA > BB) { maxa = AA; mina = BB ;}
    			else {maxa = BB; mina = AA;}	   			
	   			if ((mina - 5) < 0){ minb=0;}
	   			else minb = mina -5;
    			maxb = maxa+5;
    			
    			int maxc = (int) maxb;
    			int minc = (int) minb;
    			int AAc; 
    			if (AA > 0) { AAc = (int) AA;} else AAc = 0;
    			
    			final String age_max = String.valueOf(maxc);
    			final String age_min = String.valueOf(minc);
    			final String age_est = String.valueOf(AAc);
    			
    	       Bitmap bm;
    			try {
    				 				
    				if (params[2].equals("0")) {bm = BitmapFactory.decodeFile(params[1]);
    				} 
    					
    				else		{URL newurl = new URL(params[1]);
    				Bitmap sbm = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
    				
    					if (sbm.getHeight() > 400 ) {
    						
//    						double numh = (double) sbm.getHeight() / 350;	
//    						double numw = (double) sbm.getHeight() / 350;	
    						
    						double he = (double) sbm.getHeight() - 400;
    						double ew = (double) he / sbm.getHeight();
    						double we = (double) ew * sbm.getWidth();
    						
    						int h = (int)he;
    						int w = (int)we;
    						
    						bm = Bitmap.createScaledBitmap(sbm,sbm.getWidth()-w,
    								sbm.getHeight()-h,true);
    					}
    					else {bm = sbm;}
    				//write bitmap to file
    			
    				}
    				
    				float bW = bm.getWidth();
    				float bH = bm.getHeight();
    				float fW = tags3.getInt("width");
    				//float fH = tags3.getInt("height");
    				
    				Bitmap bm2 = bm.copy(Config.RGB_565, true);
    				
    				Canvas mCanvas = new Canvas(bm2);
    				Paint mPaint = new Paint();
				
    				mPaint.setColor(Color.RED);
    				//mPaint.setStyle(Paint.Style.STROKE);
    				 				
    				mPaint.setStrokeWidth(2);
    				mPaint.setAlpha(150);
    				mCanvas.drawCircle(elx/100*bW,ely/100*bH,(float) 0.12*fW,mPaint);
    				mCanvas.drawCircle(erx/100*bW,ery/100*bH,(float) 0.12*fW,mPaint);
//    				mCanvas.drawCircle(mlx/100*bW,mly/100*bH,(float) 0.12*fW,mPaint);
//    				mCanvas.drawCircle(mrx/100*bW,mry/100*bH,(float) 0.12*fW,mPaint);
    				mCanvas.drawCircle(mcx/100*bW,mcy/100*bH,(float) 0.12*fW,mPaint);
    				mCanvas.drawCircle(nx/100*bW,ny/100*bH,(float) 0.12*fW,mPaint);
//    				mCanvas.drawCircle(chx/100*bW,chy/100*bH,(float) 0.12*fW,mPaint);
    				
    	///////////////////////////			
    				Paint mPaint2 = new Paint();
    				
    				mPaint2.setColor(Color.GREEN);
    				mPaint2.setStyle(Paint.Style.STROKE);
    				
    				mPaint2.setStrokeWidth(1);
    				mPaint2.setAlpha(150);
    				mCanvas.drawCircle(elx/100*bW,ely/100*bH,(float) 0.3*fW,mPaint2);
    				mCanvas.drawCircle(erx/100*bW,ery/100*bH,(float) 0.3*fW,mPaint2);
//    				mCanvas.drawCircle(mlx/100*bW,mly/100*bH,(float) 0.3*fW,mPaint2);
//    				mCanvas.drawCircle(mrx/100*bW,mry/100*bH,(float) 0.3*fW,mPaint2);
    				mCanvas.drawCircle(mcx/100*bW,mcy/100*bH,(float) 0.3*fW,mPaint2);
    				mCanvas.drawCircle(nx/100*bW,ny/100*bH,(float) 0.3*fW,mPaint2);
//    				mCanvas.drawCircle(chx/100*bW,chy/100*bH,(float) 0.3*fW,mPaint2);
    				
    				Paint mPaint3 = new Paint();
    				mPaint3.setStyle(Paint.Style.STROKE);
    				mPaint3.setStrokeWidth(4);
    				
    			
    				mPaint3.setColor(Color.RED);
    				mPaint3.setAlpha(150);
    				
    				int left = (int) ((fx/100*bW)-(fwidth/100*bW/2));
    				int top = (int) ((fy/100*bH)-(fheight/100*bH/2));
    				int right = (int) ((fx/100*bW)+(fwidth/100*bW/2));
    				int bottom = (int) ((fy/100*bH)+(fheight/100*bH/2));
//    				int ileft = (int) ((fx/100*bW)-((elx + 20)/100*bW/2));
//    				int itop = (int) ((fy/100*bH)-((ely + 20)/100*bH/2));
//    				int iright = (int) ((fx/100*bW)+(erx + 20/100*bW/2));
//    				int ibottom = (int) ((fy/100*bH)+(ny +20/100*bH/2));
    				    				
    				mCanvas.drawRect((fx/100*bW)-(fwidth/100*bW/2), (fy/100*bH)-(fheight/100*bH/2), 
    						(fx/100*bW)+(fwidth/100*bW/2), (fy/100*bH)+(fheight/100*bH/2), mPaint3);
    				
    				final Bitmap bm3;
    				boolean zoom = false;
    				
    				  final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
    			         if (checkBox.isChecked()) {
    			              zoom = true;
    			         }
    				
    				if (zoom == false) {
    					bm3 = bm2;
    				}
    				
    				// zoom is enabled...
    				else {
    				
    				int a = left-20;
    				if (a < 0){a=0;};
    				int b = top-20;
    				if (b < 0){b=0;};
    				
    				
    				Bitmap bm4 = Bitmap.createBitmap(bm2,a,b,right-left+40,bottom-top+40);
    				
    				
    				
    				if (bm4.getWidth() < 1000) { 
    					
    					int wd = 400 - bm4.getWidth();
        				double wr = wd / bm4.getWidth();
        				double nh = wr * bm4.getHeight();
        				int ch = (int) nh;
    					
    					bm3 = Bitmap.createScaledBitmap(bm4, 400, ch + bm4.getHeight(), true); }
    				else {bm3=bm4;}
    				
    				}
    				
    				runOnUiThread(new Runnable() {
    				     public void run() {

    				//stuff that updates ui

    				    
    				ImageView pic = (ImageView) findViewById(R.id.pic);
    				 pic.setImageBitmap(bm3); 
    				     }
    				});
//    				
//    					Bitmap celebi = BitmapFactory.decodeResource(FaceProfilerActivity.this.getResources(),
//                            R.drawable.pitt);
    				
    				Bitmap celebi;
    				
    				float centerx;
   				    float centery;
   				    float imagew;
   				    float imageh;
    				
    				
    				if (gender.equals("female")){
    							
    				celebi = BitmapFactory.decodeResource(FaceProfilerActivity.this.getResources(),
                            R.drawable.morris);

    			    centerx = (float) 49.56;
				    centery = (float) 33.9;
				    imagew =  (float) 35.9;
				    imageh = (float) 28.4;
				
    				}
    				else {
    				
					celebi = BitmapFactory.decodeResource(FaceProfilerActivity.this.getResources(),
                            R.drawable.pitt);
    				
    			    centerx = (float) 55.67;
				    centery = (float) 42.99;
				    imagew =  (float) 36.67;
				    imageh = (float) 24.34;
    					
    				}
    				

    				
    				    Bitmap spare = bm.copy(Config.RGB_565, true);
    				    Bitmap bm1 = celebi.copy(Config.ARGB_8888,true);
    				    
//    				    float u = (float) ((56.33-18.11)/100*bm1.getWidth());
//    				    float w =	(float) ((58.17-27.16)/100*bm1.getHeight());    				    
//    				    
//    				    float x = (float) (36.22/100*bm1.getWidth());
//    				    float y = (float) (54.33/100*bm1.getHeight());
    				   
				   
//		    	   Bitmap eyes = Bitmap.createBitmap(spare,ileft,itop,iright - ileft,
//				    		ibottom - itop);
		    
				    float u = (float) ((centerx-(imagew/2))/100*bm1.getWidth());
				    float w =	(float) ((centery-(imageh/2))/100*bm1.getHeight());    				    
				    
				    float x = (float) (imagew/100*bm1.getWidth());
				    float y = (float) (imageh/100*bm1.getHeight());
//    	    				    float x = (float) (fwidth/100*bm1.getWidth());
//    	    				    float y = (float) (fheight/100*bm1.getHeight());
				    Bitmap eyes = Bitmap.createBitmap(spare,left,top,right - left,
				    		bottom - top);

    				    
    				 
    				    
    				    
    				    // make smaller first!!!
    				    //face only
    				    Bitmap spare3 = Bitmap.createScaledBitmap(eyes,(int) x,(int) y,true);
    				
//    				    final Paint paint = new Paint();   
//    				    paint.setAntiAlias(true);
//    				    Canvas c = new Canvas(bm1);
//    				    c.drawBitmap(bm1,0,0,paint);
//    				    c.drawBitmap(spare3,u,w ,paint);
//    				    
//    				    
//    				    final Bitmap bmO = bm1;
    				    
    				    Bitmap bmp1 = bm1;
    				    Bitmap bmp2 = spare3;
    				    
    				    
    				    Bitmap bmOb = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
    			        Canvas canvas = new Canvas(bmOb);
    			        
    			        canvas.drawBitmap(bmp2, u,w, null);
    			        canvas.drawBitmap(bmp1, new Matrix(), null);
    				    final Bitmap bmO = bmOb;

    				
    				runOnUiThread(new Runnable() {
   				     public void run() {

   				    	
    				ImageView image = (ImageView) findViewById(R.id.celebi);
    				image.setVisibility(View.INVISIBLE);
    				image.setImageBitmap(bmO);
   				     } 
       				});	
    				
    				
    			} catch (Exception e1) {
    				
    				
    				runOnUiThread(new Runnable() {
    				     public void run() {
    				Toast.makeText(getApplicationContext(),"Sorry I/O exception occurred, please try another image.",
    			            Toast.LENGTH_LONG).show();
    				     }
    				});
    			} 

    			//draw on bitmap
    			
    			runOnUiThread(new Runnable() {
   			     public void run() {

   			//stuff that updates ui

   			TextView tv2 = (TextView) findViewById(R.id.text2);
		        tv2.setText(moodsure + poss + " mood is " + mood + ". " + Pron + smilesure + smilestate + glasssure);
		        
			
		        TextView tv4 = (TextView) findViewById(R.id.text4);
		        tv4.setText("This person looks like (score)... \n"
		        + namer.replace("@fma", "").replace("_"," "));
		     
		    	
   			
   			TextView tv3 = (TextView) findViewById(R.id.text3);
   	        tv3.setText("I am " + faceC + "% " + facesure + "confident in recognizing a face from this picture.");
   			
   			TextView tv1 = (TextView) findViewById(R.id.text1);
   	        tv1.setText("I am " + genderC + "% sure this person is " + gender +
   	        		". My system has gone wonky and can only roughly guess " + poss + " age range. " + Pron + " seems to be between " 
   	        		+ age_min + " and " + age_max +
   	        		" years old... I'm guessing " + age_est + ".");
   			     }
   			});
    			
    			
    		} catch (JSONException e) {
    			
    			if (params[2].equals("0")){
    				runOnUiThread(new Runnable() {
    				     public void run() {
    				    Toast.makeText(getApplicationContext(),"Image not recognizable!",
    		            Toast.LENGTH_LONG).show();
    				//stuff that updates ui
    				    }
    				});
    			}
    			else {
    				runOnUiThread(new Runnable() {
    				     public void run() {
    				toaster("Image not recognizable. Is this URL valid?");    				         				     
    				     } 
    				});	    			
    			}
    		}	
    		
    		String haha = "success";
    		
    		return haha;
    		
    	
    		}
    		
    	
    	}
    	
    	
    
}