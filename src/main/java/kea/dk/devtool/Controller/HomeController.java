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
	if(session.getAttribute("PmID") ==null){
		int projektManagerID=1;
		session.setAttribute("PmID",projektManagerID);
	}

	int projektManagerID= (int) session.getAttribute("PmID");
//	String pmName="jacob"; //test - senere ændres til session
	projektModel.addAttribute("projects",projectRepository.getMyProjects(projektManagerID));
	return "projects";
	}
	@PostMapping("create_projects")
	public String createProject(@RequestParam("projectName") String projectName, @RequestParam("startDate")Date startDate,
										 @RequestParam("dueDate") Date dueDate, @RequestParam("projectManager") String projectManager,
										 @RequestParam("customerName") String customerName,HttpSession session){
		Project newproject=new Project();
		int pmID=(int) session.getAttribute("PmID");
		newproject.setProjectName(projectName);
		newproject.setCustomerName(customerName);
		newproject.setProjectManager(projectManager);
		newproject.setDueDate(dueDate.toLocalDate());
		newproject.setStartDate(startDate.toLocalDate());
		newproject.setExpectedEndDate(dueDate.toLocalDate()); // når projektet er nyoprettet er expected og duedate ens
		newproject.setProjectManagerID(pmID); // her skal bygges noget andet til en admin!
		projectRepository.addProject(newproject);
		return "redirect:projects";

	}
//	@GetMapping("/project/update/{id}")
//	public String updateProject(@PathVariable("id") int projectID, Model projektModel){
//	Project project;
//	project=projectRepository.findProjectByID(projectID);
//	projektModel.addAttribute()
//	projectRepository.updateProject(project);
//
//	return "redirect:projects";
//	}

	@GetMapping("/processes/{projektid}")
	public String showProcesses(@PathVariable("projektid") int id, Model processes, HttpSession session){

		processes.addAttribute("processes", processRepository.getProcessByProjectId(id) );
		session.setAttribute("currentProject", id);
	return "processes";
	}
	@GetMapping("/project/delete/{id}")
	public String deleteProject(@PathVariable("id") int projectID, Model projektModel, HttpSession session){
	int pmID=(int) session.getAttribute("PmID") ;
	String check;
	check=projectRepository.checkProject(pmID);
	if (check.contains("task")) {
		projectRepository.deleteTasksByProjecID(projectID);
	}
	else if (check.contains("process")) {
		projectRepository.deleteProcessByProjecID(projectID);
	}
	else {
		projectRepository.deleteProjectByID(projectID);
	}


//	projektModel.addAttribute("projects",projectRepository.getMyProjects(pmID));

	return "redirect:/projects";
	}
@PostMapping("/processes")
	public String createProcess(@RequestParam("processName") String processName,
								@RequestParam("expectedStartDate") LocalDate expectedStartDate,
								@RequestParam("expectedFinish") LocalDate expectedFinish,
								@RequestParam("startAfterTask") int startAfter,
								HttpSession session){
	int projectIdSess =(int)session.getAttribute("currentProject");
	Processes newProcess = new Processes();
	newProcess.setProcessName(processName);
	newProcess.setExpectedStartDate(expectedStartDate);
	newProcess.setExpectedFinish(expectedFinish);
	newProcess.setStartAfterTask(startAfter);
	processRepository.addProcess(newProcess, projectIdSess);
	return "redirect:/processes/" +projectIdSess;
	}
	@PostMapping("updateprocess")
	public String updateProcess(@RequestParam("processId") int updateprocessId,
								@RequestParam("projectId") int updateprojectId,
								@RequestParam("processName")String updateprocessName,
								@RequestParam("expectedStartDate") LocalDate updateexpectedStartDate,
								@RequestParam("expectedFinish") LocalDate updateexpectedFinish,
								@RequestParam("startAfterTask")int updatestartAfter,
								Model model, HttpSession session) {

		Processes updatedProcess = new Processes(updateprocessId, updateprojectId, updateprocessName,
								updateexpectedStartDate,updateexpectedFinish, updatestartAfter);
		processRepository.updateProcess(updatedProcess);
		int projectid=(int)session.getAttribute("currentProject");
		model.addAttribute("");
		return "redirect:/processes"+ projectid;
	}
	@GetMapping("/deleteprocess/{processId}")
	public String deleteProcess(@PathVariable("processId") int deleteProcessTask, HttpSession session, Model model) {
		processRepository.deleteProcessTasksById(deleteProcessTask);
		processRepository.deleteProcessById(deleteProcessTask);

	return "redirect:/processes";
	}
	@GetMapping("/taskview/{processId}")
	public String taskview(@PathVariable("processId") int processId, Model modelTask, HttpSession session){
		int projektID = (int) session.getAttribute("currentProject");
		modelTask.addAttribute("taskView", taskRepository.getTaskById(processId,projektID));
		session.setAttribute("currentProcess", processId);
		return "taskview";
	}

