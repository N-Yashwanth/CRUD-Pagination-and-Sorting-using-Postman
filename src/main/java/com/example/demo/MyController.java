package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class MyController {
	@Autowired
	private IStudentService iService;
	@Autowired
	StudentRepository sRepository;
	@Autowired
	StudentRepo sRepo;
	@GetMapping
	public List<Students> getAllStudents(){
		return sRepo.findAll();
	}
	@GetMapping("/{id}")
	public Students getStudentByID(@PathVariable int id) {
		return sRepo.findById(id).get();
	}
	@PostMapping("/add")
	@ResponseStatus(code = HttpStatus.CREATED)
	public void createStudent(@RequestBody Students s) {
		sRepo.save(s);
	}
	@PutMapping("/update/{id}")
	public Students updateStudent(@PathVariable int id, @RequestBody Students stud) {
		Students student=sRepo.findById(id).orElse(null);
		if(stud.getSname()!=null){
			student.setSname(stud.getSname());
		}
		if(stud.getScity()!=null){
			student.setScity(stud.getScity());
		}
		return sRepo.save(student);
	}
	@DeleteMapping("/{id}")
	public String deleteStudent(@PathVariable int id) {
		if(sRepo.existsById(id)) {
			sRepo.deleteById(id);
			return "Student deleted successfully";
		}
		else {
			return "Student not found";
		}
	}
	@GetMapping("/{pageNo}/{pageSize}")
	public List<Students> paginated(@PathVariable int pageNo, @PathVariable int pageSize){
		return iService.findPaginated(pageNo, pageSize);
	}
	@GetMapping("/sorting")
	public Iterable<Students> getAllByCols(@RequestParam String field, @RequestParam String direction){
		Direction sortDirection=direction.equalsIgnoreCase("desc")?Direction.DESC:Direction.ASC;
		return sRepository.findAll(Sort.by(sortDirection,field));
	}
	@GetMapping("/page/{pageNo}/{pageSize}/sort")
	public List<Students> getPaginatedandSorted(@PathVariable int pageNo, @PathVariable int pageSize, @RequestBody String field, @RequestBody String direction){
		Sort sort=direction.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(field).ascending():Sort.by(field).descending();
		Pageable pageable=PageRequest.of(pageNo, pageSize);
		Page<Students> result=sRepository.findAll(pageable);
		return result.hasContent()?result.getContent():new ArrayList<>();
	}
}
