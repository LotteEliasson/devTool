package kea.dk.devtool.Controller;

import jakarta.servlet.http.HttpSession;
import kea.dk.devtool.model.Project;
import kea.dk.devtool.model.Task;
import kea.dk.devtool.repository.ProjectRepository;
import kea.dk.devtool.repository.TaskRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;

@Controller
public class HomeController {
ProjectRepository projectRepository;
// constructor of HomeController
TaskRepository taskRepository;
public HomeController(ProjectRepository projectRepository, TaskRepository taskRepository){
	this.projectRepository=projectRepository;
	this.taskRepository = taskRepository;
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




	@GetMapping("/taskview/{processId}")
	public String showWishes(@PathVariable("processId") int processId, Model modelTask, HttpSession session){
		modelTask.addAttribute("taskView", taskRepository.getTaskById(processId));
		session.setAttribute("currentProcess", processId);
		return "taskview";
	}

	@PostMapping("/taskview")
	public String createTask(@RequestParam("TaskName") String newTaskName,
							 @RequestParam("Effort") int newEffort,
							 @RequestParam("ExpectedStartDate") Date newExpectedStartDate,
							 @RequestParam("MinAllocation") int newMinAllocation,
							 @RequestParam("TaskStatus") String newTaskStatus,
							 @RequestParam("AssignedId") String newAssignedId,
							 @RequestParam("TaskSequenceNumber") int newTaskSequenceNumber,
							 HttpSession session){

		//Opret ny Task
		int newTaskId = (int) session.getAttribute("currentProcess");
		Task newTask = new Task();

		newTask.setTaskId(newTaskId);
		newTask.setTaskName(newTaskName);
		newTask.setEffort(newEffort);
		newTask.setExpectedStartDate(newExpectedStartDate.toLocalDate());
		newTask.setMinAllocation(newMinAllocation);
		newTask.setTaskStatus(newTaskStatus);
		newTask.setAssignedId(newAssignedId);
		newTask.setTaskSequenceNumber(newTaskSequenceNumber);

		//Gem ny Task
		taskRepository.addTask(newTask, newTaskId);

		return "redirect:taskview/"+newTaskId;

		//Opdater task


		//Slet task
		@GetMapping("/deletetask/{taskId}")
		public String deleteTask(@PathVariable("taskId") int deleteTaskByID, Httpsession session){

			taskRepository.deleteTask(deleteTaskByID);
			int processId =
		}
	}

}
