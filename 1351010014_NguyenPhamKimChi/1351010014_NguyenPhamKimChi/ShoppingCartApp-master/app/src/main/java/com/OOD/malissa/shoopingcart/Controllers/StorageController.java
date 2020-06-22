package com.OOD.malissa.shoopingcart.Controllers;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public final class StorageController {

    private StorageController() {
    }


    public static void writeObject(Context context, File key, Object object) throws IOException {

        FileOutputStream fileout = new FileOutputStream(key);
        ObjectOutputStream oos = new ObjectOutputStream(fileout);
        oos.writeObject(object);
        oos.close();
        fileout.close();
    }

    public static Object readObject(Context context, File key) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = new FileInputStream(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }
}
