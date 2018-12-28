package soft.net.model;

import io.netty.channel.Channel;
import soft.common.ExceptionUtil;
import soft.common.log.IWriteLog;
import soft.common.log.Log4j2Writer;
import soft.net.ifs.IBytesBuild;

/**
 * 客户端长连接链路
 * 
 * @author fanpei
 * @date 2018-09-10 03:50
 *
 */
public class ClientChanel extends AChanelID {
	private static final IWriteLog log = new Log4j2Writer(ClientChanel.class);

	private NetEventListener listener;
	private boolean runFlag = true; // 运行标志

	public boolean isRunFlag() {
		return runFlag;
	}

	public void setRunFlag(boolean runFlag) {
		this.runFlag = runFlag;
	}

	public NetEventListener getListener() {
		return listener;
	}

	/**
	 * 更新链路
	 * 
	 * @param source
	 */
	public void updateChanel(Channel ch) {
		this.listener.updateChanel(ch);
	}

	public ClientChanel() {
	}

	public ClientChanel(NetEventListener listener) {
		this.listener = listener;
	}

	public boolean sendData(IBytesBuild data, boolean isWait) {
		try {
			listener.sendData(data, isWait);
			return true;
		} catch (Exception e) {
			log.warn("发送数据异常:{}", ExceptionUtil.getCauseMessage(e));
		}
		return false;
	}


	public boolean sendData(IBytesBuild data) {
		try {
			return listener.sendData(data);
		} catch (Exception e) {
			log.warn("发送数据异常:{}", ExceptionUtil.getCauseMessage(e));
		}
		return false;
	}


	public boolean sendData(byte[] netdatas, boolean isWait) {
		try {
			return listener.sendData(netdatas, isWait);
		} catch (Exception e) {
			log.warn("发送数据异常:{}", ExceptionUtil.getCauseMessage(e));
		}
		return false;
	}


	public boolean sendData(byte[] datas) {
		try {
			return listener.sendData(datas);
		} catch (Exception e) {
			log.warn("发送数据异常:{}", ExceptionUtil.getCauseMessage(e));
		}
		return false;
	}

	public void close() {
		listener.release();
	}

}
