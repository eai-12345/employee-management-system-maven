package com.example.employee_management_system_maven.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.example.employee_management_system_maven.entity.Department;
import com.example.employee_management_system_maven.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DepartmentService departmentService;

	@Test
	@WithMockUser
	public void testSaveDepartment() throws Exception {
		Department department = Department.builder().departmentId(1L).departmentName("IT").departmentAddress("New York")
				.departmentCode("IT-01").build();

		when(departmentService.saveDepartment(any(Department.class))).thenReturn(department);

		mockMvc.perform(post("/departments").with(SecurityMockMvcRequestPostProcessors.csrf()) // Add CSRF token
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(department))).andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void testFetchDepartmentList() throws Exception {
		Department department1 = Department.builder().departmentId(1L).departmentName("IT")
				.departmentAddress("New York").departmentCode("IT-01").build();

		Department department2 = Department.builder().departmentId(2L).departmentName("HR")
				.departmentAddress("San Francisco").departmentCode("HR-01").build();

		List<Department> departments = Arrays.asList(department1, department2);

		when(departmentService.fetchDepartmentList()).thenReturn(departments);

		mockMvc.perform(get("/departments").with(SecurityMockMvcRequestPostProcessors.csrf()) // Add CSRF token
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void testFetchDepartmentById() throws Exception {
		Department department = Department.builder().departmentId(1L).departmentName("IT").departmentAddress("New York")
				.departmentCode("IT-01").build();

		when(departmentService.fetchDepartmentById(anyLong())).thenReturn(department);

		mockMvc.perform(get("/departments/1").with(SecurityMockMvcRequestPostProcessors.csrf()) // Add CSRF token
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void testDeleteDepartmentById() throws Exception {
		mockMvc.perform(delete("/departments/1").with(SecurityMockMvcRequestPostProcessors.csrf()) // Add CSRF token
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void testUpdateDepartment() throws Exception {
		Department department = Department.builder().departmentId(1L).departmentName("IT").departmentAddress("New York")
				.departmentCode("IT-01").build();

		when(departmentService.updateDepartment(anyLong(), any(Department.class))).thenReturn(department);

		mockMvc.perform(put("/departments/1").with(SecurityMockMvcRequestPostProcessors.csrf()) // Add CSRF token
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(department))).andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void testFetchDepartmentByName() throws Exception {
		Department department = Department.builder().departmentId(1L).departmentName("IT").departmentAddress("New York")
				.departmentCode("IT-01").build();

		when(departmentService.fetchDepartmentByName("IT")).thenReturn(department);

		mockMvc.perform(get("/departments/name/IT").with(SecurityMockMvcRequestPostProcessors.csrf()) // Add CSRF token
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}