package kea.dk.devtool.utility;
import kea.dk.devtool.model.Task;

import java.util.*;
public class TaskComparator implements Comparator{

		@Override
			public int compare(Object o1,Object o2){
				Task t1=(Task)o1;
				Task t2=(Task)o2;

				if(t1.taskDaysNeeded()==t2.taskDaysNeeded())
					return 0;
				else if(t1.taskDaysNeeded()>t2.taskDaysNeeded())
					return 1;
				else
					return -1;
			}

			public int reverseCompare(Object o1, Object o2){
				Task t1=(Task)o1;
				Task t2=(Task)o2;

				if(t1.taskDaysNeeded()==t2.taskDaysNeeded())
					return 0;
				else if(t1.taskDaysNeeded()>t2.taskDaysNeeded())
					return -1;
				else
					return 1;
			}

}



