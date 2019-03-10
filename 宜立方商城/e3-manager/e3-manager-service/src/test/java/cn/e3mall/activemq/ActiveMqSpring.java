package cn.e3mall.activemq;


import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;


public class ActiveMqSpring {

    @Test
    public void sendMessage(){
        //初始化Spring容器
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");

        //从容器中获得jmsTemplate对象
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        //从容器中获得一个Destination对象
        Destination destination = (Destination) context.getBean("queueDestination");
        //发送消息
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage();
            }
        });
    }
}
