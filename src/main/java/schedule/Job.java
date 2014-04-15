/**
 * Copyright © 2014 cafebabe(loveywh@gmail.com). All rights reserved.
 */
package schedule;

import trigger.Trigger;

/**
 * 任务接口
 * 
 * @author cafebabe
 * @Date 2014年4月15日 下午5:50:31
 * @version v1.0
 */
public interface Job {
	/**
	 * 执行任务
	 * 
	 * @param trigger
	 */
	void execute(Trigger trigger);
}
