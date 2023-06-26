package com.example.prm392_group3.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

public class ObjectStorageUtil {
    public static <T> void saveObject(Context context, String fileName, T object) {
        Gson gson = new Gson();
        Type objectType = new TypeToken<T>() {}.getType();
        String json = gson.toJson(object, objectType);

        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(json);
            outputStreamWriter.close();
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e("ObjectStorageUtil", "Error saving object: " + e.getMessage());
        }
    }

    public static <T> T loadObject(Context context, String fileName, Class<T> objectClass) {
        T object = null;
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            Gson gson = new Gson();
            object = gson.fromJson(stringBuilder.toString(), objectClass);
        } catch (IOException e) {
            Log.e("ObjectStorageUtil", "Error loading object: " + e.getMessage());
        }
        return object;
    }

    public static void deleteObject(Context context, String fileName) {
        context.deleteFile(fileName);
    }
}



//ObjectStorageUtil.saveObject(context, "filename", object);  // Gọi phương thức saveObject()
//
//        T loadedObject = ObjectStorageUtil.loadObject(context, "filename", objectClass);  // Gọi phương thức loadObject()
//
//        ObjectStorageUtil.deleteObject(context, "filename");  // Gọi phương thức deleteObject()

