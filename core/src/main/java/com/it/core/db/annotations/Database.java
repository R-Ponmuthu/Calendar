package com.it.core.db.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * A annotation added to class to generate a Database using {@link DatabaseHelper}
 * 
 * @author Vivek
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Database {

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

	/**
	 * 
	 * @return true if the field with annotation {@link IsID} is auto generated else not
	 */
	boolean autoIncrementID() default false; 
}
