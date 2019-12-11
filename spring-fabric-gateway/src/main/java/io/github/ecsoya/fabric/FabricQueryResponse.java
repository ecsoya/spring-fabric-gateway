package io.github.ecsoya.fabric;

import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.hyperledger.fabric.sdk.ChaincodeResponse.Status;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import io.github.ecsoya.fabric.json.JsonUtils;

public class FabricQueryResponse<T> extends FabricResponse {

	public final T data;

	public FabricQueryResponseMetadata metadata;

	public FabricQueryResponse(int status, String errorMsg, T data) {
		super(status, errorMsg);
		this.data = data;
	}

	public FabricQueryResponseMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(FabricQueryResponseMetadata metadata) {
		this.metadata = metadata;
	}

	@Override
	public boolean isOk(boolean all) {
		if (all) {
			return data != null && super.isOk(all);
		}
		return super.isOk(all);
	}

	public static <T> FabricQueryResponse<T> failure(String errorMsg) {
		return new FabricQueryResponse<>(FAILURE, errorMsg, null);
	}

	public static <T> FabricQueryResponse<T> success(T data) {
		return new FabricQueryResponse<T>(SUCCESS, null, data);
	}

	public static <T> FabricQueryResponse<T> build(T data) {
		if (data == null) {
			return failure(null);
		}
		return success(data);
	}

	public static <T> FabricQueryResponse<T> create(ProposalResponse res, Class<T> type) {
		Status status = res.getStatus();
		if (status != Status.SUCCESS) {
			return failure(res.getMessage());
		} else {
			if (type != null) {
				try {
					int chaincodeStatus = res.getChaincodeActionResponseStatus();
					if (chaincodeStatus != -1) {

						byte[] payload = res.getChaincodeActionResponsePayload();
						return create(payload, type);
					} else {
						return failure("Chaincode executed failure.");
					}
				} catch (InvalidArgumentException e) {
					return failure("Chaincode error: " + e.getMessage());
				}
			} else {
				return success(null);
			}
		}
	}

	public static <T> FabricQueryResponse<T> create(byte[] payload, Class<T> type) {
		if (payload == null) {
			return success(null);
		}
		T payloadData = parsePayload(new String(payload, Charset.forName("utf-8")), type);
		return success(payloadData);
	}

	@SuppressWarnings("unchecked")
	private static <T> T parsePayload(String value, Class<T> type) {
		if (value == null) {
			return null;
		}
		if (String.class == type) {
			return (T) value;
		} else if (Number.class.isAssignableFrom(type)) {
			try {
				return (T) NumberFormat.getInstance().parse(value);
			} catch (ParseException e) {
				return null;
			}
		} else if (Boolean.class == type || boolean.class == type) {
			try {
				return (T) Boolean.valueOf(value);
			} catch (Exception e) {
				return null;
			}
		} else if (!value.equals("")) {
			return JsonUtils.fromJson(value, type);
		}
		return null;
	}

	public static <T> FabricQueryResponse<List<T>> many(ProposalResponse res, Class<T> type) {
		Status status = res.getStatus();
		if (status != Status.SUCCESS) {
			return failure(res.getMessage());
		} else {
			if (type != null) {
				try {
					int chaincodeStatus = res.getChaincodeActionResponseStatus();
					if (chaincodeStatus != -1) {
						byte[] payload = res.getChaincodeActionResponsePayload();
						return parsePayloadMany(new String(payload, Charset.forName("utf-8")), type);
					} else {
						return failure("Chaincode executed failure.");
					}
				} catch (InvalidArgumentException e) {
					return failure("Chaincode error: " + e.getMessage());
				}
			} else {
				return success(null);
			}
		}
	}

	public static <T> FabricQueryResponse<List<T>> many(byte[] payloads, Class<T> type) {
		return parsePayloadMany(new String(payloads, Charset.forName("utf-8")), type);
	}

	private static <T> FabricQueryResponse<List<T>> parsePayloadMany(String json, Class<T> type) {
		JsonParser parser = new JsonParser();
		try {
			JsonElement element = parser.parse(json);

			JsonArray array = null;
			JsonElement meta = null;
			if (element.isJsonArray()) {
				// 普通查询模式
				array = element.getAsJsonArray();
			} else if (element.isJsonObject()) {
				// 分页查询模式
				JsonObject object = element.getAsJsonObject();
				JsonElement data = object.get("data");
				if (data.isJsonArray()) {
					array = data.getAsJsonArray();
				}

				meta = object.get("meta");
			}

			T[] values = JsonUtils.fromJsonArray(array, type);

			FabricQueryResponse<List<T>> res = success(Arrays.asList(values));

			if (meta != null) {
				res.setMetadata(JsonUtils.fromJson(meta, FabricQueryResponseMetadata.class));
			}
			return res;

		} catch (JsonSyntaxException e) {
			return FabricQueryResponse.failure(e.getMessage());
		}
	}
}
