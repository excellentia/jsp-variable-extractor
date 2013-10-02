/**
 * 
 */
package org.ag.extractor.iface;

import java.util.Set;

/**
 * Interface managing the available operations.
 * 
 * @author Abhishek Gaurav
 */
public interface ExtractIface {

	/**
	 * Used to get the supported file type that is extraction target.
	 * 
	 * @return String extension (eg: ".jsp")
	 */
	public String getSupportedExtension();

	/**
	 * Used to get the extracted Variables from the extraction target.
	 * 
	 * @param filePath Absolute path of file that is extraction target.
	 * @return {@link Set} containing {@link String} objects.
	 */
	public Set<String> getExtractedVariables(String filePath);

}
