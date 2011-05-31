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
package de.xypron.ui.components;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * The output panel is used to display System.out and System.err.
 */
@SuppressWarnings("serial")
public class IdeOutput extends IdePanel {
    /**
     * Preferred width of text area.
     */
    private static final int TEXTAREA_WIDTH = 40;
    /**
     * Preferred height of text area.
     */
    private static final int TEXTAREA_HEIGHT = 5;

    /**
     * Scroll pane.
     */
    private JScrollPane jScrollPane = null;
    /**
     * Text area.
     */
    private JTextArea jTextArea = null;

    /**
     * Constructor for panel.
     */
    public IdeOutput() {
        super();
        this.add(getJScrollPane(), BorderLayout.CENTER);
    }

    /**
     * Get scroll pane.
     * @return scroll pane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            getJTextArea().setRows(TEXTAREA_HEIGHT);
            getJTextArea().setColumns(TEXTAREA_WIDTH);
            jScrollPane.setViewportView(getJTextArea());
        }
        return jScrollPane;
    }

    /**
     * Return text area for trace output.
     * @return text area
     */
    private JTextArea getJTextArea() {
        if (jTextArea == null) {
            jTextArea = new JTextArea();
            redirectSystemStreams();
        }
        return jTextArea;
    }

    /**
     * Add a string to the text area.
     * @param text string
     */
    private void append(final String text) {
        SwingUtilities.invokeLater(
                new Runnable() {

                    @Override
                    public void run() {
                        jTextArea.append(text);
                    }
                });
    }

    /**
     * Redirect the system streams.
     */
    private void redirectSystemStreams() {
        final PrintStream serr = System.err;
        final OutputStream err;
        final OutputStream out;

        out = new OutputStream() {

            @Override
            public void write(final int b) throws IOException {
                append(String.valueOf((char) b));
            }

            @Override
            public void write(final byte[] b, final int off, final int len)
                    throws IOException {
                append(new String(b, off, len));
            }

            @Override
            public void write(final byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        err = new OutputStream() {

            @Override
            public void write(final int b) throws IOException {
                out.write(b);
                serr.write(b);
            }

            @Override
            public void write(final byte[] b, final int off, final int len)
                    throws IOException {
                out.write(b, off, len);
                serr.write(b, off, len);
            }

            @Override
            public void write(final byte[] b) throws IOException {
                out.write(b);
                serr.write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(err, true));
    }
}
