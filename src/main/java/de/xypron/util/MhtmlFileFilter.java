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

package de.xypron.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * File filter for MIME HTML files.
 * @author Heinrich Schuchardt
 */
public final class MhtmlFileFilter
        extends FileFilter {

    /**
     * String resource helper.
     */
    private IdeText ic;
    /**
     * File filter
     */
    private static FileFilter fileFilter;

    /**
     * Constructor for singleton.
     */
    private MhtmlFileFilter() {
        ic = IdeText.getIdeText();
    }

    /**
     * Gets file filter.
     * @return file filter
     */
    public static FileFilter getFileFilter() {
        if (fileFilter == null) {
            fileFilter = new MhtmlFileFilter();
        }
        return fileFilter;
    }

    @Override
    public boolean accept(final File f) {
        return f.getName().toLowerCase().endsWith(".mhtml")
                || f.isDirectory();
    }

    @Override
    public String getDescription() {
        return ic.getText(MhtmlFileFilter.class,
                "MhtmlFileFilter.Mhtml");
    }
}

