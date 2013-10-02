/**
 * 
 */
package org.ag.extractor.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ag.extractor.iface.ExtractIface;
import org.ag.extractor.util.Utility;
import org.json.JSONObject;

/**
 * JSP Specific implementation of {@link ExtractIface}.
 * 
 * @author Abhishek Gaurav
 */
public class JspExtractorImpl implements ExtractIface {

	private String getFileData(final String filePath) {

		String fileData = null;
		String ext = this.getSupportedExtension();

		if ((filePath != null) && (filePath.endsWith(ext))) {

			fileData = Utility.getFileContents(filePath);

			if (fileData != null) {
				fileData = Utility.normaliseFileData(fileData);
			}
		} else {
			Utility.log("Expected " + ext + " file. Incorrect / UnSupported file specified : " + filePath);
		}

		return fileData;

	}

	private Set<String> getMatches(final String fileData) {

		Set<String> matches = null;

		if (fileData != null) {

			matches = new HashSet<String>();

			Pattern elPattern = Pattern.compile("((\\$)*(\\%)*)+\\{([a-zA-Z0-9_-]*(\\.)*)*\\}");
			Matcher elMatcher = elPattern.matcher(fileData);

			while (elMatcher.find()) {
				matches.add(elMatcher.group().replaceAll("(\\{)*(\\$)*(\\%)*(\\})*", ""));
			}

			Pattern strutsPattern = Pattern.compile("(value|name)+(\\s)*=(\\s)*\"([a-zA-Z0-9_-]*(\\.)*)*\"");
			Matcher strutsMatcher = strutsPattern.matcher(fileData);

			while (strutsMatcher.find()) {
				matches.add(strutsMatcher.group().replaceAll("\"", "").replaceAll("(name|value)+(\\s)*=(\\s)*", ""));
			}

			for (String match : matches) {
				Utility.log(match);
			}
		}

		return matches;
	}

	private List<String> getIndividualProperties(final String qualifiedProperty) {

		List<String> list = null;

		if (qualifiedProperty != null) {

			list = new ArrayList<String>();
			list.addAll(Arrays.asList(qualifiedProperty.split("\\.")));

		}

		return list;

	}

	private void populateMap(Map<String, Object> propertyMap, String key, String value) {

		if ((propertyMap != null) && (key != null) && (value != null)) {

			if (propertyMap.containsKey(key)) {

				Object mapValue = propertyMap.get(key);

				if (mapValue instanceof String) {

					List<String> list = new ArrayList<String>();
					list.add(mapValue.toString());
					list.add(value);

					propertyMap.put(key, list);

				} else if (mapValue instanceof List) {

					List list = (List) mapValue;
					list.add(value);

					propertyMap.put(key, list);
				}

			} else {

				propertyMap.put(key, value);

			}
		}
	}

	private String createJSON(Set<String> dataSet) {

		String json = null;

		if ((dataSet != null) && (dataSet.size() > 0)) {

			Map<String, Object> propertyMap = new HashMap<String, Object>();

			for (String qualifiedProperty : dataSet) {

				List<String> properties = this.getIndividualProperties(qualifiedProperty);

				switch (properties.size()) {
				case 1:
					this.populateMap(propertyMap, properties.get(0), "");
					break;

				case 2:
					this.populateMap(propertyMap, properties.get(0), properties.get(1));
					break;

				default:
					break;
				}

			}

			JSONObject jsonObject = new JSONObject(propertyMap);
			json = jsonObject.toString();
			
			Utility.log(json);

		}

		return json;
	}

	@Override
	public String getSupportedExtension() {
		return ".jsp";
	}

	@Override
	public Set<String> getExtractedVariables(final String filePath) {

		String fileData = this.getFileData(filePath);
		Set<String> matches = this.getMatches(fileData);

		return matches;
	}

	public static void main(String[] args) {

		final String filePath = "<give path to JSP here>";
		JspExtractorImpl impl = new JspExtractorImpl();
		Set<String> matches = impl.getExtractedVariables(filePath);
		String json = impl.createJSON(matches);

	}

}
