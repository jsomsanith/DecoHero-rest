package com.jso.deco.service.project;

import static com.jso.deco.api.exception.DHMessageCode.MISSING_FIELD;

import org.apache.commons.lang.StringUtils;

import com.jso.deco.api.common.Category;
import com.jso.deco.api.exception.DHServiceException;
import com.jso.deco.api.service.request.ProjectCreationRequest;
import com.jso.deco.api.service.request.ProjectIdeaCreationRequest;

public class ProjectServiceValidator {

	private static final String TIMESTAMP_FORMAT = "^[0-9]+$";

	public void validate(final ProjectCreationRequest request) throws DHServiceException {
		if(request.getCategory() == null) {
			throw new DHServiceException(MISSING_FIELD, "category");
		}
		else if(request.getCategory() == Category.RM && request.getRoom() == null) {
			throw new DHServiceException(MISSING_FIELD, "room");
		}
		else if(StringUtils.isBlank(request.getTitle())) {
			throw new DHServiceException(MISSING_FIELD, "title");
		}
		else if(StringUtils.isBlank(request.getDescription())) {
			throw new DHServiceException(MISSING_FIELD, "description");
		}
		else if(request.getImages() == null || request.getImages().isEmpty()) {
			throw new DHServiceException(MISSING_FIELD, "images");
		}
	}

	public void validate(final String projectId) throws DHServiceException {
		if(StringUtils.isBlank(projectId)) {
			throw new DHServiceException(MISSING_FIELD, "projectId");
		}
	}

	public void validate(final String fieldName, final String field, final String fromDate) throws DHServiceException {
		if(StringUtils.isBlank(field)) {
			throw new DHServiceException(MISSING_FIELD, fieldName);
		}
		if(fromDate != null && ! fromDate.matches(TIMESTAMP_FORMAT)) {
			throw new DHServiceException(MISSING_FIELD, "fromDate");
		}
	}
	
	public void validate(final ProjectIdeaCreationRequest request) throws DHServiceException {
		if(StringUtils.isBlank(request.getDescription())) {
			throw new DHServiceException(MISSING_FIELD, "description");
		}
	}
}
