package com.github.jselby.ej;

/**
 * Enum containing various movement types
 * @author James
 *
 */
public enum FlightTypes {
	/**
	 * Jetpack toggle via creative mode
	 * @Deprecated Not implemented yet
	 */
	@Deprecated
	CREATIVE_TOGGLE,
	/**
	 * Jetpack toggle via pressing crouch
	 */
	CROUCH,
	/**
	 * Jetpack toggle via holding crouch
	 */
	CROUCH_CONSTANT
}
