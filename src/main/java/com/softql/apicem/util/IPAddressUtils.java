package com.softql.apicem.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;

public class IPAddressUtils {

	public static long ipToLong(InetAddress ip) {
		byte[] octets = ip.getAddress();
		long result = 0;
		for (byte octet : octets) {
			result <<= 8;
			result |= octet & 0xff;
		}
		return result;
	}

	public static boolean isInRange(String fromIp, String toIP, String testIp) throws UnknownHostException {

		try {
			if (StringUtils.isNotBlank(fromIp) && StringUtils.isNotBlank(toIP)) {
				long ipLo = ipToLong(InetAddress.getByName(fromIp));
				long ipHi = ipToLong(InetAddress.getByName(toIP));
				long ipToTest = ipToLong(InetAddress.getByName(testIp));
				return ipToTest >= ipLo && ipToTest <= ipHi;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) throws UnknownHostException {
		long ipLo = ipToLong(InetAddress.getByName("192.200.0.0"));
		long ipHi = ipToLong(InetAddress.getByName("192.255.0.0"));
		long ipToTest = ipToLong(InetAddress.getByName("192.200.3.0"));

		System.out.println(ipToTest >= ipLo && ipToTest <= ipHi);
	}
}