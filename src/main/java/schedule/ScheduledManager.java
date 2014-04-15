/**
 * Copyright © 2014 cafebabe(loveywh@gmail.com). All rights reserved.
 */
package schedule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import trigger.Trigger;

/**
 * 任务调度管理器
 * 
 * @author cafebabe
 * @Date 2014年4月15日 下午6:36:38
 * @version v1.0
 */
public class ScheduledManager {
	private Logger log = LoggerFactory.getLogger(getClass());

	private Map<String, Trigger> runJobMap = new ConcurrentHashMap<String, Trigger>();
	private DelayQueue<Trigger> taskQueue = new DelayQueue<Trigger>();
	private boolean isWork = true;

	private ExecutorService manager = Executors.newSingleThreadExecutor();
	private ExecutorService executor = Executors.newFixedThreadPool(Runtime
			.getRuntime().availableProcessors());

	private static ScheduledManager instance = new ScheduledManager();

	public static ScheduledManager getInstance() {
		return instance;
	}

	private ScheduledManager() {
		startManager();
	}

	private void startManager() {
		this.manager.execute(new Runnable() {
			public void run() {
				while (isWork)
					try {
						Trigger trigger = taskQueue.take();
						executor.submit(new JobRun(trigger, runJobMap,
								taskQueue));
					} catch (InterruptedException e) {
						log.error("", e);
					}
			}
		});
	}

	public Map<String, Trigger> getRunJobMap() {
		return this.runJobMap;
	}

	public DelayQueue<Trigger> getTaskQueue() {
		return this.taskQueue;
	}

	public void schedule(Trigger trigger) {
		if ((trigger == null) || (trigger.getName() == null)) {
			new IllegalArgumentException("任务为空or任务参数不完整").printStackTrace();
		} else if (this.runJobMap.containsKey(trigger.getName())) {
			new RuntimeException("已存在同名任务:" + trigger.getName())
					.printStackTrace();
		} else {
			trigger.setSeqNum(Trigger.sequencer.getAndIncrement());
			boolean ans = this.taskQueue.offer(trigger);
			if (ans)
				this.runJobMap.put(trigger.getName(), trigger);
			else
				new RuntimeException("添加任务失败:" + trigger.getName())
						.printStackTrace();
		}
	}

	public Trigger getTrigger(String name) {
		return runJobMap.get(name);
	}

	public boolean contains(String name) {
		return runJobMap.containsKey(name);
	}

	public boolean cancel(String jobName) {
		boolean ans = false;
		Trigger trigger = runJobMap.get(jobName);
		if (trigger != null) {
			trigger.setCancel(true);
			trigger.setTask(null);
			trigger.setListener(null);
			this.runJobMap.remove(trigger.getName());
			ans = true;
		}
		return ans;
	}

	public void shutdown() {
		this.isWork = false;
		manager.shutdownNow();
		executor.shutdownNow();
	}
}