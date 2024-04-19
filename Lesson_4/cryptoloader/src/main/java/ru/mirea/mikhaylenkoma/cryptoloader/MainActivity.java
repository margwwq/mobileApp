package ru.mirea.mikhaylenkoma.cryptoloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {
    public	final	String	TAG	=	this.getClass().getSimpleName();
    private	final	int	LoaderID	=	1234;

    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.textEdit);
    }

    public void	onClickButton(View view)	{

        Bundle	bundle	=	new	Bundle();
        SecretKey key = generateKey();
        bundle.putByteArray(MyLoader.ARG_WORD,	encryptMsg(editText.getText().toString(), key));
        bundle.putByteArray("key",	key.getEncoded());
        LoaderManager.getInstance(this).initLoader(LoaderID,	bundle,	this);
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        if	(id	==	LoaderID)	{
            Toast.makeText(this,	"onCreateLoader:"	+	id,	Toast.LENGTH_SHORT).show();
            return	new	MyLoader(this,	args);
        }
        throw	new InvalidParameterException("Invalid	loader	id");
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
        if	(loader.getId()	==	LoaderID)	{
            Log.d(TAG,	"onLoadFinished:	"	+	data);
            Toast.makeText(this,	"onLoadFinished:	"	+	data,	Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        Log.d(TAG,	"onLoaderReset");
    }

    public	static SecretKey generateKey() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any	data	used	as	random	seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256, sr);
            return new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public	static	byte[]	encryptMsg(String	message,	SecretKey	secret) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            return cipher.doFinal(message.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }
}