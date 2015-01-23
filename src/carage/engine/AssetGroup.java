package carage.engine;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Vector3f;

// TODO This is a horrible idea. It doesn't allow for child assets to have children themselves. What has the world come to!?
public class AssetGroup {
	
	public static final String DEFAULT_ASSET_NAME = "asset";

	Asset parentAsset = null;
	HashMap<String, Asset> childAssets = new HashMap<>();
	
	public AssetGroup() {
		
	}
	
	public AssetGroup(Asset parentAsset) {
		this.parentAsset = parentAsset;
	}
	
	public void setParentAsset(Asset parentAsset) {
		this.parentAsset = parentAsset;
	}
	
	public void addChildAsset(Asset asset) {
		childAssets.put(nextName(), asset);
	}
	
	public void addChildAsset(Asset asset, String name) {
		childAssets.put(name,  asset);
	}
	
	public Asset getParentAsset() {
		return parentAsset;
	}
	
	public Asset getChildAsset(String name) {
		return childAssets.get(name);
	}
	
	public int numberOfAssets() {
		return childAssets.size() + 1;
	}
	
	public int numberOfChildAssets() {
		return childAssets.size();
	}
	
	public Asset[] getAllChildAssets() {
		Asset[] childAssetArray = new Asset[childAssets.size()];
		int i = 0;
		for (Asset childAsset : childAssets.values()) {
			childAssetArray[i++] = childAsset;
		}
		return childAssetArray;
	}
	
	public Asset[] getAllAssets() {
		Asset[] assetArray = new Asset[childAssets.size()+1];
		int i = 0;
		assetArray[i++] = parentAsset;
		for (Asset asset : childAssets.values()) {
			assetArray[i++] = asset;
		}
		return assetArray;
	}
	
	public void setPosition(Vector3f position) {
		parentAsset.setPosition(position);
	}
	
	public void setChildPositions(Vector3f position) {
		for (Map.Entry<String, Asset> entry : childAssets.entrySet()) {
			entry.getValue().setPosition(position);
		}
	}
	
	public void alterPosition(Vector3f position) {
		parentAsset.alterPosition(position);
	}
	
	public void alterChildPositions(Vector3f position) {
		for (Map.Entry<String, Asset> entry : childAssets.entrySet()) {
			entry.getValue().alterPosition(position);
		}
	}
	
	public void setRotation(Vector3f rotation) {
		parentAsset.setRotation(rotation);
	}
	
	public void setChildRotations(Vector3f rotation) {
		for (Map.Entry<String, Asset> entry : childAssets.entrySet()) {
			entry.getValue().setPosition(rotation);
		}
	}
	
	public void alterRotation(Vector3f rotation) {
		parentAsset.alterRotation(rotation);
	}
	
	public void alterChildRotations(Vector3f rotation) {
		for (Map.Entry<String, Asset> entry : childAssets.entrySet()) {
			entry.getValue().alterRotation(rotation);
		}
	}
	
	public void setVelocity(Vector3f velocity) {
		parentAsset.setVelocity(velocity);
	}
	
	public void setChildVelocities(Vector3f velocity) {
		for (Map.Entry<String, Asset> entry : childAssets.entrySet()) {
			entry.getValue().setPosition(velocity);
		}
	}
	
	private String nextName() {
		return DEFAULT_ASSET_NAME + "-" + childAssets.size();
	}
	
}
