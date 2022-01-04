package com.it.core.db.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataSet {

	/**
	 * Database Name must have a .db extension
	 * @return
	 */
	String name();
	
	/**
	 * Database version
	 * @return
	 */
	int version() default 1;
	
}
