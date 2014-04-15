/**
 * Copyright © 2014 cafebabe(loveywh@gmail.com). All rights reserved.
 */
package trigger;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import schedule.Job;
import schedule.JobListener;

/**
 * 任务触发器
 * 
 * @author cafebabe
 * @Date 2014年4月15日 下午6:19:32
 * @version v1.0
 */
public abstract class Trigger implements Delayed {
	public static int REPEAT_INDEFINITELY = -1;
	public static final AtomicLong sequencer = new AtomicLong(0L);
	private long createTime = System.currentTimeMillis();
	private String name;
	private long preTime;
	private long nextTime;
	private boolean complete = false;
	private boolean cancel = false;
	private int triggerCount = 0;
	private long seqNum;
	/** 任务 */
	private Job task;
	/** 任务监听 */
	private JobListener listener;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPreTime() {
		return this.preTime;
	}

	public void setPreTime(long preTime) {
		this.preTime = preTime;
	}

	public long getNextTime() {
		return this.nextTime;
	}

	public void setNextTime(long nextTime) {
		this.nextTime = nextTime;
	}

	public boolean isComplete() {
		return this.complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public boolean isCancel() {
		return this.cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public int getTriggerCount() {
		return this.triggerCount;
	}

	public void setTriggerCount(int triggerCount) {
		this.triggerCount = triggerCount;
	}

	public Job getTask() {
		return this.task;
	}

	public void setTask(Job task) {
		this.task = task;
	}

	public JobListener getListener() {
		return this.listener;
	}

	public void setListener(JobListener listener) {
		this.listener = listener;
	}

	/**
	 * @param seqNum
	 *            the seqNum to set
	 */
	public void setSeqNum(long seqNum) {
		this.seqNum = seqNum;
	}

	/**
	 * @return the seqNum
	 */
	public long getSeqNum() {
		return seqNum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Delayed o) {
		int ans = 0;
		if (o == this) {
			return ans;
		}
		if ((o instanceof Trigger)) {
			Trigger trg = (Trigger) o;
			if (getNextTime() == trg.getNextTime()) {
				if (getSeqNum() < trg.getSeqNum())
					ans = -1;
				else
					ans = 1;
			} else if (getNextTime() < trg.getNextTime()) {
				ans = -1;
			} else {
				ans = 1;
			}
		} else {
			if (getDelay(TimeUnit.MILLISECONDS) < o
					.getDelay(TimeUnit.MILLISECONDS))
				ans = -1;
			else if (getDelay(TimeUnit.MILLISECONDS) > o
					.getDelay(TimeUnit.MILLISECONDS))
				ans = 1;
		}
		return ans;
	}

	public long getDelay(TimeUnit unit) {
		return nextTime - System.currentTimeMillis();
	}

	public long getCreateTime() {
		return createTime;
	}

	public Date getEndTime() {
		return null;
	}

	public void setEndTime(Date endTime) {
	}

	/** 更新下次执行时间 */
	public abstract void updateNextTime(long curr);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Trigger [createTime=" + createTime + ", name=" + name
				+ ", preTime=" + preTime + ", nextTime=" + nextTime
				+ ", complete=" + complete + ", cancel=" + cancel
				+ ", triggerCount=" + triggerCount + ", seqNum=" + seqNum
				+ ", task=" + task + ", listener=" + listener + "]";
	}

}