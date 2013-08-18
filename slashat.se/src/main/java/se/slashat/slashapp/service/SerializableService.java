package se.slashat.slashapp.service;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import se.slashat.slashapp.MainActivity;

/**
 * Created by nicklas on 8/18/13.
 */
public abstract class SerializableService<T> {


    public abstract String getFilename();


    protected void save(T t) {

        ObjectOutputStream objectOutputStream = null;
        try {
            FileOutputStream fileOutputStream = MainActivity.getContext().openFileOutput(getFilename(), Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(t);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected T load() {
        ObjectInputStream objectInputStream = null;
        T data = null;
        try {
            FileInputStream fileInputStream = MainActivity.getContext().openFileInput(getFilename());
            objectInputStream = new ObjectInputStream(fileInputStream);
            data = (T) objectInputStream.readObject();
        } catch (FileNotFoundException e1) {
            Log.i(SerializableService.class.getName(), "No old episode list found on device. Will reparse the rss feed");
        } catch (IOException e) {
            Log.i(SerializableService.class.getName(), "No old episode list found on device. Will reparse the rss feed");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
}
