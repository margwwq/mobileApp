package ru.mirea.mikhaylenkoma.mireaproject.ui.les6;

import android.app.Activity;
import android.app.AlertDialog;

import java.util.Locale;

public class AlertDialogs {
    static void showAlertErrorSave(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity).
                setTitle("Ошибка сохранения файла")
                .setMessage("Пожалуйста введите название файла")
                .setPositiveButton("OK", null);
        builder.show();
    }

    static void showAlertSave(Activity activity, String filename, String messageCrypted) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity).setTitle("Сохранение файла")
                .setMessage(String.format(Locale.getDefault(),
                        "Файл %s успешно сохранен\nДанные зашифрованы: %s",
                        filename, messageCrypted))
                .setPositiveButton("OK", null);
        builder.show();
    }

    static void showAlertEmptyFilenameToRead(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle("Ошибка открытия файлы")
                .setMessage("Пожалуйста, введите название файла")
                .setPositiveButton("OK", null);
        builder.show();
    }

    static void showAlertFileNotExist(Activity activity, String filename) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle("Ошибка открытия файла")
                .setMessage(String.format(Locale.getDefault(), "Файла %s не существует, " +
                        "пожалуйста, введите корректное название файла", filename))
                .setPositiveButton("OK", null);
        builder.show();
    }

    static void showAlertFileContent(Activity activity, String filename,  String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle("Открытие файла")
                .setMessage(String.format(Locale.getDefault(),
                        "Содержимое файла %s:\n%s",
                        filename, message))
                .setPositiveButton("OK", null);
        builder.show();
    }
}
