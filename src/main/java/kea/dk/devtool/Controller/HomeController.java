package kea.dk.devtool.Controller;

import jakarta.servlet.http.HttpSession;
import kea.dk.devtool.model.*;
import kea.dk.devtool.repository.ProcessRepository;
import kea.dk.devtool.repository.ProjectRepository;
import kea.dk.devtool.repository.TaskRepository;
import kea.dk.devtool.repository.UserRepository;
import kea.dk.devtool.utility.TimeAndEffort;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

@Controller
public class HomeController
	{
		ProjectRepository projectRepository;
		ProcessRepository processRepository;
		TaskRepository taskRepository;
		UserRepository userRepository;

		// constructor of HomeController
		public HomeController(ProjectRepository projectRepository, ProcessRepository processRepository, TaskRepository taskRepository, UserRepository userRepository)
			{
				this.projectRepository = projectRepository;
				this.taskRepository = taskRepository;
				this.processRepository = processRepository;
				this.userRepository = userRepository;
			}
// controller of pages

		// projects:
		@GetMapping("project_manager/{id}")
		public String showProject(@PathVariable("id") int userid, Model projectModel, HttpSession session)
			{
				User user = userRepository.getUserById(userid);
				session.setAttribute("User", user);
				session.setAttribute("PmID", userid);
				ArrayList<Project> myProjects = (ArrayList<Project>) projectRepository.getMyProjects(userid);

				for (Project p : myProjects) {
					ArrayList<Processes> myprocesses=(ArrayList<Processes>) processRepository.getProcessByProjectId(p.getProjectId());
					if(myprocesses.size()!=0) {
						p.setProcesses(myprocesses);

					}else {
						myprocesses.add(new Processes());
					}

						for (Processes proc : myprocesses) {
							ArrayList<Task> tasks = (ArrayList<Task>) taskRepository.getTaskById(proc.getProcessId());
							if (tasks.size() != 0) {
								proc.setTaskList(tasks);
								proc.getProcessEndDate();
							}
							else {
								tasks.add(new Task());
							}

						}

				}

				projectModel.addAttribute("projects", myProjects);
				projectModel.addAttribute("States", ProjectStatus.values());
				return "project_manager";
			}

		@PostMapping("create_projects")
		public String createProject(@RequestParam("projectName") String projectName,
											 @RequestParam("startDate") Date startDate,
											 @RequestParam("dueDate") Date dueDate,
											 @RequestParam("projectManager") String projectManager,
											 @RequestParam("customerName") String customerName,
											 @RequestParam("projectstate") ProjectStatus projectstate,
											 HttpSession session)
			{
				Project newproject = new Project();
				int pmID = (int) session.getAttribute("PmID");
				newproject.setProjectName(projectName);
				newproject.setCustomerName(customerName);
				newproject.setProjectManager(projectManager);
				newproject.setDueDate(dueDate.toLocalDate());
				newproject.setStartDate(startDate.toLocalDate());
				newproject.setExpectedEndDate(dueDate.toLocalDate()); // når projektet er nyoprettet er expected og duedate ens
				newproject.setProjectManagerID(pmID); // her skal bygges noget andet til en admin!
				newproject.setStatus(projectstate);
				projectRepository.addProject(newproject);
				return "redirect:/project_manager/" + pmID;

			}

		@GetMapping("/updateproject/{id}")
		public String updateProject(@PathVariable("id") int projectID, Model projectModel)
			{
				Project project;
				project = projectRepository.findProjectByID(projectID);

				//ProjectStatus.values();
				projectModel.addAttribute("States", ProjectStatus.values());
				projectModel.addAttribute(project);


				return "updateproject";
			}

		@PostMapping("updateproject")
		public String showUpdateprojects(@RequestParam("projectID") int projectID, @RequestParam("projectName") String projectName, @RequestParam("startDate") Date startDate,
													@RequestParam("dueDate") Date dueDate, @RequestParam("projectManager") String projectManager,
													@RequestParam("customerName") String customerName, @RequestParam("expectedEnddate") LocalDate expectedEnddate,
													@RequestParam("newstate") ProjectStatus state, HttpSession session)
			{
				int pmID = (int) session.getAttribute("PmID");
				// slå projektet op
				Project updateproject = projectRepository.findProjectByID(projectID);

				//forbered proces-liste og hent tasks
				ArrayList<Processes> myprocesses = (ArrayList<Processes>) processRepository.getProcessByProjectId(projectID);
				if (myprocesses.size() != 0) {
					for (Processes proc : myprocesses) {
						ArrayList<Task> tasklist = (ArrayList<Task>) taskRepository.getTaskById(proc.getProcessId());
						if (tasklist.size() != 0) {
							proc.setTaskList(tasklist);
							ArrayList<LocalDate> taskEndDates = new ArrayList<>();
							for (Task enddate : tasklist) {
								taskEndDates.add(enddate.getExpectedFinish());
							}
							//denne getter setter en ny expectedfinish
							proc.getExpectedFinish();
						}
						proc.setExpectedFinish(processRepository.findProcessById(projectID).getExpectedFinish());
					}
					//tilføj procesliste med task til updateprojektet
					updateproject.setProcesses(myprocesses);

					//lav en liste slut datoer for processer
					ArrayList<LocalDate> processEndDates = new ArrayList<>();

					for (Processes p : myprocesses) {
						processEndDates.add(p.getProcessEndDate());
					}
					//sorter og tag den datoen for den proces som slutter senest og sæt det til projektets slutdato
					Collections.sort(processEndDates);
					updateproject.setExpectedEndDate(processEndDates.get(processEndDates.size() - 1));
				}
				else {
					updateproject.setExpectedEndDate(expectedEnddate);
				}
				//opdater øvrige felter
				updateproject.setProjectName(projectName);
				updateproject.setStartDate(startDate.toLocalDate());
				updateproject.setDueDate(dueDate.toLocalDate());
				updateproject.setProjectManager(projectManager);
				updateproject.setCustomerName(customerName);
				updateproject.setStatus(state);
				//gem projektet
				projectRepository.updateProject(updateproject);
				return "redirect:project_manager/" + pmID;
			}

		@GetMapping("/project/delete/{id}")
		public String deleteProject(@PathVariable("id") int projectID, HttpSession session)
			{
				int pmID = (int) session.getAttribute("PmID");
				String check;
				check = projectRepository.checkProject(projectID);
				if (check.contains("task")) {
					projectRepository.deleteTasksByProjecID(projectID);
				}
				else if (check.contains("process")) {
					projectRepository.deleteProcessByProjecID(projectID);
				}
				else {
					projectRepository.deleteProjectByID(projectID);
				}

				return "redirect:/project_manager/" + pmID;
			}

		// processes:
		@GetMapping("/processes/{projektid}")
		public String showProcesses(@PathVariable("projektid") int id, Model processesModel, HttpSession session)
			{
				ArrayList<Processes> processes=(ArrayList<Processes>) processRepository.getProcessByProjectId(id);

				if (processes.size()!=0){
					for (Processes p:processes){
						ArrayList<Task> procesTask = (ArrayList<Task>) taskRepository.getTaskById(p.getProcessId());
						if (procesTask.size() != 0) {
							for(Task t:procesTask){
								if (t.getTaskDependencyNumber()!=-1){
									t.setExpectedStartDate(taskRepository.findTaskById(t.getTaskDependencyNumber()));
									t.getExpectedFinish();
								//	taskRepository.updateTask(t);
								}
								else{
									t.setExpectedStartDate(processRepository.findProcessById(t.getProcessId()).getExpectedStartDate());
									t.getExpectedFinish();
								}
							}
							p.setTaskList(procesTask);
							p.getProcessEndDate();
						//	processRepository.updateProcess(p);
						}

					}
			}

				processesModel.addAttribute("project", projectRepository.findProjectByID(id));
				processesModel.addAttribute("showProjectName", projectRepository.findProjectByID(id).getProjectName());
				processesModel.addAttribute("showProjectManager", projectRepository.findProjectByID(id).getProjectManager());
				processesModel.addAttribute("processes",processes);
				processesModel.addAttribute("projectTasks", taskRepository.getProjectTasks(id));

				session.setAttribute("currentProject", id);
				return "processes";
			}

		@PostMapping("/createprocess")
		public String createProcess(@RequestParam("processName") String processName,
											 @RequestParam("expectedStartDate") LocalDate expectedStartDate,
											 @RequestParam("expectedFinish") LocalDate expectedFinish,
											 @RequestParam("startAfter") int startAfter,
											 HttpSession session)
			{
				int projectid = (int) session.getAttribute("currentProject");
				Processes newProcess = new Processes();
				newProcess.setProcessName(processName);


				newProcess.setStartAfterTask(startAfter);
				//hvis processen skal starte ved afslutningen af en bestemt task overskrives expectedStartDate
				if (startAfter != -1) {
					Task task = taskRepository.findTaskById(startAfter);
					expectedStartDate = task.getExpectedFinish();
				}
				else {
					//ellers sættes processen til at starte med project
					Project project = projectRepository.findProjectByID(projectid);
					expectedStartDate = project.getStartDate();
				}
				//processen får sat expectedStartDate (som enten er opdateret af brugeren eller hentet fra task ved startAfter!=-1)
				//overvej at lave StartAfter som dropdown selection med default =-1 i UI for at undgå at brugeren laver fejl under indtastning
				//ved at indtaske ugyldigt taskId
				newProcess.setExpectedStartDate(expectedStartDate);
				newProcess.setTaskList(new ArrayList<>());
				newProcess.setExpectedFinish(TimeAndEffort.procesEnddate(newProcess));
				processRepository.addProcess(newProcess, projectid);
				return "redirect:/processes/" + projectid;
			}

		@GetMapping("/updateprocess/{processid}")
		public String updateProcess(@PathVariable("processid") int processID, Model processModel, HttpSession session)
			{
				Processes updateProcess;
				int projectid = (int) session.getAttribute("currentProject");
				updateProcess = processRepository.findProcessById(processID);
				processModel.addAttribute("processUpdate", updateProcess);
				processModel.addAttribute("projectTasks", taskRepository.getProjectTasks(projectid));
				return "updateprocess";
			}

		@PostMapping("/updateprocess")
		public String updateProcess(@RequestParam("processId") int updateprocessId,
											 @RequestParam("projectId") int updateprojectId,
											 @RequestParam("processName") String updateprocessName,
											 @RequestParam("expectedStartDate") LocalDate updateexpectedStartDate,
											 @RequestParam("expectedFinish") LocalDate updateexpectedFinish,
											 @RequestParam("startAfterTask") int updatestartAfter,
											 Model model, HttpSession session)
			{
				Processes updateProcess = processRepository.findProcessById(updateprocessId);
				ArrayList<Task> taskList;
				taskList = (ArrayList<Task>) taskRepository.getTaskById(updateprocessId);

				updateProcess.setStartAfterTask(updatestartAfter); //vi fourdsætter at der er valgt gyldig tasknummer eller default
				//hvis processen skal starte ved afslutningen af en bestemt task overskrives expectedStartDate
				if (updatestartAfter != -1) {
					Task task = taskRepository.findTaskById(updatestartAfter);

					updateexpectedStartDate = task.getExpectedFinish();
					updateProcess.setExpectedStartDate(updateexpectedStartDate);
					updateexpectedFinish=updateProcess.getProcessEndDate();
				}
				//processen får sat expectedStartDate (som enten er opdateret af brugeren eller hentet fra task ved startAfter!=-1)
				//overvej at lave StartAfter som dropdown selection med default =-1 i UI for at undgå at brugeren laver fejl under indtastning
				//ved at indtaske ugyldigt taskId
				updateProcess.setTaskList(taskList);
				updateProcess.setExpectedStartDate(updateexpectedStartDate);

				updateProcess.setExpectedFinish(updateexpectedFinish);
				updateProcess.setProcessName(updateprocessName);

				processRepository.updateProcess(updateProcess);
				int projectid = (int) session.getAttribute("currentProject");
				model.addAttribute("updateProcess", projectid);
				return "redirect:/processes/" + projectid;
			}

		@GetMapping("/processes/delete/{processid}")
		public String deleteProcess(@PathVariable("processid") int deleteProcess, HttpSession session, Model processModel)
			{
				int projectid = (int) session.getAttribute("currentProject");
				//processRepository.findProcessById(deleteProcess);
				String check;
				check = processRepository.checkProcess(deleteProcess);
				if (check.contains("task")) {
					processRepository.deleteProcessTasksById(deleteProcess);
				}

				processRepository.deleteProcessById(deleteProcess);


				processModel.addAttribute("processes", processRepository.getProcessByProjectId(projectid));
				return "redirect:/processes/" + projectid;
			}

		// Tasks:
		@GetMapping("/taskview/{processId}")
		public String taskview(@PathVariable("processId") int processId, Model modelTask, HttpSession session)
			{
				int projectID = (int) session.getAttribute("currentProject");
				int projectPMID = (int) session.getAttribute("PmID");
				ArrayList<Task> taskArrayList=(ArrayList<Task>) taskRepository.getTaskById(processId);
				if(taskArrayList.size()!=0){
					for(Task task:taskArrayList){
						if (task.getTaskDependencyNumber()!=-1){
							task.setExpectedStartDate(taskRepository.findTaskById(task.getTaskDependencyNumber()));
							task.getExpectedFinish();
						}
						else{
						task.setExpectedStartDate(processRepository.findProcessById(task.getProcessId()).getExpectedStartDate());
						task.getExpectedFinish();
						}

					}
				}

				modelTask.addAttribute("project",projectRepository.findProjectByID(projectID));
				modelTask.addAttribute("showProjectName", projectRepository.findProjectByID(projectID).getProjectName());
				modelTask.addAttribute("showProjectManager", projectRepository.findProjectByID(projectID).getProjectManager());
				modelTask.addAttribute("taskView", taskArrayList);
				modelTask.addAttribute("projectsByPmId", projectRepository.getMyProjects(projectPMID));
				modelTask.addAttribute("TaskStates", TaskStatus.values());
				session.setAttribute("currentProcess", processId);

				return "taskview";
			}

		@PostMapping("/createTasks")
		public String createTask(@RequestParam("TaskName") String newTaskName,
										 @RequestParam("Effort") int newEffort,
										 @RequestParam("ExpectedStartDate") LocalDate newExpectedStartDate,
										 @RequestParam("MinAllocation") int newMinAllocation,
										 @RequestParam("TaskStatus") TaskStatus newTaskStatus,
										 @RequestParam("AssignedId") String newAssignedId,
										 @RequestParam("TaskSequenceNumber") int newTaskSequenceNumber,
										 @RequestParam("developerId") int developerId,
										 HttpSession session)
			{

				//Opret ny Task
				int newProcessId = (int) session.getAttribute("currentProcess");
				int newProjectId = (int) session.getAttribute("currentProject");

				Task newTask = new Task();


				// hvis taskDependency !=-1 så skal expected startdate overskrives med en beregning
				if (newTaskSequenceNumber != -1) {
					Task task = taskRepository.findTaskById(newTaskSequenceNumber);
					newExpectedStartDate = task.getExpectedFinish();
				}
				else {
					Processes processes = processRepository.findProcessById(newProcessId);
					newExpectedStartDate = processes.getExpectedStartDate();
				}
				newTask.setProcessId(newProcessId);
				newTask.setTaskName(newTaskName);
				newTask.setEffort(newEffort);
				newTask.setExpectedStartDate(newExpectedStartDate);
				newTask.setMinAllocation(newMinAllocation);
				newTask.setTaskStatus(newTaskStatus);
				newTask.setAssignedname(newAssignedId);
				newTask.setTaskDependencyNumber(newTaskSequenceNumber);
				newTask.setProjectId(newProjectId);
				newTask.setDeveloperId(developerId);

				//Gem ny Task

				taskRepository.addTask(newTask, newProcessId);


				return "redirect:/taskview/" + newProcessId;
			}

		//Opdater task
		@GetMapping("/updatetask/{taskId}")
		public String updateTask(@PathVariable("taskId") int updateTask, Model taskModel, HttpSession session)
			{
				int processId = (int) session.getAttribute("currentProcess");
				Task updateTasks = taskRepository.findTaskById(updateTask);
				taskModel.addAttribute("TaskStates", TaskStatus.values());
				taskModel.addAttribute("taskUpdate", updateTasks);
				taskModel.addAttribute("procesTasks", taskRepository.getTaskById(processId));
				return "updatetask";
			}


		@PostMapping("/updateTask")
		public String updateTask(@RequestParam("TaskId") int updateTaskId,
										 @RequestParam("ProcessId") int updateProcessId,
										 @RequestParam("TaskName") String updateTaskName,
										 @RequestParam("Effort") int updateEffort,
										 @RequestParam("ExpectedStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate updateExpectedStartDate,
										 @RequestParam("MinAllocation") int updateMinAllocation,
										 @RequestParam("TaskStatus") TaskStatus updateTaskStatus,
										 @RequestParam("Assignedname") String updateAssignedname,
										 @RequestParam("taskDependencyNumber") int updatetaskDependencyNumber,
										 @RequestParam("ProjectId") int updateProjectId,
										 @RequestParam("developerId") int developerId,
										 Model modelUpdateTask,
										 HttpSession session)
			{
				Task updateTasks = taskRepository.findTaskById(updateTaskId);
				if (updatetaskDependencyNumber != -1) {
					updateTasks.setExpectedStartDate(taskRepository.findTaskById(updatetaskDependencyNumber));

				}
				else {
					updateTasks.setExpectedStartDate(updateExpectedStartDate);

				}
				//denne getter setter en ny slutdato baseret på startdato
				updateTasks.getExpectedFinish();

				updateTasks.setTaskDependencyNumber(updatetaskDependencyNumber);

				updateTasks.setAssignedname(updateAssignedname);
				updateTasks.setTaskStatus(updateTaskStatus);
				updateTasks.setTaskId(updateTaskId);
				updateTasks.setTaskName(updateTaskName);
				updateTasks.setProcessId(updateProcessId);
				updateTasks.setEffort(updateEffort);
				updateTasks.setMinAllocation(updateMinAllocation);
				updateTasks.setExpectedStartDate(updateExpectedStartDate);
				updateTasks.setDeveloperId(developerId);
				updateTasks.setProjectId(updateProjectId);
				taskRepository.updateTask(updateTasks);
				int processID = (int) session.getAttribute("currentProcess");
				modelUpdateTask.addAttribute("updateTask", processID);


				return "redirect:/taskview/" + processID;
			}


		//Slet task
		@GetMapping("/deletetask/{taskId}")
		public String deleteTask(@PathVariable("taskId") int deleteTaskByID, HttpSession session)
			{

				taskRepository.deleteTask(deleteTaskByID);
				int processId = (int) session.getAttribute("currentProcess");

				return "redirect:/taskview/" + processId;
			}

		//User:
//	@GetMapping("/")
//	public String index(){
//	return "login";
//	}
		@GetMapping("login")
		public String showLogin(HttpSession session, Model model)
			{
				String loginStatus = "";
				int userID = 0;
				if (session.isNew()) {
					session.setAttribute("loginStatus", loginStatus);
					session.setAttribute("UserID", userID);
				}
				model.addAttribute("Roles", HasRole.values());
				return "login";
			}

		@PostMapping("/login")
		public String testlogin(@RequestParam("usertype") String access, @RequestParam("username") String username,
										@RequestParam("pwd") String pwd,
										HttpSession session, Model userModel)
			{

				User user;
				String loginstatus = "";
				user = userRepository.login(username, pwd);
				if (user.getUserName() == null) {
					loginstatus = "fail";
					session.setAttribute("loginstatus", loginstatus);
					//return "redirect:/login";
				}
				else if (user.getRole() == HasRole.valueOf(access)) {
					session.setAttribute("access", access);
					session.setAttribute("userId", user.getUserId());
					loginstatus = "succes";
					session.setAttribute("loginstatus", loginstatus);
					String userpage = String.valueOf(user.getRole()).toLowerCase();
					userModel.addAttribute("projects", projectRepository.getMyProjects(user.getUserId()));
					return "redirect:/" + userpage + '/' + user.getUserId();
				}
				else {
					loginstatus = "limited";
					session.setAttribute("loginstatus", loginstatus);

				}
				session.setAttribute("access", user.getRole());


				return "redirect:/post_test";
			}

		@GetMapping("post_test")
		public String showPost()
			{
				return "post_test";
			}

		// create user
		@GetMapping("create_user")
		public String showCreateUser()
			{
				return "create_user";
			}

		@PostMapping("create_user")
		public String createUser(@RequestParam("user_name") String userName, @RequestParam("password") String password)
			{
				String encodedPassword = userRepository.passwordCrypt(password);
				User newUser = new User();
				newUser.setUserName(userName);
				newUser.setUserPassword(encodedPassword);
				userRepository.createUser(newUser);
				return "redirect:/login";
			}

		@GetMapping("admin/{id}")
		public String showAdminPage(@PathVariable("id") int userid, Model userModel, HttpSession session)
			{
				User user = userRepository.getUserById(userid);


				HasRole role;
				String searchRole;
				session.setAttribute("userid", userid);

				if (session.getAttribute("Role") == null) {
					role = HasRole.UNASSIGNED;
					session.setAttribute("Role", role);
					searchRole = "";
				}
				else {
					searchRole = String.valueOf(session.getAttribute("Role"));


				}
//	session.setAttribute("userid",userid);

				userModel.addAttribute("Users", userRepository.getAllUsersByRole(searchRole));
				userModel.addAttribute("Roles", HasRole.values());
				return "admin";
			}

		@PostMapping("/admin")
		public String selectUsertype(@RequestParam("usertype") String usertype, Model userModel, HttpSession session)
			{
				session.setAttribute("Role", usertype);
				int id = (int) session.getAttribute("userid");
				userModel.addAttribute("Users", userRepository.getAllUsersByRole(usertype));
				return "redirect:/admin/" + id;
			}

		@GetMapping("edit_role/{id}")
		public String showupdateUser(@PathVariable("id") int userId, Model userModel)
			{
				User user = userRepository.getUserById(userId);
				userModel.addAttribute(user);
				return "edit_role";
			}

		@PostMapping("/edit_role")
		public String updateUser(@RequestParam("updateid") int updateid,
										 @RequestParam("updatename") String updatename,
										 @RequestParam("updaterole") String updaterole,
										 Model userModel, HttpSession session)
			{
				int id = (int) session.getAttribute("userid");
				String standard = "UNASSIGNED";
				User user = userRepository.getUserById(updateid);
				user.setUserName(updatename);
				user.setRole(HasRole.valueOf(updaterole));
				userRepository.editUser(user);
				userModel.addAttribute("Users", userRepository.getAllUsersByRole(standard));
				return "redirect:/admin/" + id;
			}

		@GetMapping("edit_password/{id}")
		public String showEditPassword(@PathVariable("id") int userid, Model userModel)
			{
				User user = userRepository.getUserById(userid);
				userModel.addAttribute(user);
				return "edit_password";
			}

		@PostMapping("/edit_password")
		public String editPassword(@RequestParam("editid") int userid, @RequestParam("updatepassword") String newPassword,
											Model userModel, HttpSession session)
			{
				User user = userRepository.getUserById(userid);
				// encode new password
				String encodedPassword = userRepository.passwordCrypt(newPassword);
				user.setUserPassword(encodedPassword);
				userRepository.updatePassword(user);
				if (session.getAttribute("Role").equals("ADMIN")) {
					String standard = "UNASSIGNED";
					userModel.addAttribute("Users", userRepository.getAllUsersByRole(standard));
					return "redirect:/admin";
				}
				return "redirect:/login";
			}

		@GetMapping("/")
		public String showindex()
			{
				return "redirect:/login";
			}

		@GetMapping("all_projects")
		public String allprojects(Model model)
			{

				model.addAttribute("allprojects", userRepository.getAllProjects());

				return "all_projects";
			}

		@GetMapping("developer/{id}")
		public String showDeveloper(@PathVariable("id") int id, Model userModel)
			{

				userModel.addAttribute("myTasks", userRepository.getMyTasks(id));
				return "developer";
			}
	}
