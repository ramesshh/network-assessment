package com.softql.apicem.util;

import org.apache.commons.lang3.StringUtils;

public class URLUtil {

	public static String constructUrl(String ip, String port, String version, String service, String ticket) {
		return new StringBuilder("https://").append(ip).append(":").append(StringUtils.isBlank(port) ? "443" : port)
				.append("/api/").append(StringUtils.isNotBlank(version) ? version : "v0").append(service)
				.append(StringUtils.isNotBlank(ticket) ? "?ticket=" + ticket : "").toString();
	}
}
