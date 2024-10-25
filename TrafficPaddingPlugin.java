// File: plugins/TrafficPaddingPlugin.java
package plugins;

import net.i2p.router.Router;
import net.i2p.router.JobImpl;
import net.i2p.router.RouterContext;
import java.util.Random;

public class TrafficPaddingPlugin extends JobImpl {
    private final RouterContext context;
    private final Random random = new Random();
    private boolean paddingEnabled = true; // Toggle padding on or off

    public TrafficPaddingPlugin(RouterContext ctx) {
        super(ctx);
        this.context = ctx;
    }

    @Override
    public void runJob() {
        if (paddingEnabled) {
            byte[] dummyData = new byte[128]; // Generate 128 bytes of dummy data
            random.nextBytes(dummyData);
            context.tunnelDispatcher().dispatch(dummyData); // Send to tunnel
            requeue(5000); // Queue dummy data every 5 seconds
        }
    }

    public void togglePadding(boolean enable) {
        paddingEnabled = enable;
    }

    @Override
    public String getName() {
        return "Traffic Padding Plugin";
    }
}
