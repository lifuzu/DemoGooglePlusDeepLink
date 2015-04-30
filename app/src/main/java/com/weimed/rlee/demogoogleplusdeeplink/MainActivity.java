package com.weimed.rlee.demogoogleplusdeeplink;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.plus.PlusShare;



public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add listener for the button
        final Button button = (Button) findViewById(R.id.helloworld);
        button.setOnClickListener(this);

        // Receive the deep link
        //String deepLinkId = PlusShare.getDeepLinkId(this.getIntent());

        Uri data = getIntent().getData();
        if (data != null) {
            Toast.makeText(getApplicationContext(), data.getPath(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        String articleApplinksUrl = "intent://contentstream.cloudapp.net:8080/article/m/1/9780231513531.4#Intent;scheme=nook;package=com.weimed.rlee.demogoogleplusdeeplink;end";
        String articleApplinkUrlSpecific = "nook://contentstream.cloudapp.net:8080/article/m/1/9780231513531.4";
        switch (view.getId()) {
            case R.id.helloworld:
                PlusShare.Builder builder = new PlusShare.Builder(this);

                // Set call-to-action metadata.
                //builder.addCallToAction("VIEW", Uri.parse(articleApplinksUrl), "/m/1/2940041388447.10");
//                builder.addCallToAction(
//                        "CREATE_ITEM", /** call-to-action button label */
//                        Uri.parse(articleApplinksUrl), /** call-to-action url (for desktop use) */
//                        "/article/m/1/2940041388447.10" /** call to action deep-link ID (for mobile use), 512 characters or fewer */);

                // Set the content url (for desktop use).
                builder.setContentUrl(Uri.parse(articleApplinksUrl));

                // Set the target deep-link ID (for mobile use).
//                builder.setContentDeepLinkId("nook://contentstream.cloudapp.net:8080/article");
                builder.setContentDeepLinkId("nook://contentstream.cloudapp.net/article",
                        "Test Title",
                        "Test Description",
                        Uri.parse("https://developers.google.com/+/images/interactive-post-android.png"));

                // Set the share text.
                builder.setText("Create your Google+ Page too!");
                builder.setType("text/plain");

                startActivityForResult(builder.getIntent(), 0);
                break;
        }
//      Toast.makeText(getApplicationContext(), "Hello World!", Toast.LENGTH_SHORT).show();
        // Share a web page here
//        String articleApplinksUrl = "http://contentstream.cloudapp.net:8080/article/m/1/2940041388447.10";
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
//                shareIntent.putExtra(Intent.EXTRA_TEXT, articleApplinksUrl);
//                shareIntent.setType("text/plain");
//                startActivity(shareIntent);
//        Intent shareIntent = new PlusShare.Builder(this)
//                .setText("Check out: " + articleApplinksUrl)
//                .setType("text/plain")
//                .setContentUrl(Uri.parse(articleApplinksUrl))
//                .setContentDeepLinkId(articleApplinksUrl)
//                .getIntent();
//
//        startActivityForResult(shareIntent, 0);
    }
}
