package com.slocumbrau.filter;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EventHandlerInjectionTest {
  private InjectionAttackWrapper wrapper;
  private HttpServletRequest request;

  @Before
  public void setUp() throws Exception {
    request = Mockito.mock(HttpServletRequest.class);
    wrapper = new InjectionAttackWrapper(request);
  }

  @Test
  public void shouldNotFilterMessageWithAnOnInThemToBeMistakenAsEventHandlers() {
    Map<String, String[]> map = new HashMap<String, String[]>();
    map.put("param1", new String[] { "%20Christina Zhong" });
    map.put("param2", new String[] { "%20ona sunny day" });
    map.put("param3", new String[] { "%20on a rainy day" });
    map.put("param4", new String[] { "Then ona winter day" });

    Mockito.when(request.getParameterMap()).thenReturn(map);

    wrapper = new InjectionAttackWrapper(request);
    Map output = wrapper.getParameterMap();
    assertEquals("%20Christina Zhong", ((String[]) output.get("param1"))[0]);
    assertEquals("%20ona sunny day", ((String[]) output.get("param2"))[0]);
    assertEquals("%20on a rainy day", ((String[]) output.get("param3"))[0]);
    assertEquals("Then ona winter day", ((String[]) output.get("param4"))[0]);

  }

  @Test
  public void shouldFilterInjectedEventHandlers() {
    Map<String, String[]> map = new HashMap<String, String[]>();
    String injectedCodeWithHexspace = "%20onMouseOver=alert('today');";
    final String param1 = "param1";
    map.put(param1, new String[] { injectedCodeWithHexspace });

    String injectedCodeWithHexquote = "%22%20string";
    final String param2 = "param2";
    map.put(param2, new String[] { injectedCodeWithHexquote });

    String injectedCodeWithSpace = " onMouseOver=alert('today');";
    final String param3 = "param3";
    map.put(param3, new String[] { injectedCodeWithSpace });

    String injectedCodeWithQuote = "' string";
    final String param4 = "param4";
    map.put(param4, new String[] { injectedCodeWithQuote });

    String injectedCodeKeyboardWithSpace = " onkeydown=alert('today');";
    final String param5 = "param5";
    map.put(param5, new String[] { injectedCodeKeyboardWithSpace });

    Mockito.when(request.getParameterMap()).thenReturn(map);

    wrapper = new InjectionAttackWrapper(request);
    Map output = wrapper.getParameterMap();

    assertEquals("", ((String[]) output.get(param1))[0]);
    assertEquals("", ((String[]) output.get(param2))[0]);
    assertEquals("", ((String[]) output.get(param3))[0]);
    assertEquals("", ((String[]) output.get(param4))[0]);
    assertEquals("", ((String[]) output.get(param5))[0]);
  }

}