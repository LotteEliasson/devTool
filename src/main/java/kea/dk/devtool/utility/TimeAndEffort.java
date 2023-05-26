package kea.dk.devtool.utility;

import org.springframework.cglib.core.Local;

import java.time.DayOfWeek;
import java.time.LocalDate;

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

	}
