package com.synnex.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author jellyh
 * @Since 2013-3-11
 */
public interface RowMapper {
	Object mapping(ResultSet rs) throws SQLException;
}
