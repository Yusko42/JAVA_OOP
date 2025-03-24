import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.HashMap;

public class ExecutionContext {
    private final Map<String, Double> parameters = new HashMap<>();
    private final Deque<Double> stack = new ArrayDeque<>();

    public final Map<String, Double> getParameters(){
        return parameters;
    }

    public final Deque<Double> getStack(){
        return stack;
    }

}
