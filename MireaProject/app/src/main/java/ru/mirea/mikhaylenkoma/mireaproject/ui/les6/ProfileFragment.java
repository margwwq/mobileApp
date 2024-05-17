package ru.mirea.mikhaylenkoma.mireaproject.ui.les6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mirea.mikhaylenkoma.mireaproject.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private FragmentProfileBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        setSharedPreferences();

        fillFields();

        return binding.getRoot();
    }

    private void setSharedPreferences() {
        binding.buttonSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getActivity().getSharedPreferences("profile_settings",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("GITHUBID", binding.editTextNickname.getText().toString());
                editor.putString("FIO", binding.editTextFIO.getText().toString());
                editor.putString("DATE_OF_BIRTH", binding.editTextDateOfBirth.getText().toString());
                editor.putString("PHONE", binding.editTextPhone.getText().toString());
                editor.putString("EMAIL", binding.editTextTextEmailAddress.getText().toString());

                editor.apply();
            }
        });
    }

    private void fillFields() {
        sharedPreferences = getActivity().getSharedPreferences("profile_settings",
                Context.MODE_PRIVATE);

        binding.editTextNickname.setText(sharedPreferences.getString("GITHUBID", ""));
        binding.editTextFIO.setText(sharedPreferences.getString("FIO", ""));
        binding.editTextDateOfBirth.setText(sharedPreferences
                .getString("DATE_OF_BIRTH", ""));
        binding.editTextPhone.setText(sharedPreferences.getString("PHONE", ""));
        binding.editTextTextEmailAddress.setText(sharedPreferences
                .getString("EMAIL", ""));
    }
}



