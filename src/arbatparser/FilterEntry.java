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

/**
 * Filter entry object which represents single rule of Arbat filter file.
 * @author s.nepochatov
 */
public class FilterEntry {
    
    /**
     * Entry rule class.
     */
    public static class EntryRule {
        
        /**
         * Default constructor.
         * @param rawFilter string form of filter;
         */
        public EntryRule(String rawFilter) throws Exception {
            char tempType = rawFilter.charAt(4);
            switch (tempType) {
                case 'D':
                    this.currRuleType = RuleTypes.DIRECTORY;
                    break;
                case 'T':
                    this.currRuleType = RuleTypes.TEXT;
                    break;
                default:
                    throw new Exception("Невідомий тип (" + tempType + ") < " + rawFilter);
            }
            this.ruleFilter = rawFilter.substring(5).trim();
        }
        
        /**
        * Type of filter rule.
        */
        public static enum RuleTypes {

            /**
            * Filter by using user defined text (any words or expression).
            */
            TEXT,

            /**
            * Filter by using defined keyword (usually directory but it may be also disk or e-mail).
            */
            DIRECTORY
        };
        
        /**
         * Current type of rule.
         */
        private RuleTypes currRuleType;
        
        /**
         * String representaion of filter rule.
         */
        private String ruleFilter;
        
        /**
         * Get current type of rule.
         * @return the currRuleType
         */
        public RuleTypes getCurrRuleType() {
            return currRuleType;
        }

        /**
         * Set current type of rule.
         * @param currRuleType the currRuleType to set
         */
        public void setCurrRuleType(RuleTypes currRuleType) {
            this.currRuleType = currRuleType;
        }

        /**
         * Get text of filter.
         * @return the ruleFilter
         */
        public String getRuleFilter() {
            return ruleFilter;
        }

        /**
         * Set text of filter.
         * @param ruleFilter the ruleFilter to set
         */
        public void setRuleFilter(String ruleFilter) {
            this.ruleFilter = ruleFilter;
        }
        
        /**
         * Get filter formated as Arbat config code.
         * @param prefix number of entry;
         * @return formated string;
         */
        public String getCode(String prefix) {
            return prefix + " " + (this.currRuleType.equals(RuleTypes.DIRECTORY) ? "D" : "T") + " " + this.ruleFilter + "\r\n";
        }
        
    }
    
    /**
     * Default flags for created entry.
     */
    private static String defaultRuleFlags = "-nnn";
    
    /**
     * Name of the entry.
     */
    private String entryName;
    
    /**
     * Flag of the entry (signal, print and etc).<br>
     * <br>
     * <b>Parser doesn't edit flags yet.</b>
     */
    private String entryFlags;
    
    /**
     * List of rules for this entry.
     */
    private List<EntryRule> ruleList;
    
    /**
     * Read corruption flag.
     */
    private Boolean isCorrupted = false;
    
    /**
     * Default constructor.
     * @param header header of entry (first line);
     * @param rulesArr array with rule strings (all other lines with same number); 
     */
    public FilterEntry(String header, String[] rulesArr) {
        this.entryName = header.substring(11).trim();
        this.entryFlags = header.substring(5, 11).trim();
        if (rulesArr != null) {
            this.ruleList = new ArrayList<EntryRule>(rulesArr.length);
            for (String currFilter: rulesArr) {
                EntryRule newRule = null;
                try {
                    newRule = new EntryRule(currFilter);
                    this.ruleList.add(newRule);
                } catch (Exception ex) {
                    System.out.println(currFilter);
                    System.out.println(Arrays.toString(rulesArr));
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Get name of this entry.
     * @return the entryName
     */
    public String getEntryName() {
        return entryName;
    }

    /**
     * Set name to this entry.
     * @param entryName the entryName to set
     */
    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    /**
     * Get etries flags.
     * @return the entryFlags
     */
    public String getEntryFlags() {
        return entryFlags;
    }

    /**
     * Set flags of this entry.
     * @param entryFlags the entryFlags to set
     */
    public void setEntryFlags(String entryFlags) {
        this.entryFlags = entryFlags;
    }

    /**
     * Get list of rules.
     * @return the ruleList
     */
    public List<EntryRule> getRuleList() {
        return ruleList;
    }

    /**
     * Set rules for this entry.
     * @param ruleList the ruleList to set
     */
    public void setRuleList(List<EntryRule> ruleList) {
        this.ruleList = ruleList;
    }

    /**
     * Get corruption flag.
     * @return the isCorrupted
     */
    public Boolean іsCorrupted() {
        return isCorrupted;
    }
    
    /**
     * Get text for UI list.
     * @return string for list entry;
     */
    public String getText() {
        if (this.isCorrupted) {
            return "<!> " + this.entryName;
        } else {
            return this.entryName;
        }
    }
    
    /**
     * Get entry formated for Arbat config file.
     * @param prefix number of entry;
     * @return formated string;
     */
    public String getCode(String prefix) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(prefix);
        strBuf.append(" N ");
        strBuf.append(this.entryFlags);
        if (this.entryFlags.length() < 4) {
            strBuf.append("  ");
        }
        strBuf.append("   ");
        strBuf.append(this.entryName);
        strBuf.append("\r\n");
        if (this.ruleList != null) {
            for (EntryRule currRule: this.ruleList) {
                strBuf.append(currRule.getCode(prefix));
            }
        }
        return strBuf.toString();
    }
}
