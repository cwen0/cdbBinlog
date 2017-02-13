package binlog;

import com.alibaba.fastjson.JSONReader;
import com.qcloud.dts.context.SubscribeContext;
import com.qcloud.dts.message.ClusterMessage;
import com.qcloud.dts.message.DataMessage;
import com.qcloud.dts.subscribe.ClusterListener;
import com.qcloud.dts.subscribe.DefaultSubscribeClient;
import com.qcloud.dts.subscribe.SubscribeClient;

import java.io.FileReader;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Config config = null;
        try {
            JSONReader reader = new JSONReader(new FileReader("config.json"));
            config = reader.readObject(Config.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SubscribeContext context = new SubscribeContext();

        context.setSecretId(config.getId());
        context.setSecretKey(config.getKey());
        context.setServiceIp(config.getIp());
        context.setServicePort(config.getPort());

        SubscribeClient client = null;
        try {
            client = new DefaultSubscribeClient(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClusterListener listener = new ClusterListener() {
            @Override
            public void notify(List<ClusterMessage> messages) throws Exception {
                for(ClusterMessage m:messages) {
                    for(DataMessage.Record.Field f:m.getRecord().getFieldList()) {
                        if(f.getFieldname().equals("id")) {
                            System.out.println("seq:"+f.getValue());
                        }
                    }
                    m.ackAsConsumed();
                }
            }

            @Override
            public void onException(Exception e) {
                System.out.println("listen exception" + e);
            }
        };

        try {
            client.addClusterListener(listener);
        }catch (Exception e) {
            e.printStackTrace();
        }

        client.askForGUID(config.getGuid());

        try {
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
