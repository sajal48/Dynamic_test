import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Feature1 {
	public Object run(Map<String,Object> reqMap) {
		Integer a = (Integer) reqMap.get("a");
		Integer b = (Integer) reqMap.get("b");
		Map<String,Object> responseMap = new HashMap<>();
		responseMap.put("sum",a+b);
		responseMap.put("sub",a-b);
		return responseMap;
	}

}