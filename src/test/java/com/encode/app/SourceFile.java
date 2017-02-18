package com.encode.app;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SourceFile {

    public enum FileFormat {TXT, EXCEL}

    FileFormat format();
    String path();

}
