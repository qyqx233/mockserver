package me.ifling.adapter;

import com.ibm.mq.*;
import me.ifling.bean.ConfigAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqActiveAdapterImpl extends ActiveAdapterBase {
    static Logger logger = LoggerFactory.getLogger(MqActiveAdapterImpl.class);
    private int port, ccsid;
    private String qmgrName, hostName, channel, userID, password, sendQName, rcvQName, name;
    private MQQueueManager qMgr;

    public MqActiveAdapterImpl(String name, ConfigAdapter config) {
        this(config.hostName, Integer.parseInt(config.port), config.qmgrName, Integer.parseInt(config.ccsid),
                config.channel, config.sendQName, config.rcvQName);
        this.name = name;
    }

    MqActiveAdapterImpl(String hostname, int port, String qmgrName, int ccsid, String channel, String sendQName, String rcvQName) {
        super();
        this.qmgrName = qmgrName;
        this.ccsid = ccsid;
        this.port = port;
        this.channel = channel;
        this.hostName = hostname;
        this.sendQName = sendQName;
        this.rcvQName = rcvQName;
    }

    public void start() throws Exception {
        MQEnvironment.hostname = this.hostName;
        MQEnvironment.channel = this.channel;
        MQEnvironment.port = this.port;
        MQEnvironment.CCSID = this.ccsid;
        //MQ中拥有权限的用户名
        MQEnvironment.userID = this.userID;
        //用户名对应的密码
        MQEnvironment.password = this.password;
        try {
            qMgr = new MQQueueManager(this.qmgrName);
        } catch (MQException e) {
            throw new Exception(e);
        }
    }

    public void sendMsg(String msgStr) {
        int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
        MQQueue queue = null;
        try {
            // 建立Q1通道的连接
            queue = qMgr.accessQueue(sendQName, openOptions, null, null, null);
            MQMessage msg = new MQMessage();// 要写入队列的消息
            msg.format = MQC.MQFMT_STRING;
            msg.characterSet = ccsid;
            msg.encoding = ccsid;
            // msg.writeObject(msgStr); //将消息写入消息对象中
            msg.writeString(msgStr);
            MQPutMessageOptions pmo = new MQPutMessageOptions();
            msg.expiry = -1; // 设置消息用不过期
            queue.put(msg, pmo);// 将消息放入队列
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (queue != null) {
                try {
                    queue.close();
                } catch (MQException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void receiveMsg() {
        int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
        MQQueue queue = null;
        try {
            queue = qMgr.accessQueue(rcvQName, openOptions, null, null, null);
            logger.info("该队列当前的深度为: {}", queue.getCurrentDepth());
            int depth = queue.getCurrentDepth();
            // 将队列的里的消息读出来
            while (depth-- > 0) {
                MQMessage msg = new MQMessage();// 要读的队列的消息
                MQGetMessageOptions gmo = new MQGetMessageOptions();
                queue.get(msg, gmo);
                logger.debug("消息的大小为: {}", msg.getDataLength());
                String content = msg.readStringOfByteLength(msg.getDataLength());
                logger.debug("消息的内容: {}\n", content);
                this.handle(content);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (queue != null) {
                try {
                    queue.close();
                } catch (MQException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        MqActiveAdapterImpl mq = new MqActiveAdapterImpl("", 3000, "", 819, "", "", "");
        mq.start();
        mq.sendMsg("我来测试一下");
//        mq.receiveMsg();
    }
}
