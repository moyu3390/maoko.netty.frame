package soft.net.model;

import java.util.EventListener;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import soft.net.ifs.IByteBuff;
import soft.net.protocol.IDecoder;
import soft.net.protocol.IProtocol;
import soft.net.protocol.MyDecoder;

/**
 * 网络事件监视器
 * 
 * @author fanpei
 *
 */
public abstract class NetEventListener implements EventListener {
	protected IDecoder decoder;
	protected CusNetSource channel;// 连接链路

	public NetEventListener(SocketChannel ch) {
		super();
		this.channel = new CusNetSource(ch);
		this.decoder = new MyDecoder(this);
	}

	public CusNetSource getNetSource() {
		return channel;
	}

	public void updateChanel(Channel ch) {
		this.channel.setNettyChanel(ch);
	}

	public void release() {
		if (channel != null)
			channel.close();
	}

	/**
	 * 网络连接建立
	 */
	public abstract void chanelConect();

	/**
	 * 接收数据处理
	 * 
	 * @param buff
	 */
	public abstract void dataReciveEvent(IByteBuff buff);

	/**
	 * 连接断开
	 */
	public abstract void closeEvent();

	/**
	 * 获取一个协议对象
	 * 
	 * @return
	 */
	public abstract IProtocol getProtocol();

}
