package fr.clue.cookieac.check;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CheckData {

     boolean enabled() default true;

     boolean experimental() default false;

     double punishmentVL() default 20.0;

     String name();

     String type() default "Z";

     String description() default "hack";

}