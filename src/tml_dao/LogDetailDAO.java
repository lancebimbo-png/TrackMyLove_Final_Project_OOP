package tml_dao;

import tml_db.DatabaseConnection;
import tml_model.LogDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogDetailDAO {

    public boolean insert(LogDetail detail) {
        String sql = "INSERT INTO log_details (log_id, medication_id, time_given, was_taken, side_effects) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detail.getLogId());
            ps.setInt(2, detail.getMedicationId());
            ps.setString(3, detail.getTimeGiven());
            ps.setBoolean(4, detail.isWasTaken());
            ps.setString(5, detail.getSideEffects());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Insert log detail failed: " + e.getMessage());
            return false;
        }
    }

    public List<LogDetail> findByLog(int logId) {
        List<LogDetail> list = new ArrayList<>();
        String sql = "SELECT ld.*, m.name AS medication_name, m.dosage, m.unit " +
                     "FROM log_details ld " +
                     "JOIN medications m ON ld.medication_id = m.id " +
                     "WHERE ld.log_id = ? " +
                     "ORDER BY ld.time_given";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, logId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("FindByLog failed: " + e.getMessage());
        }
        return list;
    }

    public LogDetail findById(int id) {
        String sql = "SELECT ld.*, m.name AS medication_name, m.dosage, m.unit " +
                     "FROM log_details ld " +
                     "JOIN medications m ON ld.medication_id = m.id " +
                     "WHERE ld.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.out.println("FindById detail failed: " + e.getMessage());
        }
        return null;
    }

    public boolean update(LogDetail detail) {
        String sql = "UPDATE log_details SET medication_id = ?, time_given = ?, was_taken = ?, side_effects = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detail.getMedicationId());
            ps.setString(2, detail.getTimeGiven());
            ps.setBoolean(3, detail.isWasTaken());
            ps.setString(4, detail.getSideEffects());
            ps.setInt(5, detail.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update log detail failed: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM log_details WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete log detail failed: " + e.getMessage());
            return false;
        }
    }

    public boolean alreadyLogged(int logId, int medicationId) {
        String sql = "SELECT id FROM log_details WHERE log_id = ? AND medication_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, logId);
            ps.setInt(2, medicationId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("AlreadyLogged check failed: " + e.getMessage());
            return false;
        }
    }

    private LogDetail mapRow(ResultSet rs) throws SQLException {
        LogDetail d = new LogDetail();
        d.setId(rs.getInt("id"));
        d.setLogId(rs.getInt("log_id"));
        d.setMedicationId(rs.getInt("medication_id"));
        d.setTimeGiven(rs.getString("time_given"));
        d.setWasTaken(rs.getBoolean("was_taken"));
        d.setSideEffects(rs.getString("side_effects"));
        d.setMedicationName(rs.getString("medication_name") + " " +
                            rs.getDouble("dosage") + rs.getString("unit"));
        return d;
    }
}