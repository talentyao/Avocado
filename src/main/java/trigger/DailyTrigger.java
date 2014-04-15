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
 * 每日任务
 * 
 * @author cafebabe
 * @Date 2014年4月15日 下午6:42:25
 * @version v1.0
 */
public class DailyTrigger extends Trigger {
	private List<Calendar> calList;
	private int index = 0;

	/**
	 * 构建每日任务,每天定点时间执行
	 * 
	 * @param name
	 *            任务名
	 * @param expression
	 *            时间表达式,多个时间用";"隔开,格式:时[:分][:秒], 如:1 表示每天1点, 2:20 表示每天2点20分
	 * @param job
	 */
	public DailyTrigger(String name, String expression, Job job) {
		setName(name);
		setTask(job);
		setCalList(passTimeExp(expression));
		setNextTime(((Calendar) this.calList.get(0)).getTimeInMillis());
	}

	/**
	 * 构建每日任务,每天定点时间执行
	 * 
	 * @param name
	 *            任务名
	 * @param calList
	 *            日历列表
	 * @param job
	 *            任务
	 */
	public DailyTrigger(String name, List<Calendar> calList, Job job) {
		setName(name);
		setTask(job);
		setCalList(passCalList(calList));
		setNextTime(((Calendar) calList.get(0)).getTimeInMillis());
	}

	private List<Calendar> passCalList(List<Calendar> calList) {
		Calendar curr = Calendar.getInstance();
		for (Calendar cal : calList) {
			if (cal.compareTo(curr) < 0) {
				cal.add(5, 1);
			}
		}
		calSort(calList);
		return calList;
	}

	private List<Calendar> passTimeExp(String expression) {
		List<Calendar> calList = new ArrayList<Calendar>();
		String[] array = expression.trim().split(";");
		Calendar curr = Calendar.getInstance();
		for (String hourMin : array) {
			if (hourMin.isEmpty())
				continue;
			String[] time = hourMin.split(":");
			int hour = Integer.parseInt(time[0].trim());
			int minute = 0;
			if (time.length > 1) {
				minute = Integer.parseInt(time[1].trim());
			}
			int second = 0;
			if (time.length > 2) {
				second = Integer.parseInt(time[2].trim());
			}
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
			cal.set(Calendar.SECOND, second);
			if (cal.compareTo(curr) < 0) {
				cal.add(5, 1);
			}
			calList.add(cal);
		}
		calSort(calList);
		return calList;
	}

	private void calSort(List<Calendar> calList) {
		Collections.sort(calList, new Comparator<Calendar>() {
			@Override
			public int compare(Calendar o1, Calendar o2) {
				return o1.compareTo(o2);
			}
		});
	}

	public void updateNextTime(long curr) {
		Calendar cal = (Calendar) this.calList.get(this.index);
		cal.add(Calendar.DATE, 1);
		this.index = ((this.index + 1) % this.calList.size());
		setNextTime(calList.get(this.index).getTimeInMillis());
	}

	public List<Calendar> getCalList() {
		return this.calList;
	}

	public void setCalList(List<Calendar> calList) {
		this.calList = calList;
	}
}