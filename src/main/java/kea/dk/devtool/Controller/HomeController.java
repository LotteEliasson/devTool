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
import org.springframework.web.bind.annotation.*;

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

	// projects:
	@GetMapping("projects")
	public String showProject(Model projektModel,HttpSession session){
	// tjek for project manager status - nedenstående er kun indtil modellen er udbygget og implementeret:
	if(session.getAttribute("PmID") ==null){
		int projectManagerID=1;
		session.setAttribute("PmID",projectManagerID);
	}
	// skal fx erstattes af nedenstående og kædes sammen så flere brugertyper kan se projekter

		/* if(session.getAttribute("Role").equals("ProjectManager"){
			int projectManagerID= user.getUserId();
			}
		*/
	int projectManagerID= (int) session.getAttribute("PmID"); // skal erstattes af ovenstående når modellen er klar

	projektModel.addAttribute("projects",projectRepository.getMyProjects(projectManagerID));
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
	@GetMapping("updateproject/{id}")
	public String updateProject(@PathVariable("id") int projectID, Model projektModel){
	Project project;
	project=projectRepository.findProjectByID(projectID);
	projektModel.addAttribute(project);


	return "updateproject";
	}
	@PostMapping("updateproject")
	public String showUpdateprojects(@RequestParam("projectID") int projectID, @RequestParam("projectName") String projectName, @RequestParam("startDate")Date startDate,
												@RequestParam("dueDate") Date dueDate, @RequestParam("projectManager") String projectManager,
												@RequestParam("customerName") String customerName, @RequestParam("expectedEnddate") Date expectedEnddate){
	Project updateproject= new Project();
	updateproject.setProjectId(projectID);
	updateproject.setProjectName(projectName);
	updateproject.setStartDate(startDate.toLocalDate());
	updateproject.setDueDate(dueDate.toLocalDate());
	updateproject.setProjectManager(projectManager);
	updateproject.setCustomerName(customerName);
	updateproject.setExpectedEndDate(expectedEnddate.toLocalDate());
	projectRepository.updateProject(updateproject);
	return "redirect:projects";
	}
	@GetMapping("/project/delete/{id}")
	public String deleteProject(@PathVariable("id") int projectID, HttpSession session){
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

		return "redirect:/projects";
	}

	// processes:
	@GetMapping("/processes/{projektid}")
	public String showProcesses(@PathVariable("projektid") int id, Model processes, HttpSession session){

		processes.addAttribute("processes", processRepository.getProcessByProjectId(id) );

		session.setAttribute("currentProject", id);
	return "processes";
	}

@PostMapping("/createprocess")
	public String createProcess(@RequestParam("processName") String processName,
								@RequestParam("expectedStartDate") LocalDate expectedStartDate,
								@RequestParam("expectedFinish") LocalDate expectedFinish,
								@RequestParam("startAfter") int startAfter,
								HttpSession session){
	int projectid =(int)session.getAttribute("currentProject");
	Processes newProcess = new Processes();
	newProcess.setProcessName(processName);
	newProcess.setExpectedStartDate(expectedStartDate);
	newProcess.setExpectedFinish(expectedFinish);
	newProcess.setStartAfterTask(startAfter);
	processRepository.addProcess(newProcess, projectid);
	return "redirect:/processes/" +projectid;
	}
	@PostMapping("/processes/update-process/")
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
		model.addAttribute("updateProcess", projectid);
		return "redirect:/processes/"+ projectid;
	}
	@GetMapping("/processes/delete/{processid}")
	public String deleteProcess(@PathVariable("processid") int deleteProcess, HttpSession session){
		int projectid=(int)session.getAttribute("currentProject");
		processRepository.findProcessById(deleteProcess);
		String check;
		check=processRepository.checkProcess(deleteProcess);
		if (check.contains("task")) {
			processRepository.deleteProcessTasksById(deleteProcess);
		}
		else {
			processRepository.deleteProcessById(deleteProcess);
		}

		return "redirect:/processes/"+ projectid;
	}

	// Tasks:
	@GetMapping("/taskview/{processId}")
	public String taskview(@PathVariable("processId") int processId, Model modelTask, HttpSession session){
		int projectID = (int) session.getAttribute("currentProject");
		modelTask.addAttribute("taskView", taskRepository.getTaskById(processId));
		session.setAttribute("currentProcess", processId);
		return "taskview";
	}

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

		newTask.setProcessId(newProcessId);

		newTask.setTaskName(newTaskName);
		newTask.setEffort(newEffort);
		newTask.setExpectedStartDate(newExpectedStartDate.toLocalDate());
		newTask.setMinAllocation(newMinAllocation);
		newTask.setTaskStatus(newTaskStatus);
		newTask.setAssignedId(newAssignedId);
		newTask.setTaskSequenceNumber(newTaskSequenceNumber);
		newTask.setProjectId(newProjectId);

		//Gem ny Task
		taskRepository.addTask(newTask, newProcessId);


		return "redirect:/taskview/" + newProcessId;
	}

	//Opdater task
	@GetMapping("/updatetask/{taskId}")
	public String updateTask(@PathVariable("taskId") int updateTask, Model taskModel) {

	Task updateTasks = taskRepository.findTaskById(updateTask);
	taskModel.addAttribute("taskUpdate", updateTasks);
		return "updatetask";
	}


	@PostMapping("/updateTask")
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


		return "redirect:/taskview/" + processID ;
}


	//Slet task
	@GetMapping("/deletetask/{taskId}")
	public String deleteTask(@PathVariable("taskId") int deleteTaskByID, HttpSession session){

		taskRepository.deleteTask(deleteTaskByID);
		int processId = (int) session.getAttribute("currentProcess");

		return "redirect:/taskview/" + processId;
	}

	//Users:
//	@GetMapping("/")
//	public String index(){
//	return "login";
//	}
	@GetMapping("login")
	public String showLogin(HttpSession session){
		String loginStatus="";
		int userID=0;
		if(session.isNew()){
			session.setAttribute("loginStatus",loginStatus);
			session.setAttribute("UserID",userID);
		}

	return "login";
	}
	@PostMapping("/login")
	public String testlogin(@RequestParam ("usertype") String access,@RequestParam("username") String username,
									@RequestParam("pwd") String pwd,HttpSession session){
	session.setAttribute("access",access);
	session.setAttribute("username",username);
	return "redirect:post_test";
	}
	@GetMapping("post_test")
	public String showPost(){
	return "post_test";
	}

}
