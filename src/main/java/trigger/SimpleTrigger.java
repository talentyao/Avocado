/**
 * Copyright © 2014 cafebabe(loveywh@gmail.com). All rights reserved.
 */
package trigger;

import java.util.Date;

import schedule.Job;

/**
 * @author cafebabe
 * @Date 2014年4月15日 下午6:42:19
 * @version v1.0
 */
public class SimpleTrigger extends Trigger {
	/** 重复次数 */
	private int repeatCount = REPEAT_INDEFINITELY;
	/** 重复间隔 单位:ms */
	private int repeatInterval;
	/** 结束时间,优先级高于repeatCount */
	private Date endTime;

	/**
	 * 构建指定开始时间的一次性任务
	 * 
	 * @param name
	 *            任务名
	 * @param startTime
	 *            开始时间
	 * @param task
	 *            任务
	 */
	public SimpleTrigger(String name, Date startTime, Job task) {
		this(name, startTime, 1, Short.MAX_VALUE, task);
	}

	/**
	 * 构建指定时间开始重复指定次数及间隔时间的触发器
	 * 
	 * @param name
	 *            任务名
	 * @param startTime
	 *            开始时间
	 * @param repeatCount
	 *            重复次数
	 * @param repeatInterval
	 *            重复间隔.ms
	 * @param task
	 *            任务
	 */
	public SimpleTrigger(String name, Date startTime, int repeatCount,
			int repeatInterval, Job task) {
		this(name, startTime, repeatCount, repeatInterval, null, task);
	}

	public SimpleTrigger(String name, Date startTime, int repeatInterval,
			Job task) {
		this(name, startTime, REPEAT_INDEFINITELY, repeatInterval, null, task);
	}

	/**
	 * 构建指定开始时间结束时间及执行间隔的触发器
	 * 
	 * @param name
	 *            任务名
	 * @param startTime
	 *            开始时间
	 * @param endStime
	 *            结束时间
	 * @param repeatInterval
	 * @param job
	 */
	public SimpleTrigger(String name, Date startTime, Date endStime,
			int repeatInterval, Job job) {
		this(name, startTime, 0, repeatInterval, endStime, job);
	}

	public SimpleTrigger(String name, Date startTime, int repeatCount,
			int repeatInterval, Date endTime, Job task) {
		setName(name);
		this.repeatCount = repeatCount;
		this.repeatInterval = repeatInterval;
		this.endTime = endTime;
		setNextTime(startTime.getTime());
		setTask(task);
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getRepeatCount() {
		return this.repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	public int getRepeatInterval() {
		return this.repeatInterval;
	}

	public void setRepeatInterval(int repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public void updateNextTime(long curr) {
		setNextTime(curr + getRepeatInterval());
		if (getRepeatCount() == Trigger.REPEAT_INDEFINITELY) {
			return;
		}
		if (getEndTime() != null) {
			if (getNextTime() > getEndTime().getTime()) {
				setComplete(true);
			}
		} else if (getTriggerCount() + 1 >= getRepeatCount())
			setComplete(true);
	}
}