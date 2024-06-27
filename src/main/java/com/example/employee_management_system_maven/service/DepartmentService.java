package com.example.employee_management_system_maven.service;

import java.util.List;

import com.example.employee_management_system_maven.entity.Department;
import com.example.employee_management_system_maven.exception.DepartmentNotFoundException;

public interface DepartmentService {
	public Department saveDepartment(Department department);

	public List<Department> fetchDepartmentList();

	public Department fetchDepartmentById(Long departmentId) throws DepartmentNotFoundException;

	public void deleteDepartmentById(Long departmentId);

	public Department updateDepartment(Long departmentId, Department department);

	Department fetchDepartmentByName(String departmentName);
}