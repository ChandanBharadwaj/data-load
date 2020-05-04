/*
 * Copyright 2006-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chaitu.poi;

/**
 * {@code RowSetFactory} implementation which constructs a {@code DefaultRowSet} instance and
 * {@code DefaultRowSetMetaData} instance. The latter will have the {@code ColumnNameExtractor} configured
 * on this factory set (default {@code RowNumberColumnNameExtractor}.
 *
 * @author Marten Deinum
 * @since 0.5.0
 */
public class DefaultRowSetFactory implements RowSetFactory {

    private ColumnNameExtractor columnNameExtractor = new RowNumberColumnNameExtractor();

    @Override
    public RowSet create(Sheet sheet) {
        DefaultRowSetMetaData metaData = new DefaultRowSetMetaData(sheet);
        metaData.setColumnNameExtractor(columnNameExtractor);
        return new DefaultRowSet(sheet, metaData);
    }

    public void setColumnNameExtractor(ColumnNameExtractor columnNameExtractor) {
        this.columnNameExtractor = columnNameExtractor;
    }
}
