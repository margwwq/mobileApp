package ru.mirea.mikhaylenkoma.securesharedpreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KeyGenParameterSpec keyGenParameterSpec	=	MasterKeys.AES256_GCM_SPEC;

        try	{
            String	mainKeyAlias	=	MasterKeys.getOrCreate(keyGenParameterSpec);

            SharedPreferences secureSharedPreferences	=	EncryptedSharedPreferences.create(
                    "shared",
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            secureSharedPreferences.edit().putString("secure",	"ЛЮБИМЫЙ ПОЭТ");

            String	result	=	secureSharedPreferences.getString("secure",	"ЛЮБИМЫЙ АКТЕР");

        }	catch	(GeneralSecurityException | IOException e)	{
            throw	new	RuntimeException(e);
        }
    }
}