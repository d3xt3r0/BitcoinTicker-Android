package xyz.dexterolabs.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

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
                ,R.array.currency_array,android.R.layout.simple_spinner_item);

        arrayAdapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerCurrencySelect.setAdapter(arrayAdapterCurrency);

        mSpinnerCurrencySelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateBitcoinValue(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mBitcoinValueTextView.setText("Nothing Selected");
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
                    mBitcoinValueTextView.setText(response.getJSONObject("bpi").getJSONObject(currency).getString("rate"));
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



}
