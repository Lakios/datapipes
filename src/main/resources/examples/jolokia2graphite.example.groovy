package examples

import org.springframework.scheduling.support.CronTrigger
import org.stropa.data.bridge.jolokia.SimpleDataBridge
import org.stropa.data.collect.jolokia.JolokiaCollector
import org.stropa.data.collect.jolokia.JolokiaDataRequest
import org.stropa.data.collect.jolokia.auth.CustomHeaderDigestAuthenticator
import org.stropa.data.schedule.DataTransferTask
import org.stropa.data.send.graphite.GraphiteSender
import org.stropa.data.send.KeyReplaceTransformer

beans {

    myCollector(JolokiaCollector) {
        jolokiaUrl = 'http://localhost:10987/bpcservlet/jmx/metrics'
        authenticator = new CustomHeaderDigestAuthenticator()
        authenticator.secret = 'asjdhsaf783gaugsfy92hf'
    }
    mySender(GraphiteSender) {
        host = 'b64bacd9.carbon.hostedgraphite.com'
        port = 2003
    }

    jolokia2MyHostedGraphite(SimpleDataBridge) {
        collector = myCollector
        sender = mySender
        transformer = new KeyReplaceTransformer()
        transformer.prefix = "73a6d152-3d94-4803-9811-d37c7b7bfc2f"
    }

    myTask(DataTransferTask) {
        dataBridge = jolokia2MyHostedGraphite
        trigger = new CronTrigger("0 0/1 * 1/1 * ?")
        dataRequest = new JolokiaDataRequest(
                null, null, "org.apache.http.impl.conn.ApacheHttpClientPoolInfo:type=ApacheHttpClientPoolInfo")
    }

}