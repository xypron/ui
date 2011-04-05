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

//org.apache.commons.codec.binary.Base64;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JComponent;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Class to create mhtml files that can be read by Excel
 */
public class Mhtml {

    int image = 0;
    /**
     * the worksheets
     */
    private TreeMap<String, Iterable<?>> sheets;

    /**
     * Constructor
     */
    public Mhtml() {
        sheets = new TreeMap<String, Iterable<?>>();
    }

    /**
     * Add a sheet to the workbook. If a sheet with the same title already
     * exists it is replaced.<br />
     * Typically the method will be called with
     * <code>Iterable<Iterable<Object>></code>, e.g.
     * <code>ArrayList<ArrayList<Object>></code>. The outer
     * <code>Iterable</code> iterates over the lines, The inner
     * <code>Iterable</code> over the columns.
     * If the 2nd level object is not an <code>Iterable</code>
     * the line has only one column. If the objects are
     * instances of Double, Float, Long, or Integer the cell numeric values
     * will be set. Otherwise only the toString() value of the object will be
     * written.
     * @param title sheet title
     * @param values cells of the worksheet
     */
    public void addSheet(String title, Iterable<?> values) {
        sheets.put(title, values);
    }

    /**
     * Get cells of a worksheet
     * @param title title of the worksheet
     * @return cells of the worksheet
     */
    public Iterable<?> getValues(String title) {
        return sheets.get(title);
    }

