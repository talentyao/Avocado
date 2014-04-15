/**
 * Copyright © 2014 cafebabe(loveywh@gmail.com). All rights reserved.
 */
package trigger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import schedule.Job;

/**
 * 每周任务
 * 
 * @author cafebabe
 * @Date 2014年4月15日 下午6:41:53
 * @version v1.0
 */
public class WeeklyTrigger extends Trigger {
	private List<Calendar> calList;
	private int index = 0;

	/**
	 * @param name
	 * @param calList
	 * @param job
	 */
	public WeeklyTrigger(String name, List<Calendar> calList, Job job) {
		setName(name);
		setTask(job);
		setCalList(passCalList(calList));
		setNextTime(((Calendar) calList.get(0)).getTimeInMillis());
	}

	private List<Calendar> passCalList(List<Calendar> calList) {
		Calendar curr = Calendar.getInstance();
		for (Calendar cal : calList) {
			if (cal.compareTo(curr) < 0) {
				cal.add(3, 1);
			}
		}
		calSort(calList);
		return calList;
	}

	/**
	 * 
	 * @param name
	 * @param hour
	 * @param minute
	 * @param dayOfWeek
	 * @param job
	 */
	public WeeklyTrigger(String name, int hour, int minute, int[] dayOfWeek,
			Job job) {
		if ((dayOfWeek == null) || (dayOfWeek.length == 0)) {
			throw new IllegalArgumentException("day of week must be set!");
		}
		List<Calendar> calList = new ArrayList<Calendar>();
		Calendar curr = Calendar.getInstance();
		for (int day : dayOfWeek) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_WEEK, day);
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
			cal.set(Calendar.SECOND, 0);
			calList.add(cal);
			if (cal.compareTo(curr) < 0) {
				cal.add(Calendar.WEEK_OF_YEAR, 1);
			}
		}
		setName(name);
		setTask(job);
		calSort(calList);
		setCalList(calList);
		setNextTime(((Calendar) calList.get(0)).getTimeInMillis());
	}

	private void calSort(List<Calendar> calList) {
		Collections.sort(calList, new Comparator<Calendar>() {
			@Override
			public int compare(Calendar o1, Calendar o2) {
				return o1.compareTo(o2);
			}
		});
	}

	public List<Calendar> getCalList() {
		return this.calList;
	}

	public void setCalList(List<Calendar> calList) {
		this.calList = calList;
	}

	public void updateNextTime(long curr) {
		Calendar cal = (Calendar) this.calList.get(this.index);
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		this.index = ((this.index + 1) % this.calList.size());
		setNextTime(calList.get(this.index).getTimeInMillis());
	}
}