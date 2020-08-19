package xyz.dexterolabs.bitcointicker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Currency;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    //Member Variables

    private TextView mBitcoinValueTextView;
    private Spinner mSpinnerCurrencySelect;

    //Constants

    final String BITCOIN_URL = "https://api.coindesk.com/v1/bpi/currentprice/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Casting

        mBitcoinValueTextView =  findViewById(R.id.text_view_bitcoin_value);
        mSpinnerCurrencySelect = findViewById(R.id.spinner_select_currency);


        //Setting up link
        TextView t2 = (TextView) findViewById(R.id.textView2);
        t2.setMovementMethod(LinkMovementMethod.getInstance());

        //Configuring the Spinner

        ArrayAdapter<CharSequence> arrayAdapterCurrency = ArrayAdapter.createFromResource(this
                ,R.array.currency_array,R.layout.spinner_item);

        arrayAdapterCurrency.setDropDownViewResource(R.layout.spinner_drop_down_item);

        mSpinnerCurrencySelect.setAdapter(arrayAdapterCurrency);

        mSpinnerCurrencySelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!isNetworkConnected()) {
                    Toast.makeText(MainActivity.this,"Please connect to the internet",Toast.LENGTH_SHORT).show();
                }
                
                updateBitcoinValue(adapterView.getSelectedItem().toString().substring(adapterView.getSelectedItem().toString().length()-4, adapterView.getSelectedItem().toString().length()-1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mBitcoinValueTextView.setText(R.string.nothing_selected_text);
            }
        });
    }


    //updateBitCoinValue()

    public void updateBitcoinValue(final String currency){

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(BITCOIN_URL+currency,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                // called when response HTTP status is 200 OK

                Log.d("Bitcoin","onSuccessCallback");
                Log.d("Bitcoin",response.toString());


                try {
                    String CurrencyVal = response.getJSONObject("bpi").getJSONObject(currency).getString("rate")+ " "+ currency ;
                    mBitcoinValueTextView.setText(CurrencyVal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Bitcoin","onFailureCallback");

            }

        });

    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }



}