//	@GetMapping("/taskview/")
//	public String createTask(){
//		return "taskview";
//	}


	@PostMapping("/createTasks")
	public String createTask(@RequestParam("TaskName") String newTaskName,
							 @RequestParam("Effort") int newEffort,
							 @RequestParam("ExpectedStartDate") Date newExpectedStartDate,
							 @RequestParam("MinAllocation") int newMinAllocation,
							 @RequestParam("TaskStatus") String newTaskStatus,
							 @RequestParam("AssignedId") String newAssignedId,
							 @RequestParam("TaskSequenceNumber") int newTaskSequenceNumber,
							 HttpSession session) {

		//Opret ny Task
		int newProcessId = (int) session.getAttribute("currentProcess");
		int newProjectId = (int) session.getAttribute("currentProject");

		Task newTask = new Task();

		newTask.setTaskId(newProcessId);

		newTask.setTaskName(newTaskName);
		newTask.setEffort(newEffort);
		newTask.setExpectedStartDate(newExpectedStartDate.toLocalDate());
		newTask.setMinAllocation(newMinAllocation);
		newTask.setTaskStatus(newTaskStatus);
		newTask.setAssignedId(newAssignedId);
		newTask.setTaskSequenceNumber(newTaskSequenceNumber);

		//Gem ny Task
		taskRepository.addTask(newTask, newProcessId,newProjectId);

		return "redirect:taskview/" + newProcessId;
	}

//	@GetMapping("/taskview/{taskID}")
//	public String updateTask(@PathVariable("taskID") int taskID, Model updateModel){
//	Task updatetask = taskRepository.findTaskById(taskID);
//
//
//	return "taskview";
//	}

	//Opdater task
//	@PostMapping("/taskview")
//	public String updateTask(@RequestParam("TaskId") int updateTaskId,
//							 @RequestParam("ProcessId") int updateProcessId,
//							 @RequestParam("TaskName") String updateTaskName,
//							 @RequestParam("Effort") int updateEffort,
//							 @RequestParam("ExpectedStartDate") Date updateExpectedStartDate,
//							 @RequestParam("MinAllocation") int updateMinAllocation,
//							 @RequestParam("TaskStatus") String updateTaskStatus,
//							 @RequestParam("AssignedId") String updateAssignedId,
//							 @RequestParam("TaskSequenceNumber") int updateTaskSequenceNumber,
//							 @RequestParam("ProjectId") int updateProjectId,
//							 Model modelUpdateTask,
//							 HttpSession session) {
//		Task updateTasks = new Task(updateTaskId, updateProcessId, updateTaskName, updateEffort, updateExpectedStartDate.toLocalDate(), updateMinAllocation, updateTaskStatus,updateAssignedId,updateTaskSequenceNumber,updateProjectId);
//
//		taskRepository.updateTask(updateTasks);
//		int processID = (int) session.getAttribute("currentProcess");
//		modelUpdateTask.addAttribute("updateTask", processID);
//
//
//		return "redirect:/taskview/" + processID;
//
//}
	@PostMapping("/opdaterTask"/{taskId})
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
