package org.verapdf.features.objects;

/**
 * @author Maksim Bezrukov
 */
public interface ActionFeaturesObjectAdapter extends FeaturesObjectAdapter {

	String getType();
	Location getLocation();

	enum Location {
		DOCUMENT("Document"),
		PAGE("Page"),
		INTERACTIVE_FORM_FIELD("Interactive From Field"),
		ANNOTATION("Annotation"),
		OUTLINES("Outlines");

		private final String text;

		Location(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}
}
