import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        String json = "{\n" +
                "    heterogeneous: [\"Nicolas\", \"Martin\", 6],\n" +
                "    homogeneous: [\"Lol\", \"CS:GO\", \"MC\"]\n" +
                "}";

        Gson gson = new Gson();

        gson.fromJson(json, Map.class).forEach((k, v) -> {
            System.out.println(((ArrayList<?>) v).toArray().getClass());
        });
    }
}