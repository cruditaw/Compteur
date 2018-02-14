package com.example.cdsm.compteur;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private TextView tvDigit0;
    private TextView tvDigit1;
    private TextView tvDigit2;
    private TextView tvDigit3;
    private EditText etLimit;
    private EditText etStartVal;
    private Button btnStart;
    private Button btnReset;
    private Switch swLampe;
    private CompteurADAL c;
    private CountDownTimer cdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int limit = -1;
                int start = -1;

                limit = checkInLimit(start);
                start = checkInStart(limit);
                if (start >= limit) {
                    Toast.makeText(MainActivity.this, "La valeur de départ ne peut pas être superieur à la valeur limite .", Toast.LENGTH_LONG).show();
                    return;
                }

                initCompteur(limit, start);
                cdt.start();
            }
        });


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdt.cancel();
                c.resetCompteur();
                tvDigit0.setText(String.valueOf(0));
                tvDigit1.setText(String.valueOf(0));
                tvDigit2.setText(String.valueOf(0));
                tvDigit3.setText(String.valueOf(0));
                swLampe.setChecked(false);
            }
        });

    }

    private void initView() {
        tvDigit0 = ((TextView) findViewById(R.id.tvDigit0));
        tvDigit1 = ((TextView) findViewById(R.id.tvDigit1));
        tvDigit2 = ((TextView) findViewById(R.id.tvDigit2));
        tvDigit3 = ((TextView) findViewById(R.id.tvDigit3));
        btnStart = ((Button) findViewById(R.id.btnStart));
        btnReset = ((Button) findViewById(R.id.btnReset));
        swLampe = ((Switch) findViewById(R.id.swLampe));
        etLimit = ((EditText) findViewById(R.id.etCptLimit));
        etStartVal = ((EditText) findViewById(R.id.etCptStartVal));

        swLampe.setChecked(false);
        tvDigit0.setText(String.valueOf(0));
        tvDigit1.setText(String.valueOf(0));
        tvDigit2.setText(String.valueOf(0));
        tvDigit3.setText(String.valueOf(0));
    }

    private void initCompteur(int limit, int start) {
        int nl = 100;
        //Log.w("INIT COMPTEUR LIMIT", "Limit : " + limit + "   - start : " + start);
        if (limit != -1 && start != -1) {
            c = new CompteurADAL(limit, start);
            nl = (limit - start) * 1000;

        }
        if (limit != -1 && start == -1) {
            c = new CompteurADAL(limit);
            nl = limit * 1000;
        }
        if (limit == -1 && start == -1) {
            c = new CompteurADAL();
            nl = nl * 1000;
        }

        cdt = new CountDownTimer(nl, 1000) {
            @Override
            public void onTick(long l) {
                if (c.getCompteur().getCurrent() < c.getCompteur().getLimit()) {
                    c.getCompteur().increment();
                    tvDigit0.setText(String.valueOf(c.getCompteur().getDigitsTab()[0].getValue()));
                    tvDigit1.setText(String.valueOf(c.getCompteur().getDigitsTab()[1].getValue()));
                    tvDigit2.setText(String.valueOf(c.getCompteur().getDigitsTab()[2].getValue()));
                    tvDigit3.setText(String.valueOf(c.getCompteur().getDigitsTab()[3].getValue()));
                }
            }

            @Override
            public void onFinish() {
                c.getLampe().setEtatLampe(true);
                swLampe.setChecked(true);
               // Log.w("END", "LIMIT REACHED");
            }
        };
    }

    private int checkInLimit(int limit) {
        if (!etLimit.getText().toString().isEmpty()) {
            try {
                limit = Integer.valueOf(etLimit.getText().toString());
            } catch (NumberFormatException nfe) {
                Toast.makeText(MainActivity.this, "Une erreur est survenue, verifiez votre valeur limite !", Toast.LENGTH_LONG).show();
            }
        }
        return limit;
    }

    private int checkInStart(int start) {
        if (!etStartVal.getText().toString().isEmpty()) {
            try {
                start = Integer.valueOf(etStartVal.getText().toString());
            } catch (NumberFormatException nfe) {
                Toast.makeText(MainActivity.this, "Une erreur est survenue, verifiez votre valeur de départ !", Toast.LENGTH_LONG).show();

            }
        }
        return start;
    }
}
