package org.gzu.zoo.watcher;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.PropertyConfigurator;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.toolkit.java.util.ThreadUtil;
import common.toolkit.java.util.ObjectUtil;


public class ZooWatcherTest implements Watcher {

	AtomicInteger seq = new AtomicInteger();
	
	private static final int SESSION_TIMEOUT = 10000;
	
	private static final String CONNECTION_STRING = "127.0.0.1:2181";
	
	private CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	private ZooKeeper zk = null;
	/**
	 * Create ZK connection
	 * @param connectString
	 * @param sessionTimeout
	 */
	public void createConection(String connectString, int sessionTimeout) {
		try {
			zk = new ZooKeeper(connectString, sessionTimeout, this);
			connectedSemaphore.await();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Close ZK connection
	 */
	public void releaseConnection() {
		if ( !ObjectUtil.isBlank( this.zk ) ) {
			try {
				this.zk.close();
			} catch ( InterruptedException e ) {}
		}
	}

	@Override
	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub

		ThreadUtil.sleep( 200 );
		if ( ObjectUtil.isBlank( event ) ) {
			return;
		}
		// 连接状态
		KeeperState keeperState = event.getState();
		// 事件类型
		EventType eventType = event.getType();
		// 受影响的path
		String path = event.getPath();
		String logPrefix = "【Watcher-" + this.seq.incrementAndGet() + "】";

		System.out.println( logPrefix + "收到Watcher通知" );
		System.out.println( logPrefix + "连接状态:\t" + keeperState.toString() );
		System.out.println( logPrefix + "事件类型:\t" + eventType.toString() );

		if ( KeeperState.SyncConnected == keeperState ) {
			// 成功连接上ZK服务器
			if ( EventType.None == eventType ) {
				System.out.println( logPrefix + "成功连接上ZK服务器" );
				connectedSemaphore.countDown();
			} else if ( EventType.NodeCreated == eventType ) {
				System.out.println( logPrefix + "节点创建" );
//				this.exists( path, true );
			} else if ( EventType.NodeDataChanged == eventType ) {
				System.out.println( logPrefix + "节点数据更新" );
//				System.out.println( logPrefix + "数据内容: " + this.readData( ZK_PATH, true ) );
			} else if ( EventType.NodeChildrenChanged == eventType ) {
				System.out.println( logPrefix + "子节点变更" );
//				System.out.println( logPrefix + "子节点列表：" + this.getChildren( ZK_PATH, true ) );
			} else if ( EventType.NodeDeleted == eventType ) {
				System.out.println( logPrefix + "节点 " + path + " 被删除" );
			}

		} else if ( KeeperState.Disconnected == keeperState ) {
			System.out.println( logPrefix + "与ZK服务器断开连接" );
		} else if ( KeeperState.AuthFailed == keeperState ) {
			System.out.println( logPrefix + "权限检查失败" );
		} else if ( KeeperState.Expired == keeperState ) {
			System.out.println( logPrefix + "会话失效" );
		}

		System.out.println( "--------------------------------------------" );
	}
	
	
}
