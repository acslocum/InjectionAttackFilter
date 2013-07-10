package com.slocumbrau.filter;

import static org.junit.Assert.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CookieXSSTest {
	private InjectionAttackWrapper wrapper;
	private HttpServletRequest request;

	@Before
	public void setUp() throws Exception {
	    request = Mockito.mock(HttpServletRequest.class);
	    wrapper = new InjectionAttackWrapper(request);
	}

	@Test
	public void shouldFilterScriptTagsFromCookies() {
		String cookieName = "aeLocale";
		String cookieVal = "en_SG_AP----><sCrIpT>alert(45152)</sCrIpT>";
		Cookie cookie = new Cookie(cookieName, cookieVal);
		Cookie[] value = new Cookie[1];
		value[0] = cookie;		
		Mockito.when(request.getCookies()).thenReturn(value);
		Cookie[] cookies = wrapper.getCookies();
		cookie = cookies[0];
		assertEquals(cookieName,cookie.getName());
		assertEquals("en_SG_APsCrIpTalert(45152)/sCrIpT",cookie.getValue());
	}

}
