/*
 *  Copyright 2010 Heinrich Schuchardt.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package de.xypron.ui.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 */
public class UserProfile extends Properties
        implements Storable {

    /**
     * File separator (slash or backslash).
     */
    private static String fs = System.getProperty("file.separator");
    /**
     * File name.
     */
    private String filename;

    /**
     * Constructor.
     * @param cls class for which the userprofile shall be loaded
     *
     */
    public UserProfile(Class<?> cls) {
        init(cls);
    }

    /**
     * Load user profile from file.
     * @param cls 
     */
    private void init(Class<?> cls) {
        String path;
        File file;
        FileInputStream fis;
        String pkg;

        pkg = cls.getName().replace(".", fs);

        path = getPath() + fs + pkg;
        new File(path).mkdirs();

        filename = path + fs + "userprofile.properties";
        file = new File(filename);
        try {
            fis = new FileInputStream(file);
            super.load(fis);
            fis.close();
        } catch (IOException ex) {
        }
    }

    /**
     * Get filename of user properties
     * @return filename
     */
    public String getFilename() {
        return filename;
    }

    @Override
    public boolean exists() {
        File file;
        file = new File(filename);
        return file.exists();
    }

    @Override
    public boolean remove() {
        File file;
        file = new File(filename);
        try {
            file.delete();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Override
    public boolean store() {
        File file;
        FileOutputStream fos;
        file = new File(filename);
        try {
            fos = new FileOutputStream(file);
            super.store(fos, filename);
            fos.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    /**
     * Gets path to user settings file.
     * @return path to user settings file.
     */
    static String getPath() {
        String ret;

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            ret = System.getenv("APPDATA") + fs + "java";
        } else {
            ret = System.getProperty("user.home") + fs + ".java";
        }
        return ret;
    }
}
