package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface IStudentService {
	List<Students> findPaginated(int pageNo, int pageSize);
}
