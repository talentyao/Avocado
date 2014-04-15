/**
 * Copyright © 2014 cafebabe(loveywh@gmail.com). All rights reserved.
 */
package test;

import java.util.Date;

import schedule.Job;
import schedule.ScheduledManager;
import trigger.SimpleTrigger;
import trigger.Trigger;

/**
 * @author cafebabe
 * @Date 2014年4月15日 下午7:38:09
 * @version v1.0
 */
public class Simple {
	public static void main(String[] args) throws InterruptedException {
		SimpleTrigger trigger = new SimpleTrigger("test1", new Date(
				System.currentTimeMillis() + 3000), new Job() {

			@Override
			public void execute(Trigger trigger) {
				System.out.println(trigger);
			}
		});
		ScheduledManager.getInstance().schedule(trigger);
		Thread.sleep(5000);
		ScheduledManager.getInstance().shutdown();
	}
}
