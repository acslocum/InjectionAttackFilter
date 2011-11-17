/**
Copyright (c) 2011 Andrew C Slocum

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
**/

package com.slocumbrau.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class InjectionAttackFilter extends HttpServletRequestWrapper {
  private static final String EVENTS = "((?i)onload|onunload|onchange|onsubmit|onreset" + "|onselect|onblur|onfocus|onkeydown|onkeypress|onkeyup"
      + "|onclick|ondblclick|onmousedown|onmousemove|onmouseout|onmouseover|onmouseup)";
  private static final String XSS_HTML_TAG = "\\n\\r|(%3C)|(%3E)|[<>]+";
  private static final String XSS_INJECTION = "(%22|')(\\s|%20)\\w.*|(\\s|%20)" + EVENTS + ".*|(%3D)|(%7C)";
  private static final String XSS_REGEX = XSS_HTML_TAG + "|" + XSS_INJECTION;
  private static final String SQL_REGEX = "('.+--)|(--)|(\\|)|(%7C)";

  public InjectionAttackFilter(HttpServletRequest request) {
    super(request);
  }

  @Override
  public String getParameter(String name) {
    String value = super.getParameter(name);
    return filterParamString(value);
  }

  @Override
  public Map<String, String[]> getParameterMap() {
    Map<String, String[]> rawMap = super.getParameterMap();
    Map<String, String[]> filteredMap = new HashMap<String, String[]>(rawMap.size());
    Set<String> keys = rawMap.keySet();
    for (String key : keys) {
      String[] rawValue = rawMap.get(key);
      String[] filteredValue = filterStringArray(rawValue);
      filteredMap.put(key, filteredValue);
    }
    return filteredMap;
  }

  protected String[] filterStringArray(String[] rawValue) {
    String[] filteredArray = new String[rawValue.length];
    for (int i = 0; i < rawValue.length; i++) {
      filteredArray[i] = filterParamString(rawValue[i]);
    }
    return filteredArray;
  }

  @Override
  public String[] getParameterValues(String name) {
    String[] rawValues = super.getParameterValues(name);
    if (rawValues == null)
      return null;
    String[] filteredValues = new String[rawValues.length];
    for (int i = 0; i < rawValues.length; i++) {
      filteredValues[i] = filterParamString(rawValues[i]);
    }
    return filteredValues;
  }

  protected String filterParamString(String rawValue) {
    if (rawValue == null) {
      return null;
    }
	if(filterXSS()) {
		rawValue = rawValue.replaceAll(XSS_REGEX, "");
	}
	if(filterSQL()) {
		rawValue = rawValue.replaceAll(SQL_REGEX, "");
	}
    return rawValue;
  }

  @Override
  public String getQueryString() {
    return filterParamString(super.getQueryString());
  }

  protected boolean filterXSS() {
	return true;
  }

  protected boolean filterSQL() {
	return true;
  }

}
