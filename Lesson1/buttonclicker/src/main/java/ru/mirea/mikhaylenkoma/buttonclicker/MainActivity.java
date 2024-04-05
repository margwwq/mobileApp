package ru.mirea.mikhaylenkoma.buttonclicker;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView textViewStudent;
    private Button btnWhoAmI;
    private CheckBox checkBox;
    private Button btnItIsNotMe ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewStudent = findViewById(R.id.tvOut);
        btnWhoAmI = findViewById(R.id.btnWhoAmI);
        Button btnItIsNotMe = findViewById(R.id.btnItIsNotMe);
        checkBox = findViewById(R.id.checkBox);
        View.OnClickListener oclBtnWhoAmI = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewStudent.setText("Я Рита, если не ошибаюсь, мой номер по списку № 15");
            }
        };

        btnWhoAmI.setOnClickListener(oclBtnWhoAmI);
    }
    public void onButtonClick(View view) {
        checkBox.setChecked(!checkBox.isChecked());
        textViewStudent.setText("Человеческое большое спасибо за то, что проверяете эту работу");
    }

}