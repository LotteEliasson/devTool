package kea.dk.devtool.utility;

import kea.dk.devtool.model.Processes;
import kea.dk.devtool.model.Project;
import kea.dk.devtool.model.Task;
import kea.dk.devtool.repository.ProcessRepository;
import kea.dk.devtool.repository.ProjectRepository;
import org.springframework.cglib.core.Local;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TimeAndEffort
	{


		public static int daysBetween(LocalDate d1, LocalDate d2){
			LocalDate check=d1;
			int days=0;
			while (check.isBefore(d2)){
				days++;
				check=check.plusDays(1);
			}
			return days;
		}
		public static int workingDaysBetween(LocalDate d1, LocalDate d2){
			LocalDate check =d1;
			int workingdays=0;
			while (check.isBefore(d2)){

				if(!(check.getDayOfWeek()== DayOfWeek.SATURDAY || check.getDayOfWeek()==DayOfWeek.SUNDAY)){
					workingdays+=1;
				}
				check=check.plusDays(1);
			}
			return workingdays;
		}
		public static int normalWorkdaysNeeded(double totalEffort,double workhoursPerDay){

			int days= (int) Math.ceil(totalEffort/workhoursPerDay);
			return days;
		}
		public static LocalDate calculateDate(LocalDate startdate,int workdays){
		LocalDate newEnddate=startdate;
		int checkdays=0;
		while (checkdays<=workdays){
			newEnddate=newEnddate.plusDays(1);
			if(newEnddate.getDayOfWeek()!=DayOfWeek.SATURDAY||newEnddate.getDayOfWeek()!=DayOfWeek.SUNDAY){
				checkdays++;
			}
		}
		return newEnddate;
		}

		/**
		 *
		 * @param process
		 * @return
		 * this method takes a process and returns a calculated date based on the duration of the critical path
		 * of the tasks. used to calculate proces enddate and startdate.
		 */
		public static LocalDate procesEnddate(Processes process){

			LocalDate procesfinish=LocalDate.now();
			// hvis der findes tasks i processen:
			if(process.getTaskList()!=null|| !process.getTaskList().isEmpty()) {
				ArrayList<Task> proces = (ArrayList<Task>) process.getTaskList();
				HashMap<Integer, Task> pathlist = new HashMap<>();
				for (Task t : proces) {

					// tilføj pathlist hvis dependencynumber ikke findes i forvejen -compare t.dependency
					if (!pathlist.containsKey(t.getTaskDependencyNumber())) {
						pathlist.put(t.getTaskDependencyNumber(), t);
					}
					// erstat hvis workdaysneeded er større end den task som ligger der i forvejen
					else if (t.taskDaysNeeded() > pathlist.get(t.getTaskDependencyNumber()).taskDaysNeeded()) {
						pathlist.replace(t.getTaskDependencyNumber(), t);
					}
				}
				// iterer over tasks i pathlist for at få en sum af workdaysneeded
				int totaldays = 0;
				for (Task criticalPath : pathlist.values()) {
					totaldays = totaldays + criticalPath.taskDaysNeeded();
				}
				//få fat i startdato for processen og kald calculateDate

				procesfinish = calculateDate(process.getExpectedStartDate(), totaldays);
			}
			//hvis der endnu ikke er tasks i processen:
			else {
				// hvis proces skal starte med projekt
				if(process.getStartAfterTask()==-1){
				procesfinish=process.getExpectedStartDate().plusDays(1);
				}
				//ellers skal den beregne expected finish ud fra projectTask - klares ved at sætte/beregne expected start først

			}
			return procesfinish;
		}

	}
