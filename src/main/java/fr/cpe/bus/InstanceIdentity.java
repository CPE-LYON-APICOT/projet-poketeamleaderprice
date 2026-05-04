package fr.cpe.bus;

/**
 * Provides a stable identity for the current running process.
 * <p>
 * Priority:
 * <ol>
 *   <li>{@code INSTANCE_NAME} environment variable, if set</li>
 *   <li>Fallback to the current process id</li>
 * </ol>
 */
public final class InstanceIdentity {

    private static final String INSTANCE_ID = resolveInstanceId();

    private InstanceIdentity() {
    }

    public static String get() {
        return INSTANCE_ID;
    }

    private static String resolveInstanceId() {
        String instanceName = System.getenv("INSTANCE_NAME");
        if (instanceName != null && !instanceName.isBlank()) {
            return instanceName.trim();
        }


        try {
            return "pid-" + ProcessHandle.current().pid();
        } catch (Exception e) {
            return "instance-unknown";
        }
    }
}

