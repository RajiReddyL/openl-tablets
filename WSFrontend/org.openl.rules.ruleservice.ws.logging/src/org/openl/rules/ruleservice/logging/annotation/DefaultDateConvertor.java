package org.openl.rules.ruleservice.logging.annotation;

import java.util.Date;

import org.openl.rules.ruleservice.logging.Convertor;

public final class DefaultDateConvertor implements Convertor<Object, Date> {
    @Override
    public Date convert(Object value) {
        throw new UnsupportedOperationException();
    }
}
