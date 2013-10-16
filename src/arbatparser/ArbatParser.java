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
    private static void parseSetFile() {
        File setFile = new File("FindBik.Set");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(setFile), "Cp1251"));
            String header = null;
            StringBuffer filterBuf = new StringBuffer();
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
                }
                filterList.add(newEntry);
                window.addToList(newEntry.getText());
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Файлу налаштувань 'FindBik.Set' не знайдено!", "Помилка!", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Файлу налаштувань 'FindBik.Set' неможливо прочитати!", "Помилка!", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    /**
     * @return the systemHeader
     */
    public static String getSystemHeader() {
        return systemHeader;
    }
}
