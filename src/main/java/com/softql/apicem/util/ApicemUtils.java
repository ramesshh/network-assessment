package com.softql.apicem.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class ApicemUtils {

	public static String join(char c, String... s) {
		List<String> l = new ArrayList<String>();
		for (String str : s) {
			CollectionUtils.addIgnoreNull(l, str);
		}
		return StringUtils.join(l, ",");
	}

	public static void main(String[] args) {
		System.out.println(join(',', null, "a", "b", null, "c", null));
	}
}
