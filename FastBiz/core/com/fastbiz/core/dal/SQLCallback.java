package com.fastbiz.core.dal;

import java.util.List;

public interface SQLCallback<T> {

	public T received(List<?> resultList);
}
