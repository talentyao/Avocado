/**
 * Copyright © 2014 cafebabe(loveywh@gmail.com). All rights reserved.
 */
package schedule;

/**
 * 任务监听接口
 * 
 * @author cafebabe
 * @Date 2014年4月15日 下午6:11:53
 * @version v1.0
 */
public interface JobListener {
	public void startJob();

	public void endJob();
}