    /**
     * Write a mhtml file
     * @param filename file name
     * @throws IOException
     * @throws MessagingException
     * @throws XMLStreamException
     */
    public void write(String filename) throws IOException, MessagingException,
            XMLStreamException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            write(out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * Write a mhtml file
     * @param out
     * @throws IOException
     * @throws MessagingException
     * @throws XMLStreamException
     */
    public void write(FileOutputStream out) throws IOException,
            MessagingException,
            XMLStreamException {
        int i = 1;
        MimeMessage msg = new MimeMessage(Session.getInstance(new Properties()));
        MimeMultipart m = new MimeMultipart();
        MimeBodyPart p;

        p = new MimeBodyPart();
        p.setText(book(), "utf-8");
        p.addHeader("Content-Location", "file:///Z:/workbook.htm");
        p.addHeader("Content-Transfer-Encoding", "quoted-printable");
        p.addHeader("Content-Type", "text/html; charset=\"utf-8\"");
        m.addBodyPart(p);
        for (Entry<String, Iterable<?>> entry : sheets.entrySet()) {
            p = new MimeBodyPart();
            p.setText(sheet(m, entry.getValue()), "utf-8");
            p.addHeader("Content-Location", "file:///Z:/" + i++ + ".htm");
            p.addHeader("Content-Transfer-Encoding", "quoted-printable");
            p.addHeader("Content-Type", "text/html; charset=\"utf-8\"");
            m.addBodyPart(p);
        }

        msg.setContent(m);
        msg.setHeader("MIME-Version", "1.0");
        msg.setHeader("X-Document-Type", "Workbook");
        msg.writeTo(out);
    }

    /**
     * Create workbook
     * @return workbook in xml format
     * @throws XMLStreamException
     */
    private String book() throws XMLStreamException {
        int i = 1;
        String ret = "";
        StringWriter s = new StringWriter();
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter w = factory.createXMLStreamWriter(s);

        w.writeStartDocument();
        w.writeStartElement("html");
        w.writeAttribute("xmlns:o", "urn:schemas-microsoft-com:office:office");
        w.writeAttribute("xmlns:x", "urn:schemas-microsoft-com:office:excel");
        w.writeAttribute("xmlns", "http://www.w3.org/TR/REC-html40");
        w.writeStartElement("head");
        w.writeStartElement("meta");
        w.writeAttribute("name", "Excel Workbook Frameset");
        w.writeEndElement(); //meta
        w.writeStartElement("meta");
        w.writeAttribute("http-equiv",
                "Content-Type content=text/html; charset=utf-8");
        w.writeEndElement(); //meta
        w.writeStartElement("meta");
        w.writeAttribute("name", "ProgId");
        w.writeAttribute("content", "Excel.Sheet");
        w.writeEndElement(); //meta
        w.writeStartElement("xml");

        w.writeStartElement("x:ExcelWorkbook");
        w.writeStartElement("x:ExcelWorksheets");
        for (Entry<String, Iterable<?>> entry : sheets.entrySet()) {
            w.writeStartElement("x:ExcelWorksheet");
            w.writeStartElement("x:Name");
            w.writeCharacters(entry.getKey());
            w.writeEndElement(); // x:Name
            w.writeStartElement("x:WorksheetSource");
            w.writeAttribute("HRef", i++ + ".htm");
            w.writeEndElement(); // x:ExcelWorksheetSource
            w.writeEndElement(); // x:ExcelWorksheet
        }
        w.writeEndElement(); // x:ExcelWorksheets
        w.writeStartElement("x:ProtectStructure");
        w.writeCharacters("False");
        w.writeEndElement(); //x:ProtectStructure
        w.writeStartElement("x:ProtectWindows");
        w.writeCharacters("False");
        w.writeEndElement(); //x:ProtectWindows
        w.writeEndElement(); // x:ExcelWorkbook
        w.writeEndElement(); //xml
        w.writeEndElement(); //head
        w.writeStartElement("frameset");
        w.writeStartElement("frame");
        w.writeAttribute("src", "1.htm");
        w.writeEndElement(); //frame
        w.writeEndElement(); //frameset
        w.writeEndElement(); //html
        w.writeEndDocument();
        w.close();
        ret = s.toString();
        return ret;
    }

    /**
     * Write worksheet
     * @param values cells of the worksheet
     * @return worksheet as xml
     * @throws XMLStreamException
     */
    private String sheet(MimeMultipart m, Iterable<?> values)
            throws XMLStreamException {
        StringWriter s = new StringWriter();
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter w = factory.createXMLStreamWriter(s);
        String ret = "";

        w.writeStartDocument();
        w.writeStartElement("html");
        w.writeAttribute("xmlns:o", "urn:schemas-microsoft-com:office:office");
        w.writeAttribute("xmlns:x", "urn:schemas-microsoft-com:office:excel");
        w.writeAttribute("xmlns", "http://www.w3.org/TR/REC-html40");
        w.writeStartElement("head");
        w.writeStartElement("meta");
        w.writeAttribute("http-equiv",
                "Content-Type content=text/html; charset=utf-8");
        w.writeEndElement(); //meta
        w.writeStartElement("meta");
        w.writeAttribute("name", "ProgId");
        w.writeEndElement(); //meta
        w.writeStartElement("meta");
        w.writeAttribute("content", "Excel.Sheet");
        w.writeEndElement(); //meta
        w.writeStartElement("xml");
        w.writeStartElement("x:WorksheetOptions");
        w.writeStartElement("x:ProtectContents");
        w.writeCharacters("False");
        w.writeEndElement(); //x:ProtectContents
        w.writeStartElement("x:ProtectObjects");
        w.writeCharacters("False");
        w.writeEndElement(); //x:ProtectObjects
        w.writeStartElement("x:ProtectScenarios");
        w.writeCharacters("False");
        w.writeEndElement(); //x:ProtectScenarios
        w.writeEndElement(); //x:WorksheetOptions
        w.writeEndElement(); //xml
        w.writeEndElement(); //head
        w.writeStartElement("body");
        w.writeStartElement("table");
        for (Object line : values) {
            w.writeStartElement("tr");
            if (line instanceof Iterable<?>) {
                for (Object obj : (Iterable<?>) line) {
                    writeElement(m, w, obj);
                }
            } else {
                writeElement(m, w, line);
            }
            w.writeEndElement(); //tr
        }
        w.writeEndElement(); //table
        w.writeEndElement(); //body
        w.writeEndElement(); //html
        w.writeEndDocument();
        w.close();
        ret = s.toString();
        return ret;
    }

    private void writeElement(MimeMultipart m, XMLStreamWriter w, Object obj)
            throws XMLStreamException {

        w.writeStartElement("td");
        if (obj instanceof JComponent) {
            JComponent component;
            component = (JComponent) obj;
            String filename = "image" + ++image + ".png";
            try {
                m.addBodyPart(image(component, filename));
                w.writeStartElement("img");
                w.writeAttribute("src", filename);
                w.writeAttribute("alt", component.getClass().getName());
                w.writeEndElement();
            } catch (MessagingException ex) {
                Logger.getLogger(Mhtml.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Mhtml.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (obj instanceof Double
                    || obj instanceof Float
                    || obj instanceof Integer
                    || obj instanceof Long) {
                w.writeAttribute("x:num", obj.toString());
            }
            w.writeCharacters(obj.toString());
        }
        w.writeEndElement(); //td
    }

    private MimeBodyPart image(JComponent component, String filename)
            throws MessagingException, IOException {
        MimeBodyPart p;
        BufferedImage bufferedImage;
        ByteArrayOutputStream byteStream;
        byte[] byteBuffer;
        int width, height;

        width = component.getPreferredSize().width;
        height = component.getPreferredSize().height;
        if (width == 0) {
            width = 32;
        }
        if (height == 0) {
            height = 32;
        }
        component.setSize(new Dimension(width,height));
        bufferedImage = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_RGB);
        component.paint(bufferedImage.getGraphics());
        byteStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteStream);
        byteStream.flush();
        byteBuffer = byteStream.toByteArray();
        p=new MimeBodyPart(new InternetHeaders(), byteBuffer);
        p.addHeader("Content-Location", "file:///Z:/" + filename);
        p.setContent(byteBuffer, "image/jpeg");
        p.addHeader("Content-Type", "image/png");
        return p;
    }
}
