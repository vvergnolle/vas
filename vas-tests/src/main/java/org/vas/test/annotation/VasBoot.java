package org.vas.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.vas.launcher.Env;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface VasBoot {
	
	boolean custom() default false;
	VasEnv env() default @VasEnv;
	VasConf conf() default @VasConf;
	Env.Profile profile() default Env.Profile.SERVER;
}