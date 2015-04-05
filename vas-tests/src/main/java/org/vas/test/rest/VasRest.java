package org.vas.test.rest;

/**
 * Marker interface for Guice which indicate a specific {@link Rest} class that
 * will prefix relative urls by the rest location of the contextual Vas server.
 *
 */
public interface VasRest extends Rest {}
