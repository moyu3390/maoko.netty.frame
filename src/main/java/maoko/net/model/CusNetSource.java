package maoko.net.model;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import io.netty.channel.Channel;
import maoko.common.model.net.CusHostAndPort;
import maoko.net.ifs.IBytesBuild;
import maoko.net.ifs.INetChanel;
import maoko.net.util.SendDataUtil;

/**
 * 自定义网络源
 * 
 * @author fanpei
 *
 */
public class CusNetSource implements INetChanel {
	private Channel chanel;
	private Map<String, Object> extraSettings;// 初始容量3

	public CusNetSource(Channel chanel) {
		this.chanel = chanel;
		//this.extraSettings = new HashMap<String, Object>(3);
	}

	@Override
	public boolean sendData(IBytesBuild netdata) throws Exception {
		return sendData(netdata, false);
	}

	@Override
	public boolean sendData(IBytesBuild netdata, boolean isWait) throws Exception {
		return SendDataUtil.sendData(chanel, netdata, isWait);
	}

	@Override
	public boolean sendData(byte[] netdatas, boolean isWait) throws Exception {
		return SendDataUtil.sendData(chanel, netdatas, isWait);
	}

	@Override
	public boolean sendData(byte[] datas) throws Exception {
		return sendData(datas, false);
	}

	@Override
	public Map<String, Object> getSettings() {
		return extraSettings;
	}

	@Override
	public void close() {
		chanel.close();
	}

	@Override
	public CusHostAndPort getRAddress() {
		InetSocketAddress addr = (InetSocketAddress) chanel.remoteAddress();
		return new CusHostAndPort(addr.getHostString(), addr.getPort());
	}

	@Override
	public int getLPort() {
		return ((InetSocketAddress) chanel.localAddress()).getPort();
	}

	@Override
	public String getChanelId() {
		return chanel.id().asShortText();
	}

	@Override
	public String getRIpPort() {
		InetSocketAddress addr = getAddress();
		return new StringBuilder(addr.getHostString()).append(":").append(addr.getPort()).toString();
	}

	@Override
	public String getRIP() {
		InetSocketAddress addr = getAddress();
		return addr.getHostString();
	}

	@Override
	public int getRPort() {
		InetSocketAddress addr = getAddress();
		return addr.getPort();
	}

	@Override
	public Channel getChannel() {
		return chanel;
	}

	private InetSocketAddress addr;

	private InetSocketAddress getAddress() {
		if (addr == null) {
			addr = (InetSocketAddress) chanel.remoteAddress();
		}
		return addr;
	}

	public void setNettyChanel(Channel chanel) {
		this.chanel = chanel;
	}

	/**
	 * 通道是否建立 连接状态
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return chanel.isActive();
	}

	private boolean longConnect = false;// 是否是长连接

	/**
	 * 是否是长连接
	 * 
	 * @return
	 */
	public boolean isLongConnect() {
		return longConnect;
	}

}
