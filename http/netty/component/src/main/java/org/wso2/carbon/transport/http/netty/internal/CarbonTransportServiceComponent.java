package org.wso2.carbon.transport.http.netty.internal;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.messaging.CarbonTransportInitializer;

import java.util.Map;

/**
 * OSGi declarative service for initializer.
 * @since 5.0.0
 */
@Component(
        name = "org.wso2.carbon.transport.http.netty.internal.CarbonTransportServiceComponent",
        immediate = true
)
public class CarbonTransportServiceComponent {
    public static final String CHANNEL_ID_KEY = "channel.id";

    @Reference(
            name = "transport-initializer",
            service = CarbonTransportInitializer.class,
            cardinality = ReferenceCardinality.OPTIONAL,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "removeTransportInitializer"
    )
    protected void addTransportInitializer(CarbonTransportInitializer serverInitializer, Map<String, ?> ref) {
        NettyTransportDataHolder.getInstance()
                .addNettyChannelInitializer((String) ref.get(CHANNEL_ID_KEY), serverInitializer);
    }

    protected void removeTransportInitializer(CarbonTransportInitializer serverInitializer) {
       //// TODO: 11/30/15
    }
}