package maoko.net.sconectclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import maoko.common.StringUtil;
import maoko.common.log.IWriteLog;
import maoko.common.log.Log4j2Writer;
import maoko.net.conf.ConfigClient;
import maoko.net.model.ClientChanel;

/**
 * 重连检测线程
 *
 * @author fanpei
 * @date 2018-09-09 23:43
 */
public class ClientCheckConTd implements Runnable {
    private static final IWriteLog log = new Log4j2Writer(ClientCheckConTd.class);

    private ClientChanel clientChanel;// 客户端信息
    private Bootstrap bstrap;
    private String ip;
    private int port;
    private long lastHeatBeat = 0;

    public ClientCheckConTd(Bootstrap bstrap, ClientChanel chanel, String ip, int port) {
        this.bstrap = bstrap;
        this.clientChanel = chanel;
        this.ip = ip;
        this.port = port;
    }

    String reconectTdName;

    @Override
    public void run() {

        if (StringUtil.isStringNull(reconectTdName)) {
            reconectTdName = "客户端重连检测 ";
            Thread.currentThread().setName(reconectTdName);
        }

        if (!clientChanel.isRunFlag())
            return;
        try {
            if (!clientChanel.getListener().getNetSource().isConnected()) {
                clientChanel.getListener().getNetSource().close();// 防止二次连接
                reConnect();
            }
            if (SConectClient.getHeartData() == null) //== null 不重连
            {
                return;
            } else if (System.currentTimeMillis() - lastHeatBeat > ConfigClient.HEARBEAT_INTERVAL) {
                sendHeartBeat();
                lastHeatBeat = System.currentTimeMillis();
                log.debug("send heartbeat to server:{}:{}", ip, port);
            }
            // Thread.sleep(ConfigClient.CONNET_RETRY_INTERVAL);
        } catch (Exception e) {
            log.error("心跳维持和检测异常", e);
        }
    }

    /**
     * 关闭连接
     */
    public void closeConect() {
        clientChanel.setRunFlag(false);
        clientChanel.close();
    }

    /**
     * 断线重连
     *
     * @throws InterruptedException
     */
    public void reConnect() throws InterruptedException {
        ChannelFuture f = bstrap.connect(ip, port); // 连接服务端
        f.addListener(new GenericFutureListener<Future<? super Void>>() {

            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    clientChanel.updateChanel(f.channel());// 更新
                    log.info("与服务器{} :{} 重新连接建立成功...", ip, port);
                } else {
                    log.info("与服务器{} :{}重新连接失败...", ip, port);
                }
            }
        });
        f.await();
    }

    /**
     * 发送心跳包
     *
     */
    private void sendHeartBeat() {
        try {
            clientChanel.sendData(SConectClient.getHeartData(), true);
        } catch (Exception e) {
            log.warn("心跳维持发送异常:{}", clientChanel.getListener().getNetSource().getRIpPort(), e);
        }
    }
}
