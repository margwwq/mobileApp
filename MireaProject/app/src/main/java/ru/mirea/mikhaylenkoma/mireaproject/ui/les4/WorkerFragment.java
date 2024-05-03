package ru.mirea.mikhaylenkoma.mireaproject.ui.les4;

import static android.Manifest.permission.POST_NOTIFICATIONS;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import ru.mirea.mikhaylenkoma.mireaproject.databinding.FragmentWorkerBinding;

public class WorkerFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private int PermissionCode = 200;
    private FragmentWorkerBinding _binding;
    private int _currentYear, _currentMonth, _currentDay, _currentHour, _currentMinute,
            _chosedYear, _chosedMonth, _chosedDay, _chosedHour, _chosedMinute;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentWorkerBinding.inflate(inflater, container, false);
        _binding.buttonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDateTime();
            }
        });

        if(ContextCompat.checkSelfPermission(getContext(), POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "РАЗРЕШЕНИЯ ПОЛУЧЕНЫ");
        } else {
            Log.d(TAG, "НЕТ РАЗРЕШЕНИЙ");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{POST_NOTIFICATIONS}, PermissionCode);
        }
        return _binding.getRoot();
    }

    private void ChooseDateTime() {
        final Calendar _currentDate = Calendar.getInstance();
        InitCurrentOptions(_currentDate);

        DatePickerDialog _datePickerDialog = new DatePickerDialog(getContext(),
                (_view, _year, _month, _dayOfMonth) -> {
                    _chosedYear = _year;
                    _chosedMonth = _month;
                    _chosedDay = _dayOfMonth;

                    Calendar _choosedDate = Calendar.getInstance();
                    _choosedDate.set(Calendar.YEAR, _chosedYear);
                    _choosedDate.set(Calendar.MONTH, _chosedMonth);
                    _choosedDate.set(Calendar.DAY_OF_MONTH, _chosedDay);

                    if(_choosedDate.getTimeInMillis() < _currentDate.getTimeInMillis()) {
                        Toast _toast = Toast.makeText(getContext(), "Введите корректную дату",
                                Toast.LENGTH_SHORT);
                        _toast.show();
                    } else {
                        TimePickerDialog _timePicker = new TimePickerDialog(getContext(),
                                (_view1, _hourOfDay, _minute) -> {
                                    _chosedHour = _hourOfDay;
                                    _chosedMinute = _minute;
                                    _choosedDate.set(Calendar.HOUR_OF_DAY, _chosedHour);
                                    _choosedDate.set(Calendar.MINUTE, _chosedMinute);

                                    if(_choosedDate.getTimeInMillis() < _currentDate.getTimeInMillis()) {
                                        Toast _toast = Toast.makeText(getContext(),
                                                "Введите корректное время", Toast.LENGTH_SHORT);
                                        _toast.show();
                                    } else {
                                        NotificationWorker(_currentDate);
                                    }
                                }, _currentHour, _currentMinute, true);
                        _timePicker.show();
                    }
                }, _currentYear, _currentMonth, _currentDay);
        _datePickerDialog.show();
    }

    private void InitCurrentOptions(Calendar _currentDate) {
        _currentYear = _currentDate.get(Calendar.YEAR);
        _currentMonth = _currentDate.get(Calendar.MONTH);
        _currentDay = _currentDate.get(Calendar.DAY_OF_MONTH);
        _currentHour = _currentDate.get(Calendar.HOUR_OF_DAY);
        _currentMinute = _currentDate.get(Calendar.MINUTE);
    }

    private long CalculateDelay(Calendar _currentDate) {
        Calendar _choosedTime = Calendar.getInstance();
        _choosedTime.set(Calendar.YEAR, _chosedYear);
        _choosedTime.set(Calendar.MONTH, _chosedMonth);
        _choosedTime.set(Calendar.DAY_OF_MONTH, _chosedDay);
        _choosedTime.set(Calendar.HOUR_OF_DAY, _chosedHour);
        _choosedTime.set(Calendar.MINUTE, _chosedMinute);
        return _choosedTime.getTimeInMillis() - _currentDate.getTimeInMillis();
    }
    private void NotificationWorker(Calendar _currentDate) {
        String _title = _binding.editTextTitleNotification.getText().toString();
        String _description = _binding.multilineDescriptionNotification.getText().toString();
        if(_title.isEmpty()) _title = "НЕТ НАЗВАНИЯ";
        if(_description.isEmpty()) _description = "НЕТ ОПИСАНИЯ";

        Data _dateTimeData = new Data.Builder().putString("Title", _title).
                putString("Description", _description).build();

        OneTimeWorkRequest _oneTimeWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInputData(_dateTimeData)
                .setInitialDelay(CalculateDelay(_currentDate), TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(getContext()).enqueue(_oneTimeWorkRequest);
    }
}