package com.jso.deco.controller;

import static com.jso.deco.api.common.Category.RM;
import static com.jso.deco.api.common.Room.ENT;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.jso.deco.api.controller.CreateProjectResponse;
import com.jso.deco.api.exception.DHServiceException;
import com.jso.deco.api.service.request.ProjectCreationRequest;
import com.jso.deco.controller.adapter.ProjectAdapter;
import com.jso.deco.controller.image.ImageService;
import com.jso.deco.data.api.DBProject;
import com.jso.deco.data.service.ProjectDataService;
import com.jso.deco.data.service.UserDataService;


public class ProjectControllerTest {
	private final ProjectController controller = new ProjectController();
	private final UserDataService userDataService = mock(UserDataService.class);
	private final ProjectDataService projectDataService = mock(ProjectDataService.class);
	private final ImageService imageService = mock(ImageService.class);
	private final ProjectAdapter projectAdapter = mock(ProjectAdapter.class);
	
	@Before
	public void init() {
		controller.setUserDataService(userDataService);
		controller.setProjectDataService(projectDataService);
		controller.setAdapter(projectAdapter);
		controller.setImageService(imageService);
	}
	
	@Test
	public void createProject_should_save_images_create_project_and_update_user() throws DHServiceException {
		//given
		String userId = "userId";
		List<String> imgDataUrls = Lists.newArrayList("image1", "image2");
		List<String> imgIds = Lists.newArrayList("0000000000", "1111111111");
		
		ProjectCreationRequest request = new ProjectCreationRequest();
		request.setImages(imgDataUrls);
		request.setTitle("Project title");
		request.setDescription("Project description");
		request.setCategory(RM);
		request.setRoom(ENT);
		
		DBProject dbProject = new DBProject();
		dbProject.setId("projectId");

		when(imageService.saveProjectImg(imgDataUrls)).thenReturn(imgIds);
		when(projectAdapter.projectCreationRequestToDBProject(request, imgIds)).thenReturn(dbProject);
		doNothing().when(projectDataService).create(Mockito.any(DBProject.class));
		doNothing().when(imageService).moveProjectImg(dbProject.getId(), imgIds);
		doNothing().when(userDataService).addProjects(userId, dbProject.getId());
		
		//when
		CreateProjectResponse response = controller.createProject(userId, request);
		
		//then
		assertThat(response.getId()).isEqualTo(dbProject.getId());
		verify(imageService, times(1)).saveProjectImg(request.getImages());
		verify(projectDataService, times(1)).create(dbProject);
		verify(imageService, times(1)).moveProjectImg(dbProject.getId(), imgIds);
		verify(userDataService, times(1)).addProjects(userId, dbProject.getId());
		
	}
}