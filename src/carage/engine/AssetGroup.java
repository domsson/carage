package carage.engine;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Vector3f;

// TODO I have no idea what I'm doing...
public class AssetGroup {
	
	public static final String DEFAULT_ASSET_NAME = "asset";

	HashMap<String, Asset> assets = new HashMap<>();
	
	public AssetGroup() {
		
	}
	
	public void addAsset(Asset asset) {
		assets.put(nextName(), asset);
	}
	
	public void addAsset(Asset asset, String name) {
		assets.put(name,  asset);
	}
	
	public Asset getAsset(String name) {
		return assets.get(name);
	}
	
	public int numberOfAssets() {
		return assets.size();
	}
	
	public Asset[] getAllAssets() {
		Asset[] assetArray = new Asset[assets.size()];
		int i = 0;
		for (Asset asset : assets.values()) {
			assetArray[i++] = asset;
		}
		return assetArray;
	}
	
	public void setPosition(Vector3f position) {
		for (Map.Entry<String, Asset> entry : assets.entrySet()) {
			entry.getValue().setPosition(position);
		}
	}
	
	public void alterPosition(Vector3f position) {
		for (Map.Entry<String, Asset> entry : assets.entrySet()) {
			entry.getValue().alterPosition(position);
		}
	}
	
	public void setRotation(Vector3f rotation) {
		for (Map.Entry<String, Asset> entry : assets.entrySet()) {
			entry.getValue().setPosition(rotation);
		}
	}
	
	public void alterRotation(Vector3f rotation) {
		for (Map.Entry<String, Asset> entry : assets.entrySet()) {
			entry.getValue().alterRotation(rotation);
		}
	}
	
	public void setVelocity(Vector3f velocity) {
		for (Map.Entry<String, Asset> entry : assets.entrySet()) {
			entry.getValue().setPosition(velocity);
		}
	}
	
	private String nextName() {
		return DEFAULT_ASSET_NAME + "-" + assets.size();
	}
	
}
