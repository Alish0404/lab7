package com.example.lab7;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 100;
    private TextView tvFileContent;
    private Button btnWrite, btnRead, btnDelete, btnInfo;
    private static final String FILE_NAME = "app_data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        initViews();
        setupButtons();
        checkStoragePermission();
    }

    private void initViews() {
        tvFileContent = findViewById(R.id.tv_file_content);
        btnWrite = findViewById(R.id.btn_write);
        btnRead = findViewById(R.id.btn_read);
        btnDelete = findViewById(R.id.btn_delete);
        btnInfo = findViewById(R.id.btn_info);
    }

    private void setupButtons() {
        btnWrite.setOnClickListener(v -> {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            String dataToSave = "Запись от: " + currentTime + "\nТестовые данные";
            writeFile(dataToSave);
        });

        btnRead.setOnClickListener(v -> {
            waitForSomeTime(1000); // Ожидание 1 секунду перед чтением
            String fileContent = readFile();
            tvFileContent.setText(fileContent.isEmpty() ? "Файл пуст или не существует" : fileContent);
        });

        btnDelete.setOnClickListener(v -> deleteFile());
        btnInfo.setOnClickListener(v -> showFileInfo());
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) return;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showToast("Разрешения получены");
        } else {
            showToast("Разрешения необходимы для работы с файлами");
        }
    }

    private void writeFile(String text) {
        try (FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE)) {
            fos.write(text.getBytes());
            showToast("Данные успешно сохранены в файл");
        } catch (IOException e) {
            showToast("Ошибка при записи файла");
            e.printStackTrace();
        }
    }

    private String readFile() {
        StringBuilder content = new StringBuilder();
        try (FileInputStream fis = openFileInput(FILE_NAME);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            showToast("Ошибка при чтении файла");
            e.printStackTrace();
        }
        return content.toString().trim();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean fileExists() {
        File file = getFileStreamPath(FILE_NAME);
        return file.exists() && file.length() > 0;
    }

    private void deleteFile() {
        File file = getFileStreamPath(FILE_NAME);
        if (file.exists() && file.delete()) {
            showToast("Файл удален");
            tvFileContent.setText("");
        } else {
            showToast("Файл не существует");
        }
    }

    private void showFileInfo() {
        File file = getFileStreamPath(FILE_NAME);
        if (file.exists()) {
            String info = "Имя: " + FILE_NAME + "\n" +
                    "Размер: " + file.length() + " байт\n" +
                    "Последнее изменение: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(file.lastModified()));
            new AlertDialog.Builder(this)
                    .setTitle("Информация о файле")
                    .setMessage(info)
                    .setPositiveButton("OK", null)
                    .show();
        } else {
            showToast("Файл не существует");
        }
    }

    private void waitForSomeTime(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
