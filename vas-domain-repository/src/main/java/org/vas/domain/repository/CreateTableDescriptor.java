package org.vas.domain.repository;

import org.vas.commons.utils.Ordered;

public interface CreateTableDescriptor extends Ordered {

  Class<?> domain();
}
