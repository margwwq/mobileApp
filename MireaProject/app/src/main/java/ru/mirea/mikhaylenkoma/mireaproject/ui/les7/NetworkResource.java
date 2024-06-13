package ru.mirea.mikhaylenkoma.mireaproject.ui.les7;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mirea.mikhaylenkoma.mireaproject.databinding.FragmentNetworkResourceBinding;

public class NetworkResource extends Fragment {
    private FragmentNetworkResourceBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNetworkResourceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.button.setOnClickListener(v -> new FetchQuoteTask().execute());

        return view;
    }

    private class FetchQuoteTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("https://zenquotes.io/api/quotes?");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();

                StringBuilder response = new StringBuilder();
                int data;
                while ((data = in.read()) != -1) {
                    response.append((char) data);
                }
                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    if (jsonArray.length() > 0) {
                        JSONObject quoteObject = jsonArray.getJSONObject(0);
                        String quote = quoteObject.getString("q");
                        String author = quoteObject.getString("a");
                        binding.quote.setText(quote);
                        binding.author.setText(author);
                    } else {
                        Toast.makeText(requireContext(), "Цитата не найдена", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Ошибка при парсинге цитаты", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Ошибка запроса", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}