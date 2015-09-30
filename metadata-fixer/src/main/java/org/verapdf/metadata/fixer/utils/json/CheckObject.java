package org.verapdf.metadata.fixer.utils.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * @author Evgeniy Muravitskiy
 */
public class CheckObject {

	private String objectType;
	private String test;

	@JsonProperty("objectType")
	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	@JsonProperty("test")
	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	@JsonIgnore
	public boolean compareTo(String objectType, String test) {
		return this.objectType.equals(objectType) && this.test.equals(test);
	}
}
