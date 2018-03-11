package com.example.cdsm.compteur;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * IMPORTANT : Bug quand orientation ecran change 3fois;
 * OnSaveInstanceState declare unsafe, passer par onPause()
 * -> Sauvegarder dans des variables locales ??
 */
public class MainActivity extends AppCompatActivity {

    private CompteurADAL compteurALampe;
    private CountDownTimer timer;
    private boolean compteurPaused;
    private boolean compteurReseted;
    private TextView tvDigit0;
    private TextView tvDigit1;
    private TextView tvDigit2;
    private TextView tvDigit3;
    private EditText etLimit;
    private EditText etStartVal;
    private Button btnStart;
    private Button btnPause;
    private Button btnResume;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMainActivity();
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleStartClick();
            }
        });
        btnResume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                handleResumeClick();
            }
        });
        btnPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePauseClick();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleResetClick();
            }
        });
    }

    private void initMainActivity() {
        tvDigit0 = ((TextView) findViewById(R.id.tvDigit0));
        tvDigit1 = ((TextView) findViewById(R.id.tvDigit1));
        tvDigit2 = ((TextView) findViewById(R.id.tvDigit2));
        tvDigit3 = ((TextView) findViewById(R.id.tvDigit3));
        btnStart = ((Button) findViewById(R.id.btnStart));
        btnReset = ((Button) findViewById(R.id.btnReset));
        btnResume = ((Button) findViewById(R.id.btnResume));
        btnPause = ((Button) findViewById(R.id.btnPause));
        etLimit = ((EditText) findViewById(R.id.etCptLimit));
        etStartVal = ((EditText) findViewById(R.id.etCptStartVal));

        compteurPaused = false;
        compteurReseted = false;
        btnPause.setEnabled(false);
        btnResume.setEnabled(false);
        btnReset.setEnabled(false);

        tvDigit0.setText(String.valueOf(0));
        tvDigit1.setText(String.valueOf(0));
        tvDigit2.setText(String.valueOf(0));
        tvDigit3.setText(String.valueOf(0));
    }

    private void handlePauseClick() {
        compteurPaused = true;
        compteurALampe.getCompteur().pause();
        timer.cancel();
        btnPause.setEnabled(false);
        btnResume.setEnabled(true);
    }

    private void handleResumeClick() {
        compteurPaused = false;
        String start = String.valueOf(compteurALampe.getCompteur().getStart());
        String limit = String.valueOf(compteurALampe.getCompteur().getLimit());
        initCompteur(limit, start);
        timer.start();
        btnPause.setEnabled(true);
        btnResume.setEnabled(false);
    }

    private void handleStartClick() {
        if (!handleInputErrors()) {
            return;
        }

        compteurReseted = false;
        btnPause.setEnabled(true);
        btnResume.setEnabled(false);
        btnStart.setEnabled(false);
        btnReset.setEnabled(true);
        String start = etStartVal.getText().toString();
        String limit = etLimit.getText().toString();
        initCompteur(limit, start);
        timer.start();
    }

    private boolean handleInputErrors() {
        if (etLimit.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "La valeur de limite est necessaire !", Toast.LENGTH_LONG).show();
            return false;
        } else {
            if (!etLimit.getText().toString().matches("[0-9]{1,4}")) {
                Toast.makeText(MainActivity.this, "La valeur de limite est incorrecte !", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        if (!etStartVal.getText().toString().isEmpty()) {
            if (!etStartVal.getText().toString().matches("[0-9]{1,4}")) {
                Toast.makeText(MainActivity.this, "La valeur de depart est incorrecte !", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private void handleResetClick() {
        compteurALampe.resetCompteur();
        timer.cancel();
        compteurPaused = false;
        compteurReseted = true;
        btnPause.setEnabled(false);
        btnResume.setEnabled(false);
        btnReset.setEnabled(false);
        btnStart.setEnabled(true);
        tvDigit0.setText(String.valueOf(0));
        tvDigit1.setText(String.valueOf(0));
        tvDigit2.setText(String.valueOf(0));
        tvDigit3.setText(String.valueOf(0));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        timer.cancel();
        int compteurValue[] = makeDigitsFromCompteur();
        String limit = String.valueOf(compteurALampe.getCompteur().getLimit());
        String start = String.valueOf(compteurALampe.getCompteur().getCurrent());
        outState.putIntArray("STATE_COMPTEUR", compteurValue);
        outState.putInt("STATE_LIMIT", Integer.valueOf(limit));
        outState.putInt("STATE_START", Integer.valueOf(start));
        outState.putBoolean("STATE_PAUSE", compteurPaused);
        outState.putBoolean("STATE_RESETED", compteurReseted);
    }

    private int[] makeDigitsFromCompteur() {
        int compteurValue[] = new int[4];
        for (int i = 0; i < 4; i++) {
            compteurValue[i] = compteurALampe.getCompteur().getDigitsTab()[i].getValue();
        }
        return compteurValue;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            int[] compteurValue = makeDigitsFromBundle(savedInstanceState);
            String limit = String.valueOf(savedInstanceState.getInt("STATE_LIMIT"));
            String start = String.valueOf(savedInstanceState.getInt("STATE_START"));

            compteurPaused = savedInstanceState.getBoolean("STATE_PAUSE");
            compteurReseted = savedInstanceState.getBoolean("STATE_RESETED");
            initCompteur(limit, start);
            handleRestore(compteurValue);
        }
    }

    private int[] makeDigitsFromBundle(Bundle savedInstanceState) {
        int compteurValue[] = new int[4];
        for (int i = 0; i < 4; i++) {
            compteurValue = savedInstanceState.getIntArray("STATE_COMPTEUR");
        }
        return compteurValue;
    }

    private void handleRestore(int[] compteurValue) {
        if (!compteurReseted) {
            handleUnresetState(compteurValue);
        } else {
            tvDigit0.setText(String.valueOf(0));
            tvDigit1.setText(String.valueOf(0));
            tvDigit2.setText(String.valueOf(0));
            tvDigit3.setText(String.valueOf(0));
        }
    }

    private void handleUnresetState(int[] compteurValue) {
        tvDigit0.setText(String.valueOf(compteurValue[0]));
        tvDigit1.setText(String.valueOf(compteurValue[1]));
        tvDigit2.setText(String.valueOf(compteurValue[2]));
        tvDigit3.setText(String.valueOf(compteurValue[3]));
        if (!compteurPaused) {
            timer.start();
            btnResume.setEnabled(false);
            btnReset.setEnabled(true);
            btnStart.setEnabled(false);
            btnPause.setEnabled(true);
        } else {
            btnResume.setEnabled(true);
            btnReset.setEnabled(true);
            btnStart.setEnabled(false);
            btnPause.setEnabled(false);
        }
    }

    private void initCompteur(String strLimit, String strStart) {
        int limit = checkInValue(strLimit);
        int start = checkInValue(strStart);

        if (start >= limit) {
            Toast.makeText(MainActivity.this, "La valeur de départ ne peut pas être superieur à la valeur limite .", Toast.LENGTH_LONG).show();
            return;
        }

        int nl = initCompteurADAL(limit, start);
        initTimer(nl);
    }

    private int checkInValue(String strValue) {
        int value = -1;
        if (!strValue.isEmpty()) {
            try {
                value = Integer.valueOf(strValue);
            } catch (NumberFormatException nfe) {
                Toast.makeText(MainActivity.this, "Valeur limite incorrecte !", Toast.LENGTH_LONG).show();
            }
        }
        return value;
    }

    private int initCompteurADAL(int limit, int start) {
        int nl = 100;
        if (limit != -1 && start != -1) {
            compteurALampe = new CompteurADAL(limit, start);
            nl = (limit - start) * 1000;

        }
        if (limit != -1 && start == -1) {
            compteurALampe = new CompteurADAL(limit);
            nl = limit * 1000;
        }
        if (limit == -1 && start == -1) {
            compteurALampe = new CompteurADAL();
            nl = nl * 1000;
        }
        nl = nl + 1000;
        return nl;
    }

    private void initTimer(final int nl) {
        timer = new CountDownTimer(nl, 1000) {
            @Override
            public void onTick(long l) {
                compteurALampe.getCompteur().increment();
                tvDigit0.setText(String.valueOf(compteurALampe.getCompteur().getDigitsTab()[0].getValue()));
                tvDigit1.setText(String.valueOf(compteurALampe.getCompteur().getDigitsTab()[1].getValue()));
                tvDigit2.setText(String.valueOf(compteurALampe.getCompteur().getDigitsTab()[2].getValue()));
                tvDigit3.setText(String.valueOf(compteurALampe.getCompteur().getDigitsTab()[3].getValue()));
            }

            @Override
            public void onFinish() {
                compteurALampe.getLampe().allumeLampe();
                handleResetClick();
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            }
        };
    }
}
