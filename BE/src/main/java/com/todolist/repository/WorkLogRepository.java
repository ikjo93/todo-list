package com.todolist.repository;

import com.todolist.domain.WorkLog;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import org.springframework.stereotype.Repository;

@Repository
public class WorkLogRepository {

    private static final String CREATION = "등록";

    private final NamedParameterJdbcTemplate jdbc;

    public WorkLogRepository(DataSource dataSource) {
        jdbc = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<WorkLog> findAllByUserId(String userId) {
        return jdbc.query("SELECT title, action, previous_column, changed_column, updated_datetime "
            + "FROM work AS A JOIN work_log AS B ON A.id = B.work_id WHERE user_id = :userId ORDER BY updated_datetime DESC",
            Collections.singletonMap("userId", userId), workLogRowMapper());
    }

    public void saveCreationLog(Integer workId, String categoryName) {
        WorkLog workLog = new WorkLog(workId, CREATION, categoryName);
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(workLog);
        jdbc.update("INSERT INTO work_log (work_id, action, previous_column, updated_datetime)"
            + " VALUES (:workId, :action, :previousColumn, :updatedDateTime)", parameters);
    }

    private RowMapper<WorkLog> workLogRowMapper() {
        return (rs, rowNum) -> {
            WorkLog workLog = new WorkLog(
                rs.getString("title"),
                rs.getString("action"),
                rs.getString("previous_column"),
                rs.getString("changed_column"),
                rs.getTimestamp("updated_datetime").toLocalDateTime()
            );

            return workLog;
        };
    }
}