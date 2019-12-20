package io.github.ecsoya.fabric.json;

public interface IFabricJsonConverter {

	String toString(Object object);

	<T> T fromString(String string, Class<T> type);

	static IFabricJsonConverter defaultConverter() {
		return DefaultFabricJsonConverter.INSTANCE;
	}
}
