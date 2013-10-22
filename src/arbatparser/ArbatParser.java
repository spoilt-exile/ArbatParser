/**
* This file is part of ArbatParser application (check README).
* Copyright (C) 2012-2013 Stanislav Nepochatov
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
**/

package arbatparser;

import java.util.*;
import java.io.*;
import javax.swing.JOptionPane;
import java.util.concurrent.*;

/**
 * Main class.
 * @author Stanislav Nepochatov
 */
public class ArbatParser {
    
    /**
     * Main app window.
     */
    public static MainFrame window = new MainFrame();
    
    /**
     * Arbat config file system header (line with 000 id).
     */
    private static String systemHeader = "";
    
    /**
     * Filter of lists.
     */
    public static List<FilterEntry> filterList = new ArrayList<FilterEntry>();
    
    /**
     * Single thread executor for parsing.
     */
    public static ExecutorService executor;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        window.setVisible(true);
        Runnable parseRunner = new Runnable() {

            @Override
            public void run() {
                parseSetFile();
            }
            
        };
        executor = Executors.newSingleThreadExecutor();
        executor.execute(parseRunner);
    }
    
    /**
     * Parse Arbat set file in current directory.
     */
    public static void parseSetFile() {
        File setFile = new File("FindBik.Set");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(setFile), "Cp1251"));
            String header = null;
            StringBuffer filterBuf = new StringBuffer();
            Boolean dirtyRead = false;
            while (reader.ready()) {
                String currLine = reader.readLine();
                if (currLine.substring(0, 3).equals("000")) {
                    systemHeader += currLine + "\r\n";
                    continue;
                } else {
                    if (header == null) {
                        header = currLine;
                        continue;
                    } else if (header.substring(0, 4).equals(currLine.substring(0, 4))) {
                        filterBuf.append(currLine);
                        filterBuf.append("\r\n");
                    } else {
                        FilterEntry newEntry = null;
                        if (filterBuf.length() == 0) {
                            newEntry = new FilterEntry(header, null);
                        } else {
                            newEntry = new FilterEntry(header, filterBuf.toString().split("\r\n"));
                            if (newEntry.іsCorrupted()) {
                                dirtyRead = true;
                            }
                        }
                        filterList.add(newEntry);
                        window.addToList(newEntry.getText());
                        header = currLine;
                        filterBuf = new StringBuffer();
                    }
                }
            }
            if (header != null) {
                FilterEntry newEntry = null;
                if (filterBuf.length() == 0) {
                    newEntry = new FilterEntry(header, null);
                } else {
                    newEntry = new FilterEntry(header, filterBuf.toString().split("\r\n"));
                    if (newEntry.іsCorrupted()) {
                        dirtyRead = true;
                    }
                }
                filterList.add(newEntry);
                window.addToList(newEntry.getText());
            }
            window.displayList();
            if (dirtyRead) {
                JOptionPane.showMessageDialog(null, "Файл прочитано з пошкодженими даними, редагування обмежене!", "Увага!", JOptionPane.WARNING_MESSAGE);
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Файлу налаштувань 'FindBik.Set' не знайдено!", "Помилка!", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Файлу налаштувань 'FindBik.Set' неможливо прочитати!", "Помилка!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Файл налаштувань 'FindBik.Set' неможливо закрити!", "Помилка!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    /**
     * Save Arbat set file with current statel
     */
    public static void saveSetFile() {
        BufferedWriter writer = null;
        Boolean dirtySave = false;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("FindBik.Set")), "Cp1251"));
            writer.write(systemHeader);
            for (int index = 0; index < filterList.size(); index++) {
                String currPrefix = String.valueOf(index + 1);
                while (currPrefix.length() < 3) {
                    currPrefix = "0" + currPrefix;
                }
                if (!filterList.get(index).іsCorrupted()) {
                    writer.write(ArbatParser.filterList.get(index).getCode(currPrefix));
                } else {
                    dirtySave = true;
                }
            }
            if (dirtySave) {
                JOptionPane.showMessageDialog(null, "Файл збережено з пошкодженими даними, робота файла не гарантується!", "Увага!", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Помилка запису до файлу 'FindBik.Set'!", "Помилка!", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Файл налаштувань 'FindBik.Set' неможливо закрити!", "Помилка!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Get header with system connection and interface configuration.
     * @return the systemHeader
     */
    public static String getSystemHeader() {
        return systemHeader;
    }
}
