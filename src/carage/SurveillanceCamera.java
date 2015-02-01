package carage;

import carage.engine.Camera;

public class SurveillanceCamera extends Camera {
	
	public static final float DEFAULT_PAN_LIMIT_LEFT   = (float) Math.toRadians(45);		// Positive angle = Panning left
	public static final float DEFAULT_PAN_LIMIT_RIGHT  = (float) Math.toRadians(45);		// Negative angle = Panning right
	public static final float DEFAULT_PAN_STEP         = (float) Math.toRadians(0.25f);
	
	public static final float DEFAULT_PITCH_LIMIT_UP   = (float) Math.toRadians(0);		// Positive angle = Looking up
	public static final float DEFAULT_PITCH_LIMIT_DOWN = (float) Math.toRadians(45);	// Negative angle = Looking down
	public static final float DEFAULT_PITCH_STEP       = (float) Math.toRadians(0.25f);
	
	public static final int PAN_NONE  =  0;
	public static final int PAN_LEFT  =  1;
	public static final int PAN_RIGHT = -1;
	
	public static final float PAN_PAUSE_TIME = 120;
	
	private boolean pitchingAllowed = true;
	private float pitchingStep      = DEFAULT_PITCH_STEP;
	private float pitchingLimitUp   = DEFAULT_PITCH_LIMIT_UP;
	private float pitchingLimitDown = DEFAULT_PITCH_LIMIT_DOWN;
	
	private int panningStatus           = PAN_NONE;
	private int panningStatusBefore     = PAN_NONE;
	private float panningPausedFor      = 0;
	private boolean autoPanning         = false;
	private float panningStep       = DEFAULT_PAN_STEP;
	private float panningLimitLeft  = DEFAULT_PAN_LIMIT_LEFT;
	private float panningLimitRight = DEFAULT_PAN_LIMIT_RIGHT;
	

	public SurveillanceCamera() {
		
	}
	
	public void tick(float delta) {
		if (autoPanning) { autoPan(delta); }
	}
	
	public void toggleAutoPanning() {
		System.out.println("toggle");
		if (autoPanning == true) { deactivateAutoPanning(); }
		else { activateAutoPanning(); }
	}
	
	public void deactivateAutoPanning() {
		autoPanning = false;
		panningStatusBefore = panningStatus;
		panningStatus = PAN_NONE;
	}
	
	public void activateAutoPanning(float radiansToLeft, float radiansToRight, float radiansPerSecond) {
		autoPanning = true;
		panningStatus = PAN_RIGHT;	// TODO start with panning left or right? doesn't really matter though...
		panningLimitLeft  = radiansToLeft;
		panningLimitRight = radiansToRight;
		panningStep = radiansPerSecond;
	}
	
	public void activateAutoPanning(float radiansToLeft, float radiansToRight) {
		activateAutoPanning(radiansToLeft, radiansToRight, panningStep);
	}
	
	public void activateAutoPanning() {
		autoPanning = true;
		panningStatus = (panningStatusBefore == PAN_NONE) ? PAN_RIGHT : panningStatusBefore; // TODO start with panning left or right?
	}
	
	public boolean autoPanningIsActivated() {
		return autoPanning;
	}
	
	public void forbidPitching() {
		pitchingAllowed = false;
	}
	
	public void allowPitching(float radiansUp, float radiansDown) { 
		pitchingAllowed = true;
		pitchingLimitUp = radiansUp;
		pitchingLimitDown = radiansDown;
	}
	
	public void allowPitching() {
		pitchingAllowed = true;
	}
	
	public void pitchUp(float radiansDelta) {
		if (!pitchingAllowed) { return; }
		float newRotation = rotation.x + radiansDelta; 
		rotation.x = (newRotation > pitchingLimitUp) ? pitchingLimitUp : newRotation;
	}
	
	public void pitchDown(float radiansDelta) {
		if (!pitchingAllowed) { return; }
		float newRotation = rotation.x - radiansDelta; 
		rotation.x = (newRotation < -pitchingLimitDown) ? -pitchingLimitDown : newRotation;
	}
	
	public void pitchUp() {
		pitchUp(pitchingStep);
	}
	
	public void pitchDown() {
		pitchDown(pitchingStep);
	}
	
	public void panLeft(float radiansDelta) {
		if (autoPanning == true) { return; }
		float newRotation = rotation.y + radiansDelta;
		rotation.y = (newRotation > panningLimitLeft) ? -panningLimitLeft : newRotation;
	}
	
	public void panLeft() {
		panLeft(panningStep);
	}
	
	public void panRight(float radiansDelta) {
		if (autoPanning == true) { return; }
		float newRotation = rotation.y - radiansDelta;
		rotation.y = (newRotation < -panningLimitRight) ? -panningLimitRight : newRotation;
	}
	
	public void panRight() {
		panRight(panningStep);
	}
	
	private void autoPan(float delta) {
		if (autoPanning == false || panningStatus == PAN_NONE) { return; }
		if (panningPausedFor > 0) { panningPausedFor -= delta; }
		if (panningPausedFor > 0) { return;	}
		
		float panBy = panningStep * delta;
		float newRotation = rotation.y + (panningStatus * panBy);
		
		if (panningStatus == PAN_LEFT) {
			if (newRotation > panningLimitLeft) { // We're at the far left
				rotation.y = panningLimitLeft;
				panningStatus = PAN_RIGHT;
				panningPausedFor = PAN_PAUSE_TIME;
			}
			else {
				rotation.y = newRotation;
			}
		}
		else if (panningStatus == PAN_RIGHT) { // We're at the far right
			if (newRotation < -panningLimitRight) {
				rotation.y = -panningLimitRight;
				panningStatus = PAN_LEFT;
				panningPausedFor = PAN_PAUSE_TIME;
			}
			else {
				rotation.y = newRotation;
			}
		}
	}

}
