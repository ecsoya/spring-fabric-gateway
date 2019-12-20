package io.github.ecsoya.fabric.json;

public class DefaultFabricJsonConverter implements IFabricJsonConverter {

	public static final DefaultFabricJsonConverter INSTANCE = new DefaultFabricJsonConverter();

	@Override
	public String toString(Object object) {
		return FabricGson.stringify(object);
	}

	@Override
	public <T> T fromString(String string, Class<T> type) {
		return FabricGson.build(string, type);
	}

}
