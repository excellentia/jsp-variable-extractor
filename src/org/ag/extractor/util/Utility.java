/**
 * 
 */
package org.ag.extractor.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Generic supporting Utility class.
 * 
 * @author Abhishek Gaurav
 */
public class Utility {

	private Utility() {
		// no access except getInstance()
	}

	public static void log(String str) {
		System.out.println(str);
	}

	public static String getFileContents(final String absFilePath) {

		String data = null;

		if (absFilePath != null) {

			File file = new File(absFilePath);

			if (file.isFile()) {

				byte[] buffer = new byte[4096];
				BufferedInputStream bis = null;

				try {

					int readSize = 0;
					StringBuilder builder = new StringBuilder();

					bis = new BufferedInputStream(new FileInputStream(file), 4096);

					while ((readSize = bis.read(buffer)) > 0) {
						builder.append(new String(buffer, 0, readSize, Charset.forName("UTF-8")));
					}

					data = builder.toString();
					bis.close();

				} catch (FileNotFoundException fnfe) {
					fnfe.printStackTrace();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}

			} else {
				log("Specified path is not a file : " + absFilePath);
			}
		}

		return data;

	}
	
	public static String normaliseFileData(final String data) {
		String str = null;
		
		str = trimToNull(data);
		
		if (str != null) {
			str = str.replaceAll("\r", "");
			str = str.replaceAll("\n", "");
		}
		
		return str;
	}

	public static String trimToNull(final String str) {

		String retVal = null;

		if (str != null) {
			retVal = str.trim();

			if (retVal.equals("")) {
				retVal = null;
			}
		}

		return retVal;

	}

}
