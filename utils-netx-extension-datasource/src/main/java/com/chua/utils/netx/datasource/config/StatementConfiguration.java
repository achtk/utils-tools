package com.chua.utils.netx.datasource.config;

/**
 * 配置
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/20
 */
public class StatementConfiguration {
    private final Integer fetchDirection;
    private final Integer fetchSize;
    private final Integer maxFieldSize;
    private final Integer maxRows;
    private final Integer queryTimeout;

    /**
     * Constructor for <code>StatementConfiguration</code>.  For more flexibility, use {@link Builder}.
     *
     * @param fetchDirection The direction for fetching rows from database tables.
     * @param fetchSize The number of rows that should be fetched from the database when more rows are needed.
     * @param maxFieldSize The maximum number of bytes that can be returned for character and binary column values.
     * @param maxRows The maximum number of rows that a <code>ResultSet</code> can produce.
     * @param queryTimeout The number of seconds the driver will wait for execution.
     */
    public StatementConfiguration(Integer fetchDirection, Integer fetchSize, Integer maxFieldSize, Integer maxRows,
                                  Integer queryTimeout) {
        this.fetchDirection = fetchDirection;
        this.fetchSize = fetchSize;
        this.maxFieldSize = maxFieldSize;
        this.maxRows = maxRows;
        this.queryTimeout = queryTimeout;
    }

    /**
     * Get the fetch direction.
     *
     * @return The direction to fetch or null if not set.
     */
    public Integer getFetchDirection() {
        return fetchDirection;
    }

    /**
     * Whether fetch direction is set.
     *
     * @return true if set, false otherwise.
     */
    public boolean isFetchDirectionSet() {
        return fetchDirection != null;
    }

    /**
     * Get the fetch size.
     *
     * @return The fetch size or null if not set.
     */
    public Integer getFetchSize() {
        return fetchSize;
    }

    /**
     * Whether fetch size is set.
     *
     * @return true if set, false otherwise.
     */
    public boolean isFetchSizeSet() {
        return fetchSize != null;
    }

    /**
     * Get the max field size.
     *
     * @return The max field size or null if not set.
     */
    public Integer getMaxFieldSize() {
        return maxFieldSize;
    }

    /**
     * Whether max field size is set.
     *
     * @return true if set, false otherwise.
     */
    public boolean isMaxFieldSizeSet() {
        return maxFieldSize != null;
    }

    /**
     * Get the max rows.
     *
     * @return The max rows or null if not set.
     */
    public Integer getMaxRows() {
        return maxRows;
    }

    /**
     * Whether max rows is set.
     *
     * @return true if set, false otherwise.
     */
    public boolean isMaxRowsSet() {
        return maxRows != null;
    }

    /**
     * Get the query timeout.
     *
     * @return The query timeout or null if not set.
     */
    public Integer getQueryTimeout() {
        return queryTimeout;
    }

    /**
     * Whether query timeout is set.
     *
     * @return true if set, false otherwise.
     */
    public boolean isQueryTimeoutSet() {
        return queryTimeout != null;
    }

    /**
     * Builder class for <code>StatementConfiguration</code> for more flexible construction.
     */
    public static final class Builder {
        private Integer fetchDirection;
        private Integer fetchSize;
        private Integer maxRows;
        private Integer queryTimeout;
        private Integer maxFieldSize;

        public Builder fetchDirection(final Integer fetchDirection) {
            this.fetchDirection = fetchDirection;
            return this;
        }

        public Builder fetchSize(final Integer fetchSize) {
            this.fetchSize = fetchSize;
            return this;
        }

        public Builder maxRows(final Integer maxRows) {
            this.maxRows = maxRows;
            return this;
        }

        public Builder queryTimeout(final Integer queryTimeout) {
            this.queryTimeout = queryTimeout;
            return this;
        }

        public Builder maxFieldSize(final Integer maxFieldSize) {
            this.maxFieldSize = maxFieldSize;
            return this;
        }

        public StatementConfiguration build() {
            return new StatementConfiguration(fetchDirection, fetchSize, maxFieldSize, maxRows, queryTimeout);
        }
    }
}
