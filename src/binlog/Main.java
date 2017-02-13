package binlog;


import com.qcloud.dts.context.SubscribeContext;
import com.qcloud.dts.message.ClusterMessage;
import com.qcloud.dts.message.DataMessage.Record;
import com.qcloud.dts.subscribe.ClusterListener;
import com.qcloud.dts.subscribe.DefaultSubscribeClient;
import com.qcloud.dts.subscribe.SubscribeClient;

import java.util.List;
import config.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);
    SubscribeClient client = null;

    public void initClient() {
        SubscribeContext context = new SubscribeContext();
        context.setSecretId(Config.secretID);
        context.setSecretKey(Config.secretKey);
        context.setServiceIp(Config.cdbIP);
        context.setServicePort(Config.cdbPort);

        try {
            client = new DefaultSubscribeClient(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            client.addClusterListener(listener);
            client.askForGUID(Config.guid);
            client.start();
            log.info("The service starts successfully and is waiting for data");
        }catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    ClusterListener listener = new ClusterListener() {
        @Override
        public void notify(List<ClusterMessage> messages) throws Exception {
            for(ClusterMessage m:messages) {
                Record record = m.getRecord();
                try{
                    messageHandler(record);
                    m.ackAsConsumed();
                }catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            }
        }

        public void noException(Exception e) {
            e.printStackTrace();
        }
    };


    public void messageHandler(Record record) {
        MessageHandler handler = new MessageHandler();
        switch (record.getOpt()) {
            case INSERT:
                handler.handleInsert(record);
                break;
            case UPDATE:
                handler.handleUpdate(record);
                break;
            case DELETE:
                handler.handleDelete(record);
                break;
            case DDL:
                handler.handleDdl(record);
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) throws Exception {
        new Main().initClient();
    }
}
