/*
Copyright 2019 B. Wieser

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation 
files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, 
modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the 
Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 * User: Bernie Wieser
 * Date: Dec 14, 2005
 * Time: 4:28:01 PM
 */
package com.omdi.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class CSVParser implements Enumeration {
	private List list = new ArrayList();
	private int idx = 0;

	public CSVParser(String str) throws ParseException {
		StringBuffer sb = new StringBuffer();
		int state = 1;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			switch (state) {
				case 1:
					{
						if (c == '"')
							state = 2;
						else if (c == ',')
							state = 4;
						else
							sb.append(c);
					}
					break;
				case 2:
					{
						if (c == '"')
							state = 3;
						else
							sb.append(c);
					}
					break;
				case 3:
					{
						if (c == ',')
							state = 4;
						else if (c == '"') {   // "" escape
							sb.append('"');
							state = 2;
						} else
							throw new ParseException("long string", i);
					}
					break;
				case 4:
					{
						list.add(sb.toString().trim());
						sb.setLength(0);
						if (c == '"')
							state = 2;
						else if (c != ',') {
							state = 1;
							sb.append(c);
						}
					}
					break;
			}
		}

		switch (state) {
			case 1: // last
				list.add(sb.toString().trim());
				break;
			case 2:
				throw new ParseException("unbounded string", 2);
			case 3: // end quoted
				list.add(sb.toString().trim());
				break;
			case 4: // last is empty
				list.add(sb.toString().trim());
				list.add("");
				break;
		}

	}

	public boolean hasMoreElements() {
		return idx < list.size();
	}

	public Object nextElement() {
		return list.get(idx++);
	}

	public String nextToken() {
		return (String) list.get(idx++);
	}
}
