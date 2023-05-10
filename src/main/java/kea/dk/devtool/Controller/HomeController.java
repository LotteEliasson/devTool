package kea.dk.devtool.Controller;

import kea.dk.devtool.model.Project;
import kea.dk.devtool.repository.ProjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;

@Controller
public class HomeController {
ProjectRepository projectRepository;
// constructor of HomeController
public HomeController(ProjectRepository projectRepository){
	this.projectRepository=projectRepository;
}
// controller of pages
	@GetMapping("projects")
	public String showProject(){
	return "projects";
	}
	@PostMapping("projects")
	public String createProject(@RequestParam("projectName") String projectName, @RequestParam("startDate")Date startDate,
										 @RequestParam("dueDate") Date dueDate, @RequestParam("projectManager") String projectManager,
										 @RequestParam("customerName") String customerName){
		Project newproject=new Project();
		newproject.setProjectName(projectName);
		newproject.setCustomerName(customerName);
		newproject.setProjectManager(projectManager);
		newproject.setDueDate(dueDate.toLocalDate());
		newproject.setStartDate(startDate.toLocalDate());
		newproject.setExpectedEndDate(dueDate.toLocalDate()); // når projektet er nyoprettet er expected og duedate ens
		projectRepository.addProject(newproject);
		return "redirect:projects";

	}

}
