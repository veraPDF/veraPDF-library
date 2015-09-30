package org.verapdf.metadata.fixer.utils.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class AppliedObjects {

	private List<CheckObject> checkObjects;

	@JsonProperty("checkObjects")
	public List<CheckObject> getCheckObjects() {
		return checkObjects;
	}

	public void setCheckObjects(List<CheckObject> checkObjects) {
		this.checkObjects = checkObjects;
	}

	@JsonIgnore
	public boolean contains(String objectType, String test) {
		for (CheckObject object : this.checkObjects) {
			if (object.compareTo(objectType, test)) {
				return true;
			}
		}

		return false;
	}
}
