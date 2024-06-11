package ru.mirea.mikhaylenkoma.mireaproject.ui.les8;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mirea.mikhaylenkoma.mireaproject.R;
import ru.mirea.mikhaylenkoma.mireaproject.databinding.FragmentMapBinding;

public class MapFragment extends Fragment {
    private FragmentMapBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);

        setButtons();

        return binding.getRoot();
    }

    private void setButtons() {
        binding.buttonShow1.setOnClickListener(v -> startMapActivity(
                "Сиринъ",
                "ул. Ильинка, 4, Москва",
                55.753174904211455, 37.62586004232783));

        binding.buttonShow2.setOnClickListener(v -> startMapActivity(
                "Брусника",
                "ул. Маросейка, 8, Москва",
                55.757556, 37.636401));

        binding.buttonShow3.setOnClickListener(v -> startMapActivity(
                "Франсуа",
                "ул. Чаплыгина, 16, Москва",
                55.762449, 37.648376));

        binding.buttonShow4.setOnClickListener(v -> startMapActivity(
                "Sweet Happiness" ,
                "Газетный пер., 9, стр. 7, корп. 1, стр. 1, Москва",
                55.758721, 37.608212));
    }

    private void startMapActivity(String title, String description, double latitude, double longitude) {
        Intent intent = new Intent(getActivity(), MapActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
    }
}