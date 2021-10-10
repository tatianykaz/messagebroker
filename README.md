# messagebroker
Simple Java Restful API implementing a local ActiveMQ message broker integrated to the application server (Wildfly).

How to configure message broker on Wildfly:

1 - Open the file standalone.xml ($WILDFLY_HOME/standalone/configuration).

2 - Add the activeMQ extension into tag extensions: 

```
<extension module="org.wildfly.extension.messaging-activemq"/>
```

3 - Add resource adapter-ref inside the ejb3 domain tag, as showed below: 

```
<subsystem xmlns="urn:jboss:domain:ejb3:6.0"> 
  <mdb> 
    <resource-adapter-ref resource-adapter-name="${ejb.resource-adapter-name:activemq-ra.rar}"/> 
    <bean-instance-pool-ref pool-name="mdb-strict-max-pool"/> 
  </mdb> ... 
</subsystem xmlns="urn:jboss:domain:ejb3:6.0"> 
```

4 - Add activeMQ subsystem inside <profile>. Copy it from the file standalone-full.xml (also $WILDFLY_HOME/standalone/configuration): 

```
<profile> 
  <subsystem xmlns="urn:jboss:domain:messaging-activemq:8.0"> 
    <server name="default"> <statistics enabled="${wildfly.messaging-activemq.statistics-enabled:${wildfly.statistics-enabled:false}}"/> 
      <security-setting name="#"> <role name="guest" send="true" consume="true" create-non-durable-queue="true" delete-non-durable-queue="true"/> 
      </security-setting> 
      <address-setting name="#" dead-letter-address="jms.queue.DLQ" expiry-address="jms.queue.ExpiryQueue" max-size-bytes="10485760" page-size-bytes="2097152" message-counter- history-day-limit="10"/>
      <http-connector name="http-connector" socket-binding="http" endpoint="http-acceptor"/>
      <http-connector name="http-connector-throughput" socket-binding="http" endpoint="http-acceptor-throughput"> 
        <param name="batch-delay" value="50"/> 
      </http-connector> 
      <in-vm-connector name="in-vm" server-id="0"> 
        <param name="buffer-pooling" value="false"/> 
      </in-vm-connector> <http-acceptor name="http-acceptor" http-listener="default"/> 
      <http-acceptor name="http-acceptor-throughput" http-listener="default"> 
        <param name="batch-delay" value="50"/> <param name="direct-deliver" value="false"/> 
      </http-acceptor> 
      <in-vm-acceptor name="in-vm" server-id="0"> 
      <param name="buffer-pooling" value="false"/> 
      </in-vm-acceptor> 
      <jms-queue name="ExpiryQueue" entries="java:/jms/queue/ExpiryQueue"/> 
      <jms-queue name="DLQ" entries="java:/jms/queue/DLQ"/> 
      <connection-factory name="InVmConnectionFactory" entries="java:/ConnectionFactory" connectors="in-vm"/> 
      <connection-factory name="RemoteConnectionFactory" entries="java:jboss/exported/jms/RemoteConnectionFactory" connectors="http-connector"/> 
      <pooled-connection-factory name="activemq-ra" entries="java:/JmsXA java:jboss/DefaultJMSConnectionFactory" connectors="in-vm" transaction="xa"/> 
    </server> 
  </subsystem> ... 
</profile>
```

5 - In the code above, add our TestQueue jms-queue tag inside the server tag (below ExpiryQueue and DLQ):

```
  <jms-queue name="TestQueue" entries="java:/queue/TestQueue"/>
```

6 - Run your Wildfly and open its management console in http://127.0.0.1:9990/console/ if you want to check the messages our TestQueue is getting ;)

```
    Runtime -> Server -> Monitor (Messaging) -> Server -> Destination (TestQueue)
```
