package org.verapdf.metadata.fixer.impl.pb.model;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.verapdf.metadata.fixer.entity.InfoDictionary;
import org.verapdf.metadata.fixer.utils.DateConverter;

/**
 * @author Evgeniy Muravitskiy
 */
public class InfoDictionaryImpl implements InfoDictionary {

	private final PDDocumentInformation info;

	public InfoDictionaryImpl(PDDocumentInformation info) {
		if (info == null) {
			throw new IllegalArgumentException("Info dictionary representation can not be null");
		}
		this.info = info;
	}

	@Override
	public String getTitle() {
		return this.info.getTitle();
	}

	@Override
	public void setTitle(String title) {
		this.info.setTitle(title);
	}

	@Override
	public String getSubject() {
		return this.info.getSubject();
	}

	@Override
	public void setSubject(String subject) {
		this.info.setSubject(subject);
	}

	@Override
	public String getAuthor() {
		return this.info.getAuthor();
	}

	@Override
	public void setAuthor(String author) {
		this.info.setAuthor(author);
	}

	@Override
	public String getProducer() {
		return this.info.getProducer();
	}

	@Override
	public void setProducer(String producer) {
		this.info.setProducer(producer);
	}

	@Override
	public String getKeywords() {
		return this.info.getKeywords();
	}

	@Override
	public void setKeywords(String keywords) {
		this.info.setKeywords(keywords);
	}

	@Override
	public String getCreator() {
		return this.info.getCreator();
	}

	@Override
	public void setCreator(String creator) {
		this.info.setCreator(creator);
	}

	@Override
	public String getCreationDate() {
		COSBase modDate = this.info.getCOSObject().getDictionaryObject(COSName.CREATION_DATE);
		return modDate instanceof COSString ? ((COSString) modDate).getString() : null;
	}

	@Override
	public void setCreationDate(String creationDate) {
		this.info.getCOSObject().setString(COSName.CREATION_DATE, DateConverter.toPDFFormat(creationDate));
	}

	@Override
	public String getModificationDate() {
		COSBase modDate = this.info.getCOSObject().getDictionaryObject(COSName.MOD_DATE);
		return modDate instanceof COSString ? ((COSString) modDate).getString() : null;
	}

	@Override
	public void setModificationDate(String modificationDate) {
		this.info.getCOSObject().setString(COSName.MOD_DATE, DateConverter.toPDFFormat(modificationDate));
	}

	@Override
	public boolean isNeedToBeUpdated() {
		return this.info.getCOSObject().isNeedToBeUpdated();
	}

	@Override
	public void setNeedToBeUpdated(boolean needToBeUpdated) {
		this.info.getCOSObject().setNeedToBeUpdated(true);
	}

}
