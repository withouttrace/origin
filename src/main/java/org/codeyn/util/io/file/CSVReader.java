package org.codeyn.util.io.file;

/**
 * Copyright 2005 Bytecode Pty Ltd.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * 读取csv格式文件；
 * 标准格式如下：
 * 1.每条记录占一行
 * 2.以逗号为分隔符
 * 3.逗号前后的空格会被忽略
 * 4.字段中包含有逗号，该字段必须用双引号括起来
 * 5.字段中包含有换行符，该字段必须用双引号括起来
 * 6.字段前后包含有空格，该字段必须用双引号括起来
 * 7.字段中的双引号用两个双引号表示
 * 8.字段中如果有双引号，该字段必须用双引号括起来
 * 9.第一条记录，可以是字段名
 * <p>
 * 这里进行了扩展，分隔符(separator)可以指定，双引号也可以用指定的字符(quote)替换；
 * 如果有空行，或者全是空格、不可见字符等，不忽略该行；
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * A very simple CSV reader released under a commercial-friendly license.
 *
 * @author Glen Smith
 *
 */
public class CSVReader {

    /** The default separator to use if none is supplied to the constructor. */
    public static final char DEFAULT_SEPARATOR = ',';
    /**
     * The default quote character to use if none is supplied to the
     * constructor.
     */
    public static final char DEFAULT_QUOTE_CHARACTER = '"';
    /**
     * The default line to start reading.
     */
    public static final int DEFAULT_SKIP_LINES = 0;
    private BufferedReader br;
    private boolean hasNext = true;
    private char separator;
    private char quotechar;
    private int skipLines;
    private boolean linesSkiped;

    /**
     * Constructs CSVReader using a comma for the separator.
     *
     * @param reader
     *            the reader to an underlying CSV source.
     */
    public CSVReader(Reader reader) {
        this(reader, DEFAULT_SEPARATOR);
    }

    /**
     * Constructs CSVReader with supplied separator.
     *
     * @param reader
     *            the reader to an underlying CSV source.
     * @param separator
     *            the delimiter to use for separating entries.
     */
    public CSVReader(Reader reader, char separator) {
        this(reader, separator, DEFAULT_QUOTE_CHARACTER);
    }

    /**
     * Constructs CSVReader with supplied separator and quote char.
     *
     * @param reader
     *            the reader to an underlying CSV source.
     * @param separator
     *            the delimiter to use for separating entries
     * @param quotechar
     *            the character to use for quoted elements
     */
    public CSVReader(Reader reader, char separator, char quotechar) {
        this(reader, separator, quotechar, DEFAULT_SKIP_LINES);
    }

    /**
     * Constructs CSVReader with supplied separator and quote char.
     *
     * @param reader
     *            the reader to an underlying CSV source.
     * @param separator
     *            the delimiter to use for separating entries
     * @param quotechar
     *            the character to use for quoted elements
     * @param line
     *            the line number to skip for start reading
     */
    public CSVReader(Reader reader, char separator, char quotechar, int line) {
        this.br = reader instanceof BufferedReader ? (BufferedReader) reader
                : new BufferedReader(reader);
        this.separator = separator;
        this.quotechar = quotechar;
        this.skipLines = line;
    }

    /**
     * Reads the entire file into a List with each element being a String[] of
     * tokens.
     *
     * @return a List of String[], with each String[] representing a line of the
     *         file.
     *
     * @throws IOException
     *             if bad things happen during the read
     */
    public List<String[]> readAll() throws IOException {
        List<String[]> allElements = new ArrayList<String[]>();
        while (hasNext) {
            String[] nextLineAsTokens = readNext();
            if (nextLineAsTokens != null) allElements.add(nextLineAsTokens);
        }
        return allElements;
    }

    /**
     * Reads the next line from the buffer and converts to a string array.
     *
     * @return a string array with each comma-separated element as a separate
     *         entry.
     *
     * @throws IOException
     *             if bad things happen during the read
     */
    public String[] readNext() throws IOException {

        String nextLine = getNextLine();
        return hasNext ? parseLine(nextLine) : null;
    }

    /**
     * Reads the next line from the file.
     *
     * @return the next line from the file without trailing newline
     * @throws IOException
     *             if bad things happen during the read
     */
    private String getNextLine() throws IOException {
        if (!this.linesSkiped) {
            for (int i = 0; i < skipLines; i++) {
                br.readLine();
            }
            this.linesSkiped = true;
        }
        String nextLine = br.readLine();
        if (nextLine == null) {
            hasNext = false;
        }
        return hasNext ? nextLine : null;
    }

    /**
     * Parses an incoming String and returns an array of elements.
     *
     * @param nextLine
     *            the string to parse
     * @return the comma-tokenized list of elements, or null if nextLine is null
     * @throws IOException
     *             if bad things happen during the read
     */
    private String[] parseLine(String nextLine) throws IOException {

        if (nextLine == null) {
            return null;
        }

        List<String> tokensOnThisLine = new ArrayList<String>();
        /**
         * 初始化长度；
         */
        StringBuffer sb = new StringBuffer(nextLine.length() + 5);
        boolean inQuotes = false;
        do {
            if (inQuotes) {
                // continuing a quoted section, reappend newline
                sb.append("\n");
                nextLine = getNextLine();
                if (nextLine == null) break;
            }
            for (int i = 0; i < nextLine.length(); i++) {

                char c = nextLine.charAt(i);
                if (c == quotechar) {
                    // this gets complex... the quote may end a quoted block, or
                    // escape another quote.
                    // do a 1-char lookahead:
                    if (inQuotes // we are in quotes, therefore there can be
                            // escaped quotes in here.
                            && nextLine.length() > (i + 1) // there is indeed
                            // another character
                            // to check.
                            && nextLine.charAt(i + 1) == quotechar) { // ..and
                        // that
                        // char.
                        // is a
                        // quote
                        // also.
                        // we have two quote chars in a row == one quote char,
                        // so consume them both and
                        // put one on the token. we do *not* exit the quoted
                        // text.
                        sb.append(nextLine.charAt(i + 1));
                        i++;
                    } else {
                        inQuotes = !inQuotes;
                        // the tricky case of an embedded quote in the middle:
                        // a,bc"d"ef,g
                        if (i > 2 // not on the begining of the line
                                && nextLine.charAt(i - 1) != this.separator // not
                                // at
                                // the
                                // begining
                                // of
                                // an
                                // escape
                                // sequence
                                && nextLine.length() > (i + 1)
                                && nextLine.charAt(i + 1) != this.separator // not
                            // at
                            // the
                            // end
                            // of
                            // an
                            // escape
                            // sequence
                        ) {
                            sb.append(c);
                        }
                    }
                } else if (c == separator && !inQuotes) {
                    tokensOnThisLine.add(sb.toString());
                    sb = new StringBuffer(); // start work on next token
                } else {
                    sb.append(c);
                }
            }
        } while (inQuotes);
        tokensOnThisLine.add(sb.toString());
        return tokensOnThisLine.toArray(new String[tokensOnThisLine
                .size()]);

    }

    /**
     * Closes the underlying reader.
     *
     * @throws IOException
     *             if the close fails
     */
    public void close() throws IOException {
        br.close();
    }

}
