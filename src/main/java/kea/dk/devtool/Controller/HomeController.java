package kea.dk.devtool.Controller;

import jakarta.servlet.http.HttpSession;
import kea.dk.devtool.model.Processes;
import kea.dk.devtool.model.Project;
import kea.dk.devtool.repository.ProcessRepository;
import kea.dk.devtool.model.Task;
import kea.dk.devtool.repository.ProjectRepository;
import kea.dk.devtool.repository.TaskRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;

@Controller
public class HomeController {
ProjectRepository projectRepository;
ProcessRepository processRepository;
TaskRepository taskRepository;
// constructor of HomeController
public HomeController(ProjectRepository projectRepository, ProcessRepository processRepository, TaskRepository taskRepository){
	this.projectRepository=projectRepository;
	this.taskRepository = taskRepository;
	this.processRepository=processRepository;
}
// controller of pages
	@GetMapping("projects")
	public String showProject(Model projektModel,HttpSession session){
	int projektManagerID= (int) session.getAttribute("PmID");
	String pmName="jacob"; //test - senere ændres til session
	projektModel.addAttribute("projects",projectRepository.getMyProjects(projektManagerID));
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
	@GetMapping("update/{id}")
	public String updateProject(@PathVariable("id") int projectID, Project project){

	return "redirect:projects";
	}
	@GetMapping("/processes/{id}")
	public String showProcesses(@PathVariable("id") int id, Model processes, HttpSession session){

		processes.addAttribute("processes", processRepository.getProcessByProjectId(id) );

	return "processes";
	}
	@GetMapping("/delete/{id}")
	public String deleteProject(@PathVariable("id") int projectID, Model projektModel, HttpSession session){
	int pmID=(int) session.getAttribute("PmID") ;
	projectRepository.deleteTasksByProjecID(projectID);
	projectRepository.deleteProcessByProjecID(projectID);
	projectRepository.deleteProjectByID(projectID);
	projektModel.addAttribute("projects",projectRepository.getMyProjects(pmID));

	return "redirect:projects";
	}
@PostMapping("/processes")
	public String createProcess(@RequestParam("processName") String processName,
								@RequestParam("expectedStartDate") LocalDate expectedStartDate,
								@RequestParam("expectedFinish") LocalDate expectedFinish,
								@RequestParam("startAfterTask") int startAfter){

	Processes newProcess = new Processes();
	newProcess.setProcessName(processName);
	newProcess.setExpectedStartDate(expectedStartDate);
	newProcess.setExpectedFinish(expectedFinish);
	newProcess.setStartAfterTask(startAfter);
	processRepository.addProcess(newProcess);
	return "redirect:/processes";
	}
	@PostMapping("updateprocess")
	public String updateProcess(@RequestParam("processId") int updateprocessId,
								@RequestParam("projectId") int updateprojectId,
								@RequestParam("processName")String updateprocessName,
								@RequestParam("expectedStartDate") LocalDate updateexpectedStartDate,
								@RequestParam("expectedFinish") LocalDate updateexpectedFinish,
								@RequestParam("startAfterTask")int updatestartAfter) {

		Processes updatedProcess = new Processes(updateprocessId, updateprojectId, updateprocessName,
								updateexpectedStartDate,updateexpectedFinish,
								updatestartAfter);
		processRepository.updateProcess(updatedProcess);

		return "redirect:/processes";
	}
	@GetMapping("/deleteprocess/{processId}")
	public String deleteProcess(@PathVariable("processId") int deleteProcessTask, HttpSession session, Model model) {
		processRepository.deleteProcessTasksById(deleteProcessTask);
		processRepository.deleteProcessById(deleteProcessTask);

	return "redirect:/processes";
	}
	@GetMapping("/taskview/{processId}")
	public String taskview(@PathVariable("processId") int processId, Model modelTask, HttpSession session){
		modelTask.addAttribute("taskView", taskRepository.getTaskById(processId));
		session.setAttribute("currentProcess", processId);
		return "taskview";
	}

	@GetMapping("/taskview/")
	public String createTask(){
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
							 HttpSession session) {

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

		return "redirect:taskview/" + newTaskId;
	}

	@GetMapping("/taskview")
	public String updateTask(@PathVariable("processId") int updateTasks, Model updateModel ){

	return "taskview";
	}

	//Opdater task
	@PostMapping("/taskview")
	public String updateTask(@RequestParam("TaskId") int updateTaskId,
							 @RequestParam("ProcessId") int updateProcessId,
							 @RequestParam("TaskName") String updateTaskName,
							 @RequestParam("Effort") int updateEffort,
							 @RequestParam("ExpectedStartDate") Date updateExpectedStartDate,
							 @RequestParam("MinAllocation") int updateMinAllocation,
							 @RequestParam("TaskStatus") String updateTaskStatus,
							 @RequestParam("AssignedId") String updateAssignedId,
							 @RequestParam("TaskSequenceNumber") int updateTaskSequenceNumber,
							 @RequestParam("ProjectId") int updateProjectId,
							 Model modelUpdateTask,
							 HttpSession session) {
		Task updateTasks = new Task(updateTaskId, updateProcessId, updateTaskName, updateEffort, updateExpectedStartDate.toLocalDate(), updateMinAllocation, updateTaskStatus,updateAssignedId,updateTaskSequenceNumber,updateProjectId);

		taskRepository.updateTask(updateTasks);
		int processID = (int) session.getAttribute("currentProcess");
		modelUpdateTask.addAttribute("updateTask", processID);


		return "redirect:/taskview/" + processID;

}


	//Slet task
	@GetMapping("/deletetask/{taskId}")
	public String deleteTask(@PathVariable("taskId") int deleteTaskByID, HttpSession session){

		taskRepository.deleteTask(deleteTaskByID);
		int processId = (int) session.getAttribute("currentProcess");

		return "redirect:/taskview/" + processId;
	}

}
