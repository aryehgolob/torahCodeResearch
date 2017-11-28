package com.qaz216.codes.etc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class OpsUtil {
	
	public static Properties getProperties(String propFileName) {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(propFileName);

			// load a properties file
			prop.load(input);

			return prop;

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

}
