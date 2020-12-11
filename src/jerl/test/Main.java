package jerl.test;

public class Main {
	public static void main(String... args) {
		System.out.println("KV");
		KV<String, String> kv = new KV<>();
		kv.put("k", "v");
		System.out.println(kv.get("k"));
		kv.put("k2", "v2");
		System.out.println(kv.get("k2"));
		kv.put("k", "not v");
		System.out.println(kv.get("k"));
		System.out.println(kv.get("2"));
		kv.clear();
		System.out.println(kv.get("k"));

		System.out.println("KV8");
		KV8<String, String> kv8 = new KV8<>();
		kv8.put("k", "v");
		System.out.println(kv8.get("k"));
		kv8.put("k2", "v2");
		System.out.println(kv8.get("k2"));
		kv8.put("k", "not v");
		System.out.println(kv8.get("k"));
		System.out.println(kv8.get("2"));
		kv8.clear();
		System.out.println(kv8.get("k"));
	}
}
