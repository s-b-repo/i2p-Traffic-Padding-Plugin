// File: plugins/TrafficPaddingPlugin.java
package plugins;

import net.i2p.router.RouterContext;
import net.i2p.router.JobImpl;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrafficPaddingPlugin extends JobImpl {
    private static final Logger LOGGER = Logger.getLogger(TrafficPaddingPlugin.class.getName());

    private final RouterContext context;
    private final Random random = new Random();
    private boolean paddingEnabled = true;
    private int paddingInterval = 5000; // Default interval (ms)
    private int dataSize = 128; // Default data size in bytes

    public TrafficPaddingPlugin(RouterContext ctx) {
        super(ctx);
        this.context = ctx;
    }

    @Override
    public void runJob() {
        if (paddingEnabled) {
            byte[] dummyData = new byte[dataSize];
            random.nextBytes(dummyData);
            try {
                context.tunnelDispatcher().dispatch(dummyData); // Dispatch dummy data
                LOGGER.log(Level.INFO, "Dispatched dummy data of size {0} bytes", dataSize);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to dispatch dummy data", e);
            }
            
            // Requeue job with randomized interval to improve obfuscation
            int randomInterval = paddingInterval + random.nextInt(2000) - 1000; // ±1 second
            requeue(Math.max(1000, randomInterval)); // Minimum 1-second delay
        }
    }

    public void togglePadding(boolean enable) {
        paddingEnabled = enable;
        LOGGER.log(Level.INFO, "Traffic padding {0}", enable ? "enabled" : "disabled");
    }

    public void setPaddingInterval(int intervalMs) {
        this.paddingInterval = intervalMs;
        LOGGER.log(Level.INFO, "Set padding interval to {0} ms", paddingInterval);
    }

    public void setDataSize(int sizeBytes) {
        this.dataSize = sizeBytes;
        LOGGER.log(Level.INFO, "Set data size to {0} bytes", dataSize);
    }

    @Override
    public String getName() {
        return "Enhanced Traffic Padding Plugin";
    }
}
